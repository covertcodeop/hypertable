/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package org.hypertable.thriftgen;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.thrift.*;
import org.apache.thrift.async.*;
import org.apache.thrift.meta_data.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;

/**
 * Describes an AccessGroup
 * <dl>
 *   <dt>name</dt>
 *   <dd>Name of the access group</dd>
 * 
 *   <dt>in_memory</dt>
 *   <dd>Is this access group in memory</dd>
 * 
 *   <dt>replication</dt>
 *   <dd>Replication factor for this AG</dd>
 * 
 *   <dt>blocksize</dt>
 *   <dd>Specifies blocksize for this AG</dd>
 * 
 *   <dt>compressor</dt>
 *   <dd>Specifies compressor for this AG</dd>
 * 
 *   <dt>bloom_filter</dt>
 *   <dd>Specifies bloom filter type</dd>
 * 
 *   <dt>columns</dt>
 *   <dd>Specifies list of column families in this AG</dd>
 * </dl>
 */
public class AccessGroup implements TBase<AccessGroup, AccessGroup._Fields>, java.io.Serializable, Cloneable {
  private static final TStruct STRUCT_DESC = new TStruct("AccessGroup");

  private static final TField NAME_FIELD_DESC = new TField("name", TType.STRING, (short)1);
  private static final TField IN_MEMORY_FIELD_DESC = new TField("in_memory", TType.BOOL, (short)2);
  private static final TField REPLICATION_FIELD_DESC = new TField("replication", TType.I16, (short)3);
  private static final TField BLOCKSIZE_FIELD_DESC = new TField("blocksize", TType.I32, (short)4);
  private static final TField COMPRESSOR_FIELD_DESC = new TField("compressor", TType.STRING, (short)5);
  private static final TField BLOOM_FILTER_FIELD_DESC = new TField("bloom_filter", TType.STRING, (short)6);
  private static final TField COLUMNS_FIELD_DESC = new TField("columns", TType.LIST, (short)7);

