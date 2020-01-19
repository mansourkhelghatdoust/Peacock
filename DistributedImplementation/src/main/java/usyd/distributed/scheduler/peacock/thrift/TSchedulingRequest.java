/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package usyd.distributed.scheduler.peacock.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-08-02")
public class TSchedulingRequest implements org.apache.thrift.TBase<TSchedulingRequest, TSchedulingRequest._Fields>, java.io.Serializable, Cloneable, Comparable<TSchedulingRequest> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TSchedulingRequest");

  private static final org.apache.thrift.protocol.TField TASKS_FIELD_DESC = new org.apache.thrift.protocol.TField("tasks", org.apache.thrift.protocol.TType.MAP, (short)1);
  private static final org.apache.thrift.protocol.TField ESTIMATED_DURATION_FIELD_DESC = new org.apache.thrift.protocol.TField("estimatedDuration", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField JOB_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("jobId", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField FRONTEND_ADDRESS_FIELD_DESC = new org.apache.thrift.protocol.TField("frontendAddress", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField MAXIMUM_WAITING_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("maximumWaitingTime", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField ENTER_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("enterTime", org.apache.thrift.protocol.TType.I64, (short)6);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TSchedulingRequestStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TSchedulingRequestTupleSchemeFactory();

  public java.util.Map<java.lang.String,java.nio.ByteBuffer> tasks; // required
  public int estimatedDuration; // required
  public java.lang.String jobId; // required
  public java.lang.String frontendAddress; // required
  public long maximumWaitingTime; // required
  public long enterTime; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TASKS((short)1, "tasks"),
    ESTIMATED_DURATION((short)2, "estimatedDuration"),
    JOB_ID((short)3, "jobId"),
    FRONTEND_ADDRESS((short)4, "frontendAddress"),
    MAXIMUM_WAITING_TIME((short)5, "maximumWaitingTime"),
    ENTER_TIME((short)6, "enterTime");

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
        case 1: // TASKS
          return TASKS;
        case 2: // ESTIMATED_DURATION
          return ESTIMATED_DURATION;
        case 3: // JOB_ID
          return JOB_ID;
        case 4: // FRONTEND_ADDRESS
          return FRONTEND_ADDRESS;
        case 5: // MAXIMUM_WAITING_TIME
          return MAXIMUM_WAITING_TIME;
        case 6: // ENTER_TIME
          return ENTER_TIME;
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
  private static final int __ESTIMATEDDURATION_ISSET_ID = 0;
  private static final int __MAXIMUMWAITINGTIME_ISSET_ID = 1;
  private static final int __ENTERTIME_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TASKS, new org.apache.thrift.meta_data.FieldMetaData("tasks", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING            , true))));
    tmpMap.put(_Fields.ESTIMATED_DURATION, new org.apache.thrift.meta_data.FieldMetaData("estimatedDuration", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.JOB_ID, new org.apache.thrift.meta_data.FieldMetaData("jobId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.FRONTEND_ADDRESS, new org.apache.thrift.meta_data.FieldMetaData("frontendAddress", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MAXIMUM_WAITING_TIME, new org.apache.thrift.meta_data.FieldMetaData("maximumWaitingTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ENTER_TIME, new org.apache.thrift.meta_data.FieldMetaData("enterTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TSchedulingRequest.class, metaDataMap);
  }

  public TSchedulingRequest() {
  }

  public TSchedulingRequest(
    java.util.Map<java.lang.String,java.nio.ByteBuffer> tasks,
    int estimatedDuration,
    java.lang.String jobId,
    java.lang.String frontendAddress,
    long maximumWaitingTime,
    long enterTime)
  {
    this();
    this.tasks = tasks;
    this.estimatedDuration = estimatedDuration;
    setEstimatedDurationIsSet(true);
    this.jobId = jobId;
    this.frontendAddress = frontendAddress;
    this.maximumWaitingTime = maximumWaitingTime;
    setMaximumWaitingTimeIsSet(true);
    this.enterTime = enterTime;
    setEnterTimeIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TSchedulingRequest(TSchedulingRequest other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetTasks()) {
      java.util.Map<java.lang.String,java.nio.ByteBuffer> __this__tasks = new java.util.HashMap<java.lang.String,java.nio.ByteBuffer>(other.tasks);
      this.tasks = __this__tasks;
    }
    this.estimatedDuration = other.estimatedDuration;
    if (other.isSetJobId()) {
      this.jobId = other.jobId;
    }
    if (other.isSetFrontendAddress()) {
      this.frontendAddress = other.frontendAddress;
    }
    this.maximumWaitingTime = other.maximumWaitingTime;
    this.enterTime = other.enterTime;
  }

  public TSchedulingRequest deepCopy() {
    return new TSchedulingRequest(this);
  }

  @Override
  public void clear() {
    this.tasks = null;
    setEstimatedDurationIsSet(false);
    this.estimatedDuration = 0;
    this.jobId = null;
    this.frontendAddress = null;
    setMaximumWaitingTimeIsSet(false);
    this.maximumWaitingTime = 0;
    setEnterTimeIsSet(false);
    this.enterTime = 0;
  }

  public int getTasksSize() {
    return (this.tasks == null) ? 0 : this.tasks.size();
  }

  public void putToTasks(java.lang.String key, java.nio.ByteBuffer val) {
    if (this.tasks == null) {
      this.tasks = new java.util.HashMap<java.lang.String,java.nio.ByteBuffer>();
    }
    this.tasks.put(key, val);
  }

  public java.util.Map<java.lang.String,java.nio.ByteBuffer> getTasks() {
    return this.tasks;
  }

  public TSchedulingRequest setTasks(java.util.Map<java.lang.String,java.nio.ByteBuffer> tasks) {
    this.tasks = tasks;
    return this;
  }

  public void unsetTasks() {
    this.tasks = null;
  }

  /** Returns true if field tasks is set (has been assigned a value) and false otherwise */
  public boolean isSetTasks() {
    return this.tasks != null;
  }

  public void setTasksIsSet(boolean value) {
    if (!value) {
      this.tasks = null;
    }
  }

  public int getEstimatedDuration() {
    return this.estimatedDuration;
  }

  public TSchedulingRequest setEstimatedDuration(int estimatedDuration) {
    this.estimatedDuration = estimatedDuration;
    setEstimatedDurationIsSet(true);
    return this;
  }

  public void unsetEstimatedDuration() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ESTIMATEDDURATION_ISSET_ID);
  }

  /** Returns true if field estimatedDuration is set (has been assigned a value) and false otherwise */
  public boolean isSetEstimatedDuration() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ESTIMATEDDURATION_ISSET_ID);
  }

  public void setEstimatedDurationIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ESTIMATEDDURATION_ISSET_ID, value);
  }

  public java.lang.String getJobId() {
    return this.jobId;
  }

  public TSchedulingRequest setJobId(java.lang.String jobId) {
    this.jobId = jobId;
    return this;
  }

  public void unsetJobId() {
    this.jobId = null;
  }

  /** Returns true if field jobId is set (has been assigned a value) and false otherwise */
  public boolean isSetJobId() {
    return this.jobId != null;
  }

  public void setJobIdIsSet(boolean value) {
    if (!value) {
      this.jobId = null;
    }
  }

  public java.lang.String getFrontendAddress() {
    return this.frontendAddress;
  }

  public TSchedulingRequest setFrontendAddress(java.lang.String frontendAddress) {
    this.frontendAddress = frontendAddress;
    return this;
  }

  public void unsetFrontendAddress() {
    this.frontendAddress = null;
  }

  /** Returns true if field frontendAddress is set (has been assigned a value) and false otherwise */
  public boolean isSetFrontendAddress() {
    return this.frontendAddress != null;
  }

  public void setFrontendAddressIsSet(boolean value) {
    if (!value) {
      this.frontendAddress = null;
    }
  }

  public long getMaximumWaitingTime() {
    return this.maximumWaitingTime;
  }

  public TSchedulingRequest setMaximumWaitingTime(long maximumWaitingTime) {
    this.maximumWaitingTime = maximumWaitingTime;
    setMaximumWaitingTimeIsSet(true);
    return this;
  }

  public void unsetMaximumWaitingTime() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __MAXIMUMWAITINGTIME_ISSET_ID);
  }

  /** Returns true if field maximumWaitingTime is set (has been assigned a value) and false otherwise */
  public boolean isSetMaximumWaitingTime() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __MAXIMUMWAITINGTIME_ISSET_ID);
  }

  public void setMaximumWaitingTimeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __MAXIMUMWAITINGTIME_ISSET_ID, value);
  }

  public long getEnterTime() {
    return this.enterTime;
  }

  public TSchedulingRequest setEnterTime(long enterTime) {
    this.enterTime = enterTime;
    setEnterTimeIsSet(true);
    return this;
  }

  public void unsetEnterTime() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ENTERTIME_ISSET_ID);
  }

  /** Returns true if field enterTime is set (has been assigned a value) and false otherwise */
  public boolean isSetEnterTime() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ENTERTIME_ISSET_ID);
  }

  public void setEnterTimeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ENTERTIME_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case TASKS:
      if (value == null) {
        unsetTasks();
      } else {
        setTasks((java.util.Map<java.lang.String,java.nio.ByteBuffer>)value);
      }
      break;

    case ESTIMATED_DURATION:
      if (value == null) {
        unsetEstimatedDuration();
      } else {
        setEstimatedDuration((java.lang.Integer)value);
      }
      break;

    case JOB_ID:
      if (value == null) {
        unsetJobId();
      } else {
        setJobId((java.lang.String)value);
      }
      break;

    case FRONTEND_ADDRESS:
      if (value == null) {
        unsetFrontendAddress();
      } else {
        setFrontendAddress((java.lang.String)value);
      }
      break;

    case MAXIMUM_WAITING_TIME:
      if (value == null) {
        unsetMaximumWaitingTime();
      } else {
        setMaximumWaitingTime((java.lang.Long)value);
      }
      break;

    case ENTER_TIME:
      if (value == null) {
        unsetEnterTime();
      } else {
        setEnterTime((java.lang.Long)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TASKS:
      return getTasks();

    case ESTIMATED_DURATION:
      return getEstimatedDuration();

    case JOB_ID:
      return getJobId();

    case FRONTEND_ADDRESS:
      return getFrontendAddress();

    case MAXIMUM_WAITING_TIME:
      return getMaximumWaitingTime();

    case ENTER_TIME:
      return getEnterTime();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case TASKS:
      return isSetTasks();
    case ESTIMATED_DURATION:
      return isSetEstimatedDuration();
    case JOB_ID:
      return isSetJobId();
    case FRONTEND_ADDRESS:
      return isSetFrontendAddress();
    case MAXIMUM_WAITING_TIME:
      return isSetMaximumWaitingTime();
    case ENTER_TIME:
      return isSetEnterTime();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof TSchedulingRequest)
      return this.equals((TSchedulingRequest)that);
    return false;
  }

  public boolean equals(TSchedulingRequest that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_tasks = true && this.isSetTasks();
    boolean that_present_tasks = true && that.isSetTasks();
    if (this_present_tasks || that_present_tasks) {
      if (!(this_present_tasks && that_present_tasks))
        return false;
      if (!this.tasks.equals(that.tasks))
        return false;
    }

    boolean this_present_estimatedDuration = true;
    boolean that_present_estimatedDuration = true;
    if (this_present_estimatedDuration || that_present_estimatedDuration) {
      if (!(this_present_estimatedDuration && that_present_estimatedDuration))
        return false;
      if (this.estimatedDuration != that.estimatedDuration)
        return false;
    }

    boolean this_present_jobId = true && this.isSetJobId();
    boolean that_present_jobId = true && that.isSetJobId();
    if (this_present_jobId || that_present_jobId) {
      if (!(this_present_jobId && that_present_jobId))
        return false;
      if (!this.jobId.equals(that.jobId))
        return false;
    }

    boolean this_present_frontendAddress = true && this.isSetFrontendAddress();
    boolean that_present_frontendAddress = true && that.isSetFrontendAddress();
    if (this_present_frontendAddress || that_present_frontendAddress) {
      if (!(this_present_frontendAddress && that_present_frontendAddress))
        return false;
      if (!this.frontendAddress.equals(that.frontendAddress))
        return false;
    }

    boolean this_present_maximumWaitingTime = true;
    boolean that_present_maximumWaitingTime = true;
    if (this_present_maximumWaitingTime || that_present_maximumWaitingTime) {
      if (!(this_present_maximumWaitingTime && that_present_maximumWaitingTime))
        return false;
      if (this.maximumWaitingTime != that.maximumWaitingTime)
        return false;
    }

    boolean this_present_enterTime = true;
    boolean that_present_enterTime = true;
    if (this_present_enterTime || that_present_enterTime) {
      if (!(this_present_enterTime && that_present_enterTime))
        return false;
      if (this.enterTime != that.enterTime)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetTasks()) ? 131071 : 524287);
    if (isSetTasks())
      hashCode = hashCode * 8191 + tasks.hashCode();

    hashCode = hashCode * 8191 + estimatedDuration;

    hashCode = hashCode * 8191 + ((isSetJobId()) ? 131071 : 524287);
    if (isSetJobId())
      hashCode = hashCode * 8191 + jobId.hashCode();

    hashCode = hashCode * 8191 + ((isSetFrontendAddress()) ? 131071 : 524287);
    if (isSetFrontendAddress())
      hashCode = hashCode * 8191 + frontendAddress.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(maximumWaitingTime);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(enterTime);

    return hashCode;
  }

  @Override
  public int compareTo(TSchedulingRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetTasks()).compareTo(other.isSetTasks());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTasks()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tasks, other.tasks);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetEstimatedDuration()).compareTo(other.isSetEstimatedDuration());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEstimatedDuration()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.estimatedDuration, other.estimatedDuration);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetJobId()).compareTo(other.isSetJobId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetJobId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.jobId, other.jobId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetFrontendAddress()).compareTo(other.isSetFrontendAddress());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFrontendAddress()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.frontendAddress, other.frontendAddress);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetMaximumWaitingTime()).compareTo(other.isSetMaximumWaitingTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMaximumWaitingTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.maximumWaitingTime, other.maximumWaitingTime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetEnterTime()).compareTo(other.isSetEnterTime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEnterTime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.enterTime, other.enterTime);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TSchedulingRequest(");
    boolean first = true;

    sb.append("tasks:");
    if (this.tasks == null) {
      sb.append("null");
    } else {
      sb.append(this.tasks);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("estimatedDuration:");
    sb.append(this.estimatedDuration);
    first = false;
    if (!first) sb.append(", ");
    sb.append("jobId:");
    if (this.jobId == null) {
      sb.append("null");
    } else {
      sb.append(this.jobId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("frontendAddress:");
    if (this.frontendAddress == null) {
      sb.append("null");
    } else {
      sb.append(this.frontendAddress);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("maximumWaitingTime:");
    sb.append(this.maximumWaitingTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("enterTime:");
    sb.append(this.enterTime);
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

  private static class TSchedulingRequestStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TSchedulingRequestStandardScheme getScheme() {
      return new TSchedulingRequestStandardScheme();
    }
  }

  private static class TSchedulingRequestStandardScheme extends org.apache.thrift.scheme.StandardScheme<TSchedulingRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TSchedulingRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TASKS
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map0 = iprot.readMapBegin();
                struct.tasks = new java.util.HashMap<java.lang.String,java.nio.ByteBuffer>(2*_map0.size);
                java.lang.String _key1;
                java.nio.ByteBuffer _val2;
                for (int _i3 = 0; _i3 < _map0.size; ++_i3)
                {
                  _key1 = iprot.readString();
                  _val2 = iprot.readBinary();
                  struct.tasks.put(_key1, _val2);
                }
                iprot.readMapEnd();
              }
              struct.setTasksIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ESTIMATED_DURATION
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.estimatedDuration = iprot.readI32();
              struct.setEstimatedDurationIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // JOB_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.jobId = iprot.readString();
              struct.setJobIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // FRONTEND_ADDRESS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.frontendAddress = iprot.readString();
              struct.setFrontendAddressIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // MAXIMUM_WAITING_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.maximumWaitingTime = iprot.readI64();
              struct.setMaximumWaitingTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // ENTER_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.enterTime = iprot.readI64();
              struct.setEnterTimeIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TSchedulingRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.tasks != null) {
        oprot.writeFieldBegin(TASKS_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, struct.tasks.size()));
          for (java.util.Map.Entry<java.lang.String, java.nio.ByteBuffer> _iter4 : struct.tasks.entrySet())
          {
            oprot.writeString(_iter4.getKey());
            oprot.writeBinary(_iter4.getValue());
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(ESTIMATED_DURATION_FIELD_DESC);
      oprot.writeI32(struct.estimatedDuration);
      oprot.writeFieldEnd();
      if (struct.jobId != null) {
        oprot.writeFieldBegin(JOB_ID_FIELD_DESC);
        oprot.writeString(struct.jobId);
        oprot.writeFieldEnd();
      }
      if (struct.frontendAddress != null) {
        oprot.writeFieldBegin(FRONTEND_ADDRESS_FIELD_DESC);
        oprot.writeString(struct.frontendAddress);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(MAXIMUM_WAITING_TIME_FIELD_DESC);
      oprot.writeI64(struct.maximumWaitingTime);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(ENTER_TIME_FIELD_DESC);
      oprot.writeI64(struct.enterTime);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TSchedulingRequestTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TSchedulingRequestTupleScheme getScheme() {
      return new TSchedulingRequestTupleScheme();
    }
  }

  private static class TSchedulingRequestTupleScheme extends org.apache.thrift.scheme.TupleScheme<TSchedulingRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TSchedulingRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetTasks()) {
        optionals.set(0);
      }
      if (struct.isSetEstimatedDuration()) {
        optionals.set(1);
      }
      if (struct.isSetJobId()) {
        optionals.set(2);
      }
      if (struct.isSetFrontendAddress()) {
        optionals.set(3);
      }
      if (struct.isSetMaximumWaitingTime()) {
        optionals.set(4);
      }
      if (struct.isSetEnterTime()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetTasks()) {
        {
          oprot.writeI32(struct.tasks.size());
          for (java.util.Map.Entry<java.lang.String, java.nio.ByteBuffer> _iter5 : struct.tasks.entrySet())
          {
            oprot.writeString(_iter5.getKey());
            oprot.writeBinary(_iter5.getValue());
          }
        }
      }
      if (struct.isSetEstimatedDuration()) {
        oprot.writeI32(struct.estimatedDuration);
      }
      if (struct.isSetJobId()) {
        oprot.writeString(struct.jobId);
      }
      if (struct.isSetFrontendAddress()) {
        oprot.writeString(struct.frontendAddress);
      }
      if (struct.isSetMaximumWaitingTime()) {
        oprot.writeI64(struct.maximumWaitingTime);
      }
      if (struct.isSetEnterTime()) {
        oprot.writeI64(struct.enterTime);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TSchedulingRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TMap _map6 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.tasks = new java.util.HashMap<java.lang.String,java.nio.ByteBuffer>(2*_map6.size);
          java.lang.String _key7;
          java.nio.ByteBuffer _val8;
          for (int _i9 = 0; _i9 < _map6.size; ++_i9)
          {
            _key7 = iprot.readString();
            _val8 = iprot.readBinary();
            struct.tasks.put(_key7, _val8);
          }
        }
        struct.setTasksIsSet(true);
      }
      if (incoming.get(1)) {
        struct.estimatedDuration = iprot.readI32();
        struct.setEstimatedDurationIsSet(true);
      }
      if (incoming.get(2)) {
        struct.jobId = iprot.readString();
        struct.setJobIdIsSet(true);
      }
      if (incoming.get(3)) {
        struct.frontendAddress = iprot.readString();
        struct.setFrontendAddressIsSet(true);
      }
      if (incoming.get(4)) {
        struct.maximumWaitingTime = iprot.readI64();
        struct.setMaximumWaitingTimeIsSet(true);
      }
      if (incoming.get(5)) {
        struct.enterTime = iprot.readI64();
        struct.setEnterTimeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
