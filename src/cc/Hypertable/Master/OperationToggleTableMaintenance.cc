/*
 * Copyright (C) 2007-2015 Hypertable, Inc.
 *
 * This file is part of Hypertable.
 *
 * Hypertable is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of the
 * License, or any later version.
 *
 * Hypertable is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

/// @file
/// Definitions for OperationToggleTableMaintenance.
/// This file contains definitions for OperationToggleTableMaintenance, an
/// Operation class for toggling maintenance for a table either on or off.

#include <Common/Compat.h>
#include "OperationToggleTableMaintenance.h"

#include <Hypertable/Master/DispatchHandlerOperationToggleTableMaintenance.h>
#include <Hypertable/Master/Utility.h>

#include <Hypertable/Lib/Canonicalize.h>
#include <Hypertable/Lib/Key.h>

#include <Hyperspace/Session.h>

#include <Common/Error.h>
#include <Common/FailureInducer.h>
#include <Common/ScopeGuard.h>
#include <Common/Serialization.h>

#include <boost/algorithm/string.hpp>

#include <chrono>
#include <thread>

using namespace Hypertable;
using namespace Hypertable::Lib;
using namespace Hyperspace;
using namespace std;

OperationToggleTableMaintenance::OperationToggleTableMaintenance(ContextPtr &context,
                                                                 const std::string &table_name,
                                                                 bool toggle_on)
  : Operation(context, MetaLog::EntityType::OPERATION_TOGGLE_TABLE_MAINTENANCE),
    m_name(table_name), m_toggle_on(toggle_on) {
  Canonicalize::table_name(m_name);
  add_dependency(Dependency::INIT);
}

OperationToggleTableMaintenance::OperationToggleTableMaintenance(ContextPtr &context,
                                   const MetaLog::EntityHeader &header)
  : Operation(context, header) {
}


/// @detail
/// This method carries out the operation via the following states:
///
/// <table>
/// <tr>
/// <th>State</th>
/// <th>Description</th>
/// </tr>
/// <tr>
/// <td>INITIAL</td>
/// <td><ul>
/// <li>Maps table name (#m_name) to table ID and stores in #m_id</li>
/// <li>Transitions state to UPDATE_HYPERSPACE</li>
/// <li>Persists operation to MML and drops through to next state</li>
/// </ul></td>
/// </tr>
/// <tr>
/// <td>UPDATE_HYPERSPACE</td>
/// <td><ul>
/// <li>If #m_toggle_on is <i>true</i>, deletes the "maintenance_disabled"
///     attribute of the table ID file in Hyperspace</li>
/// <li>Otherwise, if #m_toggle_on is <i>false</i>, sets the
///     "maintenance_disabled" attribute of the table ID file in
///     Hyperspace</li>
/// <li>Dependencies are set to METADATA and #m_id + " move range"</li>
/// <li>Transitions state to SCAN_METADATA</li>
/// <li>Persists operation to MML and returns</li>
/// </ul></td>
/// </tr>
/// <tr>
/// <td>SCAN_METADATA</td>
/// <td><ul>
/// <li>Obtains list of servers via call to
///     Utility::get_table_server_set()</li>
/// <li>For each server in #m_completed, removes server as dependency</li>
/// <li>For each server not in #m_completed, adds server as dependency</li>
/// <li>Transitions state to ISSUE_REQUESTS</li>
/// <li>Persists operation to MML and returns</li>
/// </ul></td>
/// </tr>
/// <tr>
/// <td>ISSUE_REQUESTS</td>
/// <td><ul>
/// <li>Issues a toggle maintenance request to all servers in #m_servers and
///     waits for their completion</li>
/// <li>If any of the requests failed, the servers of the successfully
///     completed requests are added to #m_completed, the servers in
///     #m_servers are removed as dependencies, METADATA is added as a
///     dependency, #m_servers is cleared, state is transitioned back to
///     SCAN_METADATA, the operation sleeps for 5 seconds, the operation is
///     persisted to the MML and then the function returns</li>
/// <li>Otherwise, if all requsts completed successfully, the operation is
///     completed with a call to complete_ok()</li>
/// </ul></td>
/// </tr>
/// </table>
void OperationToggleTableMaintenance::execute() {
  bool is_namespace;
  int32_t state = get_state();

  HT_INFOF("Entering ToggleTableMaintenance-%lld (table=%s %s) state=%s",
           (Lld)header.id, m_name.c_str(), m_toggle_on ? "ON" : "OFF",
           OperationState::get_text(state));

  switch (state) {

  case OperationState::INITIAL:
    if (m_context->namemap->name_to_id(m_name, m_id, &is_namespace)) {
      if (is_namespace) {
        complete_error(Error::TABLE_NOT_FOUND, format("%s is a namespace", m_name.c_str()));
        break;
      }
    }
    else {
      complete_error(Error::TABLE_NOT_FOUND, m_name);
      break;
    }
    set_state(OperationState::UPDATE_HYPERSPACE);
    m_context->mml_writer->record_state(shared_from_this());
    HT_MAYBE_FAIL("toggle-table-maintenance-INITIAL");

    // drop through ...

  case OperationState::UPDATE_HYPERSPACE:
    try {
      String tablefile = m_context->toplevel_dir + "/tables/" + m_id;
      if (m_toggle_on) {
        uint64_t handle = 0;
        HT_ON_SCOPE_EXIT(&Hyperspace::close_handle_ptr, m_context->hyperspace, &handle);
        handle = m_context->hyperspace->open(tablefile, OPEN_FLAG_READ);
        m_context->hyperspace->attr_del(handle, "maintenance_disabled");
      }
      else
        m_context->hyperspace->attr_set(tablefile, "maintenance_disabled", "1", 1);
    }
    catch (Exception &e) {
      if (e.code() != Error::HYPERSPACE_ATTR_NOT_FOUND) {
        HT_ERRORF("Problem %s 'maintenance_disabled' attr for %s (%s, %s)",
                  m_toggle_on ? "setting" : "deleting", m_id.c_str(),
                  Error::get_text(e.code()), e.what());
        complete_error(e);
        break;
      }
    }
    HT_MAYBE_FAIL("toggle-table-maintenance-UPDATE_HYPERSPACE-1");
    {
      lock_guard<mutex> lock(m_mutex);
      m_dependencies.erase(Dependency::INIT);
      m_dependencies.insert(Dependency::METADATA);
      m_dependencies.insert(m_id + " move range");
      m_state = OperationState::SCAN_METADATA;
    }
    m_context->mml_writer->record_state(shared_from_this());
    HT_MAYBE_FAIL("toggle-table-maintenance-UPDATE_HYPERSPACE-2");
    break;

  case OperationState::SCAN_METADATA:
    {
      std::set<std::string> servers;

      // Determine servers that hold the table's ranges
      if (!m_context->test_mode)
        Utility::get_table_server_set(m_context, m_id, "", servers);
      else
        m_context->get_available_servers(servers);

      // Populate m_servers and m_dependencies
      {
        lock_guard<mutex> lock(m_mutex);
        m_servers.clear();
        for (auto &server : servers) {
          if (m_completed.count(server))
            m_dependencies.erase(server);
          else {
            m_servers.insert(server);
            m_dependencies.insert(server);
          }
        }
        m_state = OperationState::ISSUE_REQUESTS;
      }
    }
    m_context->mml_writer->record_state(shared_from_this());
    HT_MAYBE_FAIL("toggle-table-maintenance-SCAN_METADATA");
    break;

  case OperationState::ISSUE_REQUESTS:
    if (!m_context->test_mode) {
      TableIdentifier table;
      table.id = m_id.c_str();
      table.generation = 0;
      DispatchHandlerOperationPtr op_handler =
        make_shared<DispatchHandlerOperationToggleTableMaintenance>(m_context, table, m_toggle_on);
      op_handler->start(m_servers);
      if (!op_handler->wait_for_completion()) {
        std::set<DispatchHandlerOperation::Result> results;
        op_handler->get_results(results);
        for (auto &result : results) {
          if (result.error == Error::OK) {
            lock_guard<mutex> lock(m_mutex);
            m_completed.insert(result.location);
          }
          else
            HT_WARNF("Error at %s - %s (%s)", result.location.c_str(),
                     Error::get_text(result.error), result.msg.c_str());
        }

        {
          lock_guard<mutex> lock(m_mutex);
          for (auto s : m_servers)
            m_dependencies.erase(s);
          m_dependencies.insert(Dependency::METADATA);
          m_servers.clear();
          m_state = OperationState::SCAN_METADATA;
        }
        this_thread::sleep_for(chrono::milliseconds(5000));
        m_context->mml_writer->record_state(shared_from_this());
        break;
      }
    }
    HT_MAYBE_FAIL("toggle-table-maintenance-ISSUE_REQUESTS");
    complete_ok();
    break;

  default:
    HT_FATALF("Unrecognized state %d", state);
  }

  HT_INFOF("Leaving ToggleTableMaintenance-%lld (table=%s %s) state=%s",
           (Lld)header.id, m_name.c_str(), m_toggle_on ? "ON" : "OFF",
           OperationState::get_text(get_state()));
}


void OperationToggleTableMaintenance::display_state(std::ostream &os) {
  os << " table_name=" << m_name << " table_id=" << m_id
     << " " << (m_toggle_on ? "ON" : "OFF");
}

uint8_t OperationToggleTableMaintenance::encoding_version_state() const {
  return 1;
}

size_t OperationToggleTableMaintenance::encoded_length_state() const {
  size_t length = 1 + Serialization::encoded_length_vstr(m_name) +
    Serialization::encoded_length_vstr(m_id);
  length += 4;
  for (const auto &location : m_servers)
    length += Serialization::encoded_length_vstr(location);
  length += 4;
  for (const auto &location : m_completed)
    length += Serialization::encoded_length_vstr(location);
  return length;
}

void OperationToggleTableMaintenance::encode_state(uint8_t **bufp) const {
  Serialization::encode_vstr(bufp, m_name);
  Serialization::encode_vstr(bufp, m_id);
  Serialization::encode_bool(bufp, m_toggle_on);
  Serialization::encode_i32(bufp, m_servers.size());
  for (const auto &location : m_servers)
    Serialization::encode_vstr(bufp, location);
  Serialization::encode_i32(bufp, m_completed.size());
  for (const auto &location : m_completed)
    Serialization::encode_vstr(bufp, location);
}

void OperationToggleTableMaintenance::decode_state(uint8_t version, const uint8_t **bufp, size_t *remainp) {
  decode_state_old(version, bufp, remainp);
}

void OperationToggleTableMaintenance::decode_state_old(uint8_t version, const uint8_t **bufp, size_t *remainp) {
  m_name = Serialization::decode_vstr(bufp, remainp);
  m_id = Serialization::decode_vstr(bufp, remainp);
  m_toggle_on = Serialization::decode_bool(bufp, remainp);
  size_t length = Serialization::decode_i32(bufp, remainp);
  for (size_t i=0; i<length; i++)
    m_servers.insert( Serialization::decode_vstr(bufp, remainp) );
  length = Serialization::decode_i32(bufp, remainp);
  for (size_t i=0; i<length; i++)
    m_completed.insert( Serialization::decode_vstr(bufp, remainp) );
}

const String OperationToggleTableMaintenance::name() {
  return "OperationToggleTableMaintenance";
}

const String OperationToggleTableMaintenance::label() {
  return format("Toggle Table Maintenance (table=%s, %s)",
                m_name.c_str(), m_toggle_on ? "ON" : "OFF");
}