  public String name;
  public boolean in_memory;
  public short replication;
  public int blocksize;
  public String compressor;
  public String bloom_filter;
  public List<ColumnFamily> columns;

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements TFieldIdEnum {
    NAME((short)1, "name"),
    IN_MEMORY((short)2, "in_memory"),
    REPLICATION((short)3, "replication"),
    BLOCKSIZE((short)4, "blocksize"),
    COMPRESSOR((short)5, "compressor"),
    BLOOM_FILTER((short)6, "bloom_filter"),
    COLUMNS((short)7, "columns");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // NAME
          return NAME;
        case 2: // IN_MEMORY
          return IN_MEMORY;
        case 3: // REPLICATION
          return REPLICATION;
        case 4: // BLOCKSIZE
          return BLOCKSIZE;
        case 5: // COMPRESSOR
          return COMPRESSOR;
        case 6: // BLOOM_FILTER
          return BLOOM_FILTER;
        case 7: // COLUMNS
          return COLUMNS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __IN_MEMORY_ISSET_ID = 0;
  private static final int __REPLICATION_ISSET_ID = 1;
  private static final int __BLOCKSIZE_ISSET_ID = 2;
  private BitSet __isset_bit_vector = new BitSet(3);

  public static final Map<_Fields, FieldMetaData> metaDataMap;
  static {
    Map<_Fields, FieldMetaData> tmpMap = new EnumMap<_Fields, FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NAME, new FieldMetaData("name", TFieldRequirementType.OPTIONAL, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.IN_MEMORY, new FieldMetaData("in_memory", TFieldRequirementType.OPTIONAL, 
        new FieldValueMetaData(TType.BOOL)));
    tmpMap.put(_Fields.REPLICATION, new FieldMetaData("replication", TFieldRequirementType.OPTIONAL, 
        new FieldValueMetaData(TType.I16)));
    tmpMap.put(_Fields.BLOCKSIZE, new FieldMetaData("blocksize", TFieldRequirementType.OPTIONAL, 
        new FieldValueMetaData(TType.I32)));
    tmpMap.put(_Fields.COMPRESSOR, new FieldMetaData("compressor", TFieldRequirementType.OPTIONAL, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.BLOOM_FILTER, new FieldMetaData("bloom_filter", TFieldRequirementType.OPTIONAL, 
        new FieldValueMetaData(TType.STRING)));
    tmpMap.put(_Fields.COLUMNS, new FieldMetaData("columns", TFieldRequirementType.OPTIONAL, 
        new ListMetaData(TType.LIST, 
            new StructMetaData(TType.STRUCT, ColumnFamily.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    FieldMetaData.addStructMetaDataMap(AccessGroup.class, metaDataMap);
  }

  public AccessGroup() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public AccessGroup(AccessGroup other) {
    __isset_bit_vector.clear();
    __isset_bit_vector.or(other.__isset_bit_vector);
    if (other.isSetName()) {
      this.name = other.name;
    }
    this.in_memory = other.in_memory;
    this.replication = other.replication;
    this.blocksize = other.blocksize;
    if (other.isSetCompressor()) {
      this.compressor = other.compressor;
    }
    if (other.isSetBloom_filter()) {
      this.bloom_filter = other.bloom_filter;
    }
    if (other.isSetColumns()) {
      List<ColumnFamily> __this__columns = new ArrayList<ColumnFamily>();
      for (ColumnFamily other_element : other.columns) {
        __this__columns.add(new ColumnFamily(other_element));
      }
      this.columns = __this__columns;
    }
  }

  public AccessGroup deepCopy() {
    return new AccessGroup(this);
  }

  @Override
  public void clear() {
    this.name = null;
    setIn_memoryIsSet(false);
    this.in_memory = false;
    setReplicationIsSet(false);
    this.replication = 0;
    setBlocksizeIsSet(false);
    this.blocksize = 0;
    this.compressor = null;
    this.bloom_filter = null;
    this.columns = null;
  }

  public String getName() {
    return this.name;
  }

  public AccessGroup setName(String name) {
    this.name = name;
    return this;
  }

  public void unsetName() {
    this.name = null;
  }

  /** Returns true if field name is set (has been asigned a value) and false otherwise */
  public boolean isSetName() {
    return this.name != null;
  }

  public void setNameIsSet(boolean value) {
    if (!value) {
      this.name = null;
    }
  }

  public boolean isIn_memory() {
    return this.in_memory;
  }

  public AccessGroup setIn_memory(boolean in_memory) {
    this.in_memory = in_memory;
    setIn_memoryIsSet(true);
    return this;
  }

  public void unsetIn_memory() {
    __isset_bit_vector.clear(__IN_MEMORY_ISSET_ID);
  }

  /** Returns true if field in_memory is set (has been asigned a value) and false otherwise */
  public boolean isSetIn_memory() {
    return __isset_bit_vector.get(__IN_MEMORY_ISSET_ID);
  }

  public void setIn_memoryIsSet(boolean value) {
    __isset_bit_vector.set(__IN_MEMORY_ISSET_ID, value);
  }

  public short getReplication() {
    return this.replication;
  }

  public AccessGroup setReplication(short replication) {
    this.replication = replication;
    setReplicationIsSet(true);
    return this;
  }

  public void unsetReplication() {
    __isset_bit_vector.clear(__REPLICATION_ISSET_ID);
  }

  /** Returns true if field replication is set (has been asigned a value) and false otherwise */
  public boolean isSetReplication() {
    return __isset_bit_vector.get(__REPLICATION_ISSET_ID);
  }

  public void setReplicationIsSet(boolean value) {
    __isset_bit_vector.set(__REPLICATION_ISSET_ID, value);
  }

  public int getBlocksize() {
    return this.blocksize;
  }

  public AccessGroup setBlocksize(int blocksize) {
    this.blocksize = blocksize;
    setBlocksizeIsSet(true);
    return this;
  }

  public void unsetBlocksize() {
    __isset_bit_vector.clear(__BLOCKSIZE_ISSET_ID);
  }

  /** Returns true if field blocksize is set (has been asigned a value) and false otherwise */
  public boolean isSetBlocksize() {
    return __isset_bit_vector.get(__BLOCKSIZE_ISSET_ID);
  }

  public void setBlocksizeIsSet(boolean value) {
    __isset_bit_vector.set(__BLOCKSIZE_ISSET_ID, value);
  }

  public String getCompressor() {
    return this.compressor;
  }

  public AccessGroup setCompressor(String compressor) {
    this.compressor = compressor;
    return this;
  }

  public void unsetCompressor() {
    this.compressor = null;
  }

  /** Returns true if field compressor is set (has been asigned a value) and false otherwise */
  public boolean isSetCompressor() {
    return this.compressor != null;
  }

  public void setCompressorIsSet(boolean value) {
    if (!value) {
      this.compressor = null;
    }
  }

  public String getBloom_filter() {
    return this.bloom_filter;
  }

  public AccessGroup setBloom_filter(String bloom_filter) {
    this.bloom_filter = bloom_filter;
    return this;
  }

  public void unsetBloom_filter() {
    this.bloom_filter = null;
  }

  /** Returns true if field bloom_filter is set (has been asigned a value) and false otherwise */
  public boolean isSetBloom_filter() {
    return this.bloom_filter != null;
  }

  public void setBloom_filterIsSet(boolean value) {
    if (!value) {
      this.bloom_filter = null;
    }
  }

  public int getColumnsSize() {
    return (this.columns == null) ? 0 : this.columns.size();
  }

  public java.util.Iterator<ColumnFamily> getColumnsIterator() {
    return (this.columns == null) ? null : this.columns.iterator();
  }

  public void addToColumns(ColumnFamily elem) {
    if (this.columns == null) {
      this.columns = new ArrayList<ColumnFamily>();
    }
    this.columns.add(elem);
  }

  public List<ColumnFamily> getColumns() {
    return this.columns;
  }

  public AccessGroup setColumns(List<ColumnFamily> columns) {
    this.columns = columns;
    return this;
  }

  public void unsetColumns() {
    this.columns = null;
  }

  /** Returns true if field columns is set (has been asigned a value) and false otherwise */
  public boolean isSetColumns() {
    return this.columns != null;
  }

  public void setColumnsIsSet(boolean value) {
    if (!value) {
      this.columns = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case NAME:
      if (value == null) {
        unsetName();
      } else {
        setName((String)value);
      }
      break;

    case IN_MEMORY:
      if (value == null) {
        unsetIn_memory();
      } else {
        setIn_memory((Boolean)value);
      }
      break;

    case REPLICATION:
      if (value == null) {
        unsetReplication();
      } else {
        setReplication((Short)value);
      }
      break;

    case BLOCKSIZE:
      if (value == null) {
        unsetBlocksize();
      } else {
        setBlocksize((Integer)value);
      }
      break;

    case COMPRESSOR:
      if (value == null) {
        unsetCompressor();
      } else {
        setCompressor((String)value);
      }
      break;

    case BLOOM_FILTER:
      if (value == null) {
        unsetBloom_filter();
      } else {
        setBloom_filter((String)value);
      }
      break;

    case COLUMNS:
      if (value == null) {
        unsetColumns();
      } else {
        setColumns((List<ColumnFamily>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case NAME:
      return getName();

    case IN_MEMORY:
      return new Boolean(isIn_memory());

    case REPLICATION:
      return new Short(getReplication());

    case BLOCKSIZE:
      return new Integer(getBlocksize());

    case COMPRESSOR:
      return getCompressor();

    case BLOOM_FILTER:
      return getBloom_filter();

    case COLUMNS:
      return getColumns();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been asigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case NAME:
      return isSetName();
    case IN_MEMORY:
      return isSetIn_memory();
    case REPLICATION:
      return isSetReplication();
    case BLOCKSIZE:
      return isSetBlocksize();
    case COMPRESSOR:
      return isSetCompressor();
    case BLOOM_FILTER:
      return isSetBloom_filter();
    case COLUMNS:
      return isSetColumns();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof AccessGroup)
      return this.equals((AccessGroup)that);
    return false;
  }

  public boolean equals(AccessGroup that) {
    if (that == null)
      return false;

    boolean this_present_name = true && this.isSetName();
    boolean that_present_name = true && that.isSetName();
    if (this_present_name || that_present_name) {
      if (!(this_present_name && that_present_name))
        return false;
      if (!this.name.equals(that.name))
        return false;
    }

    boolean this_present_in_memory = true && this.isSetIn_memory();
    boolean that_present_in_memory = true && that.isSetIn_memory();
    if (this_present_in_memory || that_present_in_memory) {
      if (!(this_present_in_memory && that_present_in_memory))
        return false;
      if (this.in_memory != that.in_memory)
        return false;
    }

    boolean this_present_replication = true && this.isSetReplication();
    boolean that_present_replication = true && that.isSetReplication();
    if (this_present_replication || that_present_replication) {
      if (!(this_present_replication && that_present_replication))
        return false;
      if (this.replication != that.replication)
        return false;
    }

    boolean this_present_blocksize = true && this.isSetBlocksize();
    boolean that_present_blocksize = true && that.isSetBlocksize();
    if (this_present_blocksize || that_present_blocksize) {
      if (!(this_present_blocksize && that_present_blocksize))
        return false;
      if (this.blocksize != that.blocksize)
        return false;
    }

    boolean this_present_compressor = true && this.isSetCompressor();
    boolean that_present_compressor = true && that.isSetCompressor();
    if (this_present_compressor || that_present_compressor) {
      if (!(this_present_compressor && that_present_compressor))
        return false;
      if (!this.compressor.equals(that.compressor))
        return false;
    }

    boolean this_present_bloom_filter = true && this.isSetBloom_filter();
    boolean that_present_bloom_filter = true && that.isSetBloom_filter();
    if (this_present_bloom_filter || that_present_bloom_filter) {
      if (!(this_present_bloom_filter && that_present_bloom_filter))
        return false;
      if (!this.bloom_filter.equals(that.bloom_filter))
        return false;
    }

    boolean this_present_columns = true && this.isSetColumns();
    boolean that_present_columns = true && that.isSetColumns();
    if (this_present_columns || that_present_columns) {
      if (!(this_present_columns && that_present_columns))
        return false;
      if (!this.columns.equals(that.columns))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(AccessGroup other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    AccessGroup typedOther = (AccessGroup)other;

    lastComparison = Boolean.valueOf(isSetName()).compareTo(typedOther.isSetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetName()) {
      lastComparison = TBaseHelper.compareTo(this.name, typedOther.name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetIn_memory()).compareTo(typedOther.isSetIn_memory());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIn_memory()) {
      lastComparison = TBaseHelper.compareTo(this.in_memory, typedOther.in_memory);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetReplication()).compareTo(typedOther.isSetReplication());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetReplication()) {
      lastComparison = TBaseHelper.compareTo(this.replication, typedOther.replication);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBlocksize()).compareTo(typedOther.isSetBlocksize());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBlocksize()) {
      lastComparison = TBaseHelper.compareTo(this.blocksize, typedOther.blocksize);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCompressor()).compareTo(typedOther.isSetCompressor());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCompressor()) {
      lastComparison = TBaseHelper.compareTo(this.compressor, typedOther.compressor);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBloom_filter()).compareTo(typedOther.isSetBloom_filter());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBloom_filter()) {
      lastComparison = TBaseHelper.compareTo(this.bloom_filter, typedOther.bloom_filter);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetColumns()).compareTo(typedOther.isSetColumns());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetColumns()) {
      lastComparison = TBaseHelper.compareTo(this.columns, typedOther.columns);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(TProtocol iprot) throws TException {
    TField field;
    iprot.readStructBegin();
    while (true)
    {
      field = iprot.readFieldBegin();
      if (field.type == TType.STOP) { 
        break;
      }
      switch (field.id) {
        case 1: // NAME
          if (field.type == TType.STRING) {
            this.name = iprot.readString();
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 2: // IN_MEMORY
          if (field.type == TType.BOOL) {
            this.in_memory = iprot.readBool();
            setIn_memoryIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 3: // REPLICATION
          if (field.type == TType.I16) {
            this.replication = iprot.readI16();
            setReplicationIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 4: // BLOCKSIZE
          if (field.type == TType.I32) {
            this.blocksize = iprot.readI32();
            setBlocksizeIsSet(true);
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 5: // COMPRESSOR
          if (field.type == TType.STRING) {
            this.compressor = iprot.readString();
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 6: // BLOOM_FILTER
          if (field.type == TType.STRING) {
            this.bloom_filter = iprot.readString();
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        case 7: // COLUMNS
          if (field.type == TType.LIST) {
            {
              TList _list12 = iprot.readListBegin();
              this.columns = new ArrayList<ColumnFamily>(_list12.size);
              for (int _i13 = 0; _i13 < _list12.size; ++_i13)
              {
                ColumnFamily _elem14;
                _elem14 = new ColumnFamily();
                _elem14.read(iprot);
                this.columns.add(_elem14);
              }
              iprot.readListEnd();
            }
          } else { 
            TProtocolUtil.skip(iprot, field.type);
          }
          break;
        default:
          TProtocolUtil.skip(iprot, field.type);
      }
      iprot.readFieldEnd();
    }
    iprot.readStructEnd();

    // check for required fields of primitive type, which can't be checked in the validate method
    validate();
  }

  public void write(TProtocol oprot) throws TException {
    validate();

    oprot.writeStructBegin(STRUCT_DESC);
    if (this.name != null) {
      if (isSetName()) {
        oprot.writeFieldBegin(NAME_FIELD_DESC);
        oprot.writeString(this.name);
        oprot.writeFieldEnd();
      }
    }
    if (isSetIn_memory()) {
      oprot.writeFieldBegin(IN_MEMORY_FIELD_DESC);
      oprot.writeBool(this.in_memory);
      oprot.writeFieldEnd();
    }
    if (isSetReplication()) {
      oprot.writeFieldBegin(REPLICATION_FIELD_DESC);
      oprot.writeI16(this.replication);
      oprot.writeFieldEnd();
    }
    if (isSetBlocksize()) {
      oprot.writeFieldBegin(BLOCKSIZE_FIELD_DESC);
      oprot.writeI32(this.blocksize);
      oprot.writeFieldEnd();
    }
    if (this.compressor != null) {
      if (isSetCompressor()) {
        oprot.writeFieldBegin(COMPRESSOR_FIELD_DESC);
        oprot.writeString(this.compressor);
        oprot.writeFieldEnd();
      }
    }
    if (this.bloom_filter != null) {
      if (isSetBloom_filter()) {
        oprot.writeFieldBegin(BLOOM_FILTER_FIELD_DESC);
        oprot.writeString(this.bloom_filter);
        oprot.writeFieldEnd();
      }
    }
    if (this.columns != null) {
      if (isSetColumns()) {
        oprot.writeFieldBegin(COLUMNS_FIELD_DESC);
        {
          oprot.writeListBegin(new TList(TType.STRUCT, this.columns.size()));
          for (ColumnFamily _iter15 : this.columns)
          {
            _iter15.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
    }
    oprot.writeFieldStop();
    oprot.writeStructEnd();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("AccessGroup(");
    boolean first = true;

    if (isSetName()) {
      sb.append("name:");
      if (this.name == null) {
        sb.append("null");
      } else {
        sb.append(this.name);
      }
      first = false;
    }
    if (isSetIn_memory()) {
      if (!first) sb.append(", ");
      sb.append("in_memory:");
      sb.append(this.in_memory);
      first = false;
    }
    if (isSetReplication()) {
      if (!first) sb.append(", ");
      sb.append("replication:");
      sb.append(this.replication);
      first = false;
    }
    if (isSetBlocksize()) {
      if (!first) sb.append(", ");
      sb.append("blocksize:");
      sb.append(this.blocksize);
      first = false;
    }
    if (isSetCompressor()) {
      if (!first) sb.append(", ");
      sb.append("compressor:");
      if (this.compressor == null) {
        sb.append("null");
      } else {
        sb.append(this.compressor);
      }
      first = false;
    }
    if (isSetBloom_filter()) {
      if (!first) sb.append(", ");
      sb.append("bloom_filter:");
      if (this.bloom_filter == null) {
        sb.append("null");
      } else {
        sb.append(this.bloom_filter);
      }
      first = false;
    }
    if (isSetColumns()) {
      if (!first) sb.append(", ");
      sb.append("columns:");
      if (this.columns == null) {
        sb.append("null");
      } else {
        sb.append(this.columns);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws TException {
    // check for required fields
  }

}

