/* -*- c++ -*-
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
/// Declarations for TableMutatorSyncDispatchHandler.
/// This file contains declarations for TableMutatorSyncDispatchHandler, a class
/// for issuing and responding to RangeServer::commit_log_sync() requests.

#ifndef Hypertable_Lib_TableMutatorSyncDispatchHandler_h
#define Hypertable_Lib_TableMutatorSyncDispatchHandler_h

#include <Hypertable/Lib/RangeServer/Client.h>

#include <AsyncComm/Comm.h>
#include <AsyncComm/CommAddress.h>
#include <AsyncComm/DispatchHandler.h>
#include <AsyncComm/Event.h>

#include <Common/InetAddr.h>
#include <Common/StringExt.h>

#include <condition_variable>
#include <mutex>

namespace Hypertable {

  using namespace Lib;
  using namespace std;

  /**
   * This class is a DispatchHandler class that is used for collecting
   * asynchronous commit log sync requests.
   */
  class TableMutatorSyncDispatchHandler : public DispatchHandler {

  public:


    struct ErrorResult {
      CommAddress addr;
      int error;
      std::string msg;
    };

    /**
     * Constructor.
     */
    TableMutatorSyncDispatchHandler(Comm *comm, TableIdentifierManaged &table_id, time_t timeout);

    /**
     * Destructor
     */
    ~TableMutatorSyncDispatchHandler();

    /**
     * Adds
     */
    void add(const CommAddress &addr);

    /**
     * Dispatch method.  This gets called by the AsyncComm layer
     * when an event occurs in response to a previously sent
     * request that was supplied with this dispatch handler.
     *
     * @param event_ptr shared pointer to event object
     */
    virtual void handle(EventPtr &event_ptr);

    bool wait_for_completion();
    void retry();
    void get_errors(vector<ErrorResult> &errors);

  private:
    std::mutex m_mutex;
    std::condition_variable m_cond;
    int m_outstanding {};
    RangeServer::Client m_client;
    vector<ErrorResult> m_errors;
    CommAddressSet m_pending;
    TableIdentifierManaged &m_table_identifier;
  };
}


#endif // Hypertable_Lib_TableMutatorSyncDispatchHandler_h
