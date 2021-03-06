/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package usyd.distributed.scheduler.peacock.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-08-02")
public class TGlobalStateUpdateRequest implements org.apache.thrift.TBase<TGlobalStateUpdateRequest, TGlobalStateUpdateRequest._Fields>, java.io.Serializable, Cloneable, Comparable<TGlobalStateUpdateRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TGlobalStateUpdateRequest");

  private static final org.apache.thrift.protocol.TField NUM_TASKS_FIELD_DESC = new org.apache.thrift.protocol.TField("numTasks", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField AVG_EXECUTION_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("avgExecutionTime", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField ADDED_FIELD_DESC = new org.apache.thrift.protocol.TField("added", org.apache.thrift.protocol.TType.BOOL, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TGlobalStateUpdateRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TGlobalStateUpdateRequestTupleSchemeFactory();

  public int numTasks; // required
  public long avgExecutionTime; // required
  public boolean added; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    NUM_TASKS((short)1, "numTasks"),
    AVG_EXECUTION_TIME((short)2, "avgExecutionTime"),
    ADDED((short)3, "added");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // NUM_TASKS
          return NUM_TASKS;
        case 2: // AVG_EXECUTION_TIME
          return AVG_EXECUTION_TIME;
        case 3: // ADDED
          return ADDED;
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
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __NUMTASKS_ISSET_ID = 0;
  private static final int __AVGEXECUTIONTIME_ISSET_ID = 1;
  private static final int __ADDED_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.NUM_TASKS, new org.apache.thrift.meta_data.FieldMetaData("numTasks", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.AVG_EXECUTION_TIME, new org.apache.thrift.meta_data.FieldMetaData("avgExecutionTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ADDED, new org.apache.thrift.meta_data.FieldMetaData("added", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TGlobalStateUpdateRequest.class, metaDataMap);
  }

  public TGlobalStateUpdateRequest() {
  }

  public TGlobalStateUpdateRequest(
    int numTasks,
    long avgExecutionTime,
    boolean added)
  {
    this();
    this.numTasks = numTasks;
    setNumTasksIsSet(true);
    this.avgExecutionTime = avgExecutionTime;
    setAvgExecutionTimeIsSet(true);
    this.added = added;
    setAddedIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TGlobalStateUpdateRequest(TGlobalStateUpdateRequest other) {
    __isset_bitfield = other.__isset_bitfield;
    this.numTasks = other.numTasks;
    this.avgExecutionTime = other.avgExecutionTime;
    this.added = other.added;
  }

  public TGlobalStateUpdateRequest deepCopy() {
    return new TGlobalStateUpdateRequest(this);
  }

  @Override
  public void clear() {
    setNumTasksIsSet(false);
    this.numTasks = 0;
    setAvgExecutionTimeIsSet(false);
    this.avgExecutionTime = 0;
    setAddedIsSet(false);
    this.added = false;
  }

  public int getNumTasks() {
    return this.numTasks;
  }

  public TGlobalStateUpdateRequest setNumTasks(int numTasks) {
    this.numTasks = numTasks;
    setNumTasksIsSet(true);
    return this;
  }

  public void unsetNumTasks() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __NUMTASKS_ISSET_ID);
  }

  /** Returns true if field numTasks is set (has been assigned a value) and false otherwise */
  public boolean isSetNumTasks() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __NUMTASKS_ISSET_ID);
  }

  public void setNumTasksIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __NUMTASKS_ISSET_ID, value);
  }

  public long getAvgExecutionTime() {
    return this.avgExecutionTime;
  }

  public TGlobalStateUpdateRequest setAvgExecutionTime(long avgExecutionTime) {
    this.avgExecutionTime = avgExecutionTime;
    setAvgExecutionTimeIsSet(true);
    return this;
  }

  public void unsetAvgExecutionTime() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __AVGEXECUTIONTIME_ISSET_ID);
  }

  /** Returns true if field avgExecutionTime is set (has been assigned a value) and false otherwise */
  public boolean isSetAvgExecutionTime() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __AVGEXECUTIONTIME_ISSET_ID);
  }

  public void setAvgExecutionTimeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __AVGEXECUTIONTIME_ISSET_ID, value);
  }

  public boolean isAdded() {
    return this.added;
  }

  public TGlobalStateUpdateRequest setAdded(boolean added) {
    this.added = added;
    setAddedIsSet(true);
    return this;
  }

  public void unsetAdded() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ADDED_ISSET_ID);
  }

  /** Returns true if field added is set (has been assigned a value) and false otherwise */
  public boolean isSetAdded() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ADDED_ISSET_ID);
  }

  public void setAddedIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ADDED_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case NUM_TASKS:
      if (value == null) {
        unsetNumTasks();
      } else {
        setNumTasks((java.lang.Integer)value);
      }
      break;

    case AVG_EXECUTION_TIME:
      if (value == null) {
        unsetAvgExecutionTime();
      } else {
        setAvgExecutionTime((java.lang.Long)value);
      }
      break;

    case ADDED:
      if (value == null) {
        unsetAdded();
      } else {
        setAdded((java.lang.Boolean)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case NUM_TASKS:
      return getNumTasks();

    case AVG_EXECUTION_TIME:
      return getAvgExecutionTime();

    case ADDED:
      return isAdded();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case NUM_TASKS:
      return isSetNumTasks();
    case AVG_EXECUTION_TIME:
      return isSetAvgExecutionTime();
    case ADDED:
      return isSetAdded();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof TGlobalStateUpdateRequest)
      return this.equals((TGlobalStateUpdateRequest)that);
    return false;
  }

  public boolean equals(TGlobalStateUpdateRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_numTasks = true;
    boolean that_present_numTasks = true;
    if (this_present_numTasks || that_present_numTasks) {
      if (!(this_present_numTasks && that_present_numTasks))
        return false;
      if (this.numTasks != that.numTasks)
        return false;
    }

    boolean this_present_avgExecutionTime = true;
    boolean that_present_avgExecutionTime = true;
    if (this_present_avgExecutionTime || that_present_avgExecutionTime) {
      if (!(this_present_avgExecutionTime && that_present_avgExecutionTime))
        return false;
      if (this.avgExecutionTime != that.avgExecutionTime)
        return false;
    }

    boolean this_present_added = true;
    boolean that_present_added = true;
    if (this_present_added || that_present_added) {
      if (!(this_present_added && that_present_added))
        return false;
      if (this.added != that.added)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + numTasks;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(avgExecutionTime);

    hashCode = hashCode * 8191 + ((added) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(TGlobalStateUpdateRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetNumTasks()).compareTo(other.isSetNumTasks());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNumTasks()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.numTasks, other.numTasks);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetAvgExecutionTime()).compareTo(other.isSetAvgExecutionTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAvgExecutionTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.avgExecutionTime, other.avgExecutionTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetAdded()).compareTo(other.isSetAdded());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAdded()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.added, other.added);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TGlobalStateUpdateRequest(");
    boolean first = true;

    sb.append("numTasks:");
    sb.append(this.numTasks);
    first = false;
    if (!first) sb.append(", ");
    sb.append("avgExecutionTime:");
    sb.append(this.avgExecutionTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("added:");
    sb.append(this.added);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TGlobalStateUpdateRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TGlobalStateUpdateRequestStandardScheme getScheme() {
      return new TGlobalStateUpdateRequestStandardScheme();
    }
  }

  private static class TGlobalStateUpdateRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<TGlobalStateUpdateRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TGlobalStateUpdateRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // NUM_TASKS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.numTasks = iprot.readI32();
              struct.setNumTasksIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // AVG_EXECUTION_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.avgExecutionTime = iprot.readI64();
              struct.setAvgExecutionTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ADDED
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.added = iprot.readBool();
              struct.setAddedIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TGlobalStateUpdateRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(NUM_TASKS_FIELD_DESC);
      oprot.writeI32(struct.numTasks);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(AVG_EXECUTION_TIME_FIELD_DESC);
      oprot.writeI64(struct.avgExecutionTime);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(ADDED_FIELD_DESC);
      oprot.writeBool(struct.added);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TGlobalStateUpdateRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TGlobalStateUpdateRequestTupleScheme getScheme() {
      return new TGlobalStateUpdateRequestTupleScheme();
    }
  }

  private static class TGlobalStateUpdateRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<TGlobalStateUpdateRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TGlobalStateUpdateRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetNumTasks()) {
        optionals.set(0);
      }
      if (struct.isSetAvgExecutionTime()) {
        optionals.set(1);
      }
      if (struct.isSetAdded()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetNumTasks()) {
        oprot.writeI32(struct.numTasks);
      }
      if (struct.isSetAvgExecutionTime()) {
        oprot.writeI64(struct.avgExecutionTime);
      }
      if (struct.isSetAdded()) {
        oprot.writeBool(struct.added);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TGlobalStateUpdateRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.numTasks = iprot.readI32();
        struct.setNumTasksIsSet(true);
      }
      if (incoming.get(1)) {
        struct.avgExecutionTime = iprot.readI64();
        struct.setAvgExecutionTimeIsSet(true);
      }
      if (incoming.get(2)) {
        struct.added = iprot.readBool();
        struct.setAddedIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

