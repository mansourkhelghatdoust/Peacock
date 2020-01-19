/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package usyd.distributed.scheduler.peacock.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-08-02")
public class TFullProbe implements org.apache.thrift.TBase<TFullProbe, TFullProbe._Fields>, java.io.Serializable, Cloneable, Comparable<TFullProbe> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TFullProbe");

  private static final org.apache.thrift.protocol.TField TASK_IDS_FIELD_DESC = new org.apache.thrift.protocol.TField("taskIds", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField JOB_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("jobId", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField ESTIMATED_DURATION_FIELD_DESC = new org.apache.thrift.protocol.TField("estimatedDuration", org.apache.thrift.protocol.TType.I64, (short)3);
  private static final org.apache.thrift.protocol.TField MAXIMUM_WAITING_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("maximumWaitingTime", org.apache.thrift.protocol.TType.I64, (short)4);
  private static final org.apache.thrift.protocol.TField ENTER_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("enterTime", org.apache.thrift.protocol.TType.I64, (short)5);
  private static final org.apache.thrift.protocol.TField SCHEDULER_ADDRESS_FIELD_DESC = new org.apache.thrift.protocol.TField("schedulerAddress", org.apache.thrift.protocol.TType.STRUCT, (short)6);
  private static final org.apache.thrift.protocol.TField IMMEDIATE_EXECUTION_FIELD_DESC = new org.apache.thrift.protocol.TField("immediateExecution", org.apache.thrift.protocol.TType.BOOL, (short)7);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TFullProbeStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TFullProbeTupleSchemeFactory();

  public java.lang.String taskIds; // required
  public java.lang.String jobId; // required
  public long estimatedDuration; // required
  public long maximumWaitingTime; // required
  public long enterTime; // required
  public THostPort schedulerAddress; // required
  public boolean immediateExecution; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TASK_IDS((short)1, "taskIds"),
    JOB_ID((short)2, "jobId"),
    ESTIMATED_DURATION((short)3, "estimatedDuration"),
    MAXIMUM_WAITING_TIME((short)4, "maximumWaitingTime"),
    ENTER_TIME((short)5, "enterTime"),
    SCHEDULER_ADDRESS((short)6, "schedulerAddress"),
    IMMEDIATE_EXECUTION((short)7, "immediateExecution");

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
        case 1: // TASK_IDS
          return TASK_IDS;
        case 2: // JOB_ID
          return JOB_ID;
        case 3: // ESTIMATED_DURATION
          return ESTIMATED_DURATION;
        case 4: // MAXIMUM_WAITING_TIME
          return MAXIMUM_WAITING_TIME;
        case 5: // ENTER_TIME
          return ENTER_TIME;
        case 6: // SCHEDULER_ADDRESS
          return SCHEDULER_ADDRESS;
        case 7: // IMMEDIATE_EXECUTION
          return IMMEDIATE_EXECUTION;
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
  private static final int __IMMEDIATEEXECUTION_ISSET_ID = 3;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TASK_IDS, new org.apache.thrift.meta_data.FieldMetaData("taskIds", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.JOB_ID, new org.apache.thrift.meta_data.FieldMetaData("jobId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ESTIMATED_DURATION, new org.apache.thrift.meta_data.FieldMetaData("estimatedDuration", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.MAXIMUM_WAITING_TIME, new org.apache.thrift.meta_data.FieldMetaData("maximumWaitingTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ENTER_TIME, new org.apache.thrift.meta_data.FieldMetaData("enterTime", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.SCHEDULER_ADDRESS, new org.apache.thrift.meta_data.FieldMetaData("schedulerAddress", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRUCT        , "THostPort")));
    tmpMap.put(_Fields.IMMEDIATE_EXECUTION, new org.apache.thrift.meta_data.FieldMetaData("immediateExecution", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TFullProbe.class, metaDataMap);
  }

  public TFullProbe() {
  }

  public TFullProbe(
    java.lang.String taskIds,
    java.lang.String jobId,
    long estimatedDuration,
    long maximumWaitingTime,
    long enterTime,
    THostPort schedulerAddress,
    boolean immediateExecution)
  {
    this();
    this.taskIds = taskIds;
    this.jobId = jobId;
    this.estimatedDuration = estimatedDuration;
    setEstimatedDurationIsSet(true);
    this.maximumWaitingTime = maximumWaitingTime;
    setMaximumWaitingTimeIsSet(true);
    this.enterTime = enterTime;
    setEnterTimeIsSet(true);
    this.schedulerAddress = schedulerAddress;
    this.immediateExecution = immediateExecution;
    setImmediateExecutionIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TFullProbe(TFullProbe other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetTaskIds()) {
      this.taskIds = other.taskIds;
    }
    if (other.isSetJobId()) {
      this.jobId = other.jobId;
    }
    this.estimatedDuration = other.estimatedDuration;
    this.maximumWaitingTime = other.maximumWaitingTime;
    this.enterTime = other.enterTime;
    if (other.isSetSchedulerAddress()) {
      this.schedulerAddress = new THostPort(other.schedulerAddress);
    }
    this.immediateExecution = other.immediateExecution;
  }

  public TFullProbe deepCopy() {
    return new TFullProbe(this);
  }

  @Override
  public void clear() {
    this.taskIds = null;
    this.jobId = null;
    setEstimatedDurationIsSet(false);
    this.estimatedDuration = 0;
    setMaximumWaitingTimeIsSet(false);
    this.maximumWaitingTime = 0;
    setEnterTimeIsSet(false);
    this.enterTime = 0;
    this.schedulerAddress = null;
    setImmediateExecutionIsSet(false);
    this.immediateExecution = false;
  }

  public java.lang.String getTaskIds() {
    return this.taskIds;
  }

  public TFullProbe setTaskIds(java.lang.String taskIds) {
    this.taskIds = taskIds;
    return this;
  }

  public void unsetTaskIds() {
    this.taskIds = null;
  }

  /** Returns true if field taskIds is set (has been assigned a value) and false otherwise */
  public boolean isSetTaskIds() {
    return this.taskIds != null;
  }

  public void setTaskIdsIsSet(boolean value) {
    if (!value) {
      this.taskIds = null;
    }
  }

  public java.lang.String getJobId() {
    return this.jobId;
  }

  public TFullProbe setJobId(java.lang.String jobId) {
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

  public long getEstimatedDuration() {
    return this.estimatedDuration;
  }

  public TFullProbe setEstimatedDuration(long estimatedDuration) {
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

  public long getMaximumWaitingTime() {
    return this.maximumWaitingTime;
  }

  public TFullProbe setMaximumWaitingTime(long maximumWaitingTime) {
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

  public TFullProbe setEnterTime(long enterTime) {
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

  public THostPort getSchedulerAddress() {
    return this.schedulerAddress;
  }

  public TFullProbe setSchedulerAddress(THostPort schedulerAddress) {
    this.schedulerAddress = schedulerAddress;
    return this;
  }

  public void unsetSchedulerAddress() {
    this.schedulerAddress = null;
  }

  /** Returns true if field schedulerAddress is set (has been assigned a value) and false otherwise */
  public boolean isSetSchedulerAddress() {
    return this.schedulerAddress != null;
  }

  public void setSchedulerAddressIsSet(boolean value) {
    if (!value) {
      this.schedulerAddress = null;
    }
  }

  public boolean isImmediateExecution() {
    return this.immediateExecution;
  }

  public TFullProbe setImmediateExecution(boolean immediateExecution) {
    this.immediateExecution = immediateExecution;
    setImmediateExecutionIsSet(true);
    return this;
  }

  public void unsetImmediateExecution() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __IMMEDIATEEXECUTION_ISSET_ID);
  }

  /** Returns true if field immediateExecution is set (has been assigned a value) and false otherwise */
  public boolean isSetImmediateExecution() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __IMMEDIATEEXECUTION_ISSET_ID);
  }

  public void setImmediateExecutionIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __IMMEDIATEEXECUTION_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case TASK_IDS:
      if (value == null) {
        unsetTaskIds();
      } else {
        setTaskIds((java.lang.String)value);
      }
      break;

    case JOB_ID:
      if (value == null) {
        unsetJobId();
      } else {
        setJobId((java.lang.String)value);
      }
      break;

    case ESTIMATED_DURATION:
      if (value == null) {
        unsetEstimatedDuration();
      } else {
        setEstimatedDuration((java.lang.Long)value);
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

    case SCHEDULER_ADDRESS:
      if (value == null) {
        unsetSchedulerAddress();
      } else {
        setSchedulerAddress((THostPort)value);
      }
      break;

    case IMMEDIATE_EXECUTION:
      if (value == null) {
        unsetImmediateExecution();
      } else {
        setImmediateExecution((java.lang.Boolean)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TASK_IDS:
      return getTaskIds();

    case JOB_ID:
      return getJobId();

    case ESTIMATED_DURATION:
      return getEstimatedDuration();

    case MAXIMUM_WAITING_TIME:
      return getMaximumWaitingTime();

    case ENTER_TIME:
      return getEnterTime();

    case SCHEDULER_ADDRESS:
      return getSchedulerAddress();

    case IMMEDIATE_EXECUTION:
      return isImmediateExecution();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case TASK_IDS:
      return isSetTaskIds();
    case JOB_ID:
      return isSetJobId();
    case ESTIMATED_DURATION:
      return isSetEstimatedDuration();
    case MAXIMUM_WAITING_TIME:
      return isSetMaximumWaitingTime();
    case ENTER_TIME:
      return isSetEnterTime();
    case SCHEDULER_ADDRESS:
      return isSetSchedulerAddress();
    case IMMEDIATE_EXECUTION:
      return isSetImmediateExecution();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof TFullProbe)
      return this.equals((TFullProbe)that);
    return false;
  }

  public boolean equals(TFullProbe that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_taskIds = true && this.isSetTaskIds();
    boolean that_present_taskIds = true && that.isSetTaskIds();
    if (this_present_taskIds || that_present_taskIds) {
      if (!(this_present_taskIds && that_present_taskIds))
        return false;
      if (!this.taskIds.equals(that.taskIds))
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

    boolean this_present_estimatedDuration = true;
    boolean that_present_estimatedDuration = true;
    if (this_present_estimatedDuration || that_present_estimatedDuration) {
      if (!(this_present_estimatedDuration && that_present_estimatedDuration))
        return false;
      if (this.estimatedDuration != that.estimatedDuration)
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

    boolean this_present_schedulerAddress = true && this.isSetSchedulerAddress();
    boolean that_present_schedulerAddress = true && that.isSetSchedulerAddress();
    if (this_present_schedulerAddress || that_present_schedulerAddress) {
      if (!(this_present_schedulerAddress && that_present_schedulerAddress))
        return false;
      if (!this.schedulerAddress.equals(that.schedulerAddress))
        return false;
    }

    boolean this_present_immediateExecution = true;
    boolean that_present_immediateExecution = true;
    if (this_present_immediateExecution || that_present_immediateExecution) {
      if (!(this_present_immediateExecution && that_present_immediateExecution))
        return false;
      if (this.immediateExecution != that.immediateExecution)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetTaskIds()) ? 131071 : 524287);
    if (isSetTaskIds())
      hashCode = hashCode * 8191 + taskIds.hashCode();

    hashCode = hashCode * 8191 + ((isSetJobId()) ? 131071 : 524287);
    if (isSetJobId())
      hashCode = hashCode * 8191 + jobId.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(estimatedDuration);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(maximumWaitingTime);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(enterTime);

    hashCode = hashCode * 8191 + ((isSetSchedulerAddress()) ? 131071 : 524287);
    if (isSetSchedulerAddress())
      hashCode = hashCode * 8191 + schedulerAddress.hashCode();

    hashCode = hashCode * 8191 + ((immediateExecution) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(TFullProbe other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetTaskIds()).compareTo(other.isSetTaskIds());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTaskIds()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.taskIds, other.taskIds);
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
    lastComparison = java.lang.Boolean.valueOf(isSetSchedulerAddress()).compareTo(other.isSetSchedulerAddress());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSchedulerAddress()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.schedulerAddress, other.schedulerAddress);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetImmediateExecution()).compareTo(other.isSetImmediateExecution());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetImmediateExecution()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.immediateExecution, other.immediateExecution);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TFullProbe(");
    boolean first = true;

    sb.append("taskIds:");
    if (this.taskIds == null) {
      sb.append("null");
    } else {
      sb.append(this.taskIds);
    }
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
    sb.append("estimatedDuration:");
    sb.append(this.estimatedDuration);
    first = false;
    if (!first) sb.append(", ");
    sb.append("maximumWaitingTime:");
    sb.append(this.maximumWaitingTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("enterTime:");
    sb.append(this.enterTime);
    first = false;
    if (!first) sb.append(", ");
    sb.append("schedulerAddress:");
    if (this.schedulerAddress == null) {
      sb.append("null");
    } else {
      sb.append(this.schedulerAddress);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("immediateExecution:");
    sb.append(this.immediateExecution);
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

  private static class TFullProbeStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TFullProbeStandardScheme getScheme() {
      return new TFullProbeStandardScheme();
    }
  }

  private static class TFullProbeStandardScheme extends org.apache.thrift.scheme.StandardScheme<TFullProbe> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TFullProbe struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TASK_IDS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.taskIds = iprot.readString();
              struct.setTaskIdsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // JOB_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.jobId = iprot.readString();
              struct.setJobIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ESTIMATED_DURATION
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.estimatedDuration = iprot.readI64();
              struct.setEstimatedDurationIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // MAXIMUM_WAITING_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.maximumWaitingTime = iprot.readI64();
              struct.setMaximumWaitingTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // ENTER_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.enterTime = iprot.readI64();
              struct.setEnterTimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // SCHEDULER_ADDRESS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.schedulerAddress = new THostPort();
              struct.schedulerAddress.read(iprot);
              struct.setSchedulerAddressIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // IMMEDIATE_EXECUTION
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.immediateExecution = iprot.readBool();
              struct.setImmediateExecutionIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, TFullProbe struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.taskIds != null) {
        oprot.writeFieldBegin(TASK_IDS_FIELD_DESC);
        oprot.writeString(struct.taskIds);
        oprot.writeFieldEnd();
      }
      if (struct.jobId != null) {
        oprot.writeFieldBegin(JOB_ID_FIELD_DESC);
        oprot.writeString(struct.jobId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(ESTIMATED_DURATION_FIELD_DESC);
      oprot.writeI64(struct.estimatedDuration);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(MAXIMUM_WAITING_TIME_FIELD_DESC);
      oprot.writeI64(struct.maximumWaitingTime);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(ENTER_TIME_FIELD_DESC);
      oprot.writeI64(struct.enterTime);
      oprot.writeFieldEnd();
      if (struct.schedulerAddress != null) {
        oprot.writeFieldBegin(SCHEDULER_ADDRESS_FIELD_DESC);
        struct.schedulerAddress.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(IMMEDIATE_EXECUTION_FIELD_DESC);
      oprot.writeBool(struct.immediateExecution);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TFullProbeTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TFullProbeTupleScheme getScheme() {
      return new TFullProbeTupleScheme();
    }
  }

  private static class TFullProbeTupleScheme extends org.apache.thrift.scheme.TupleScheme<TFullProbe> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TFullProbe struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetTaskIds()) {
        optionals.set(0);
      }
      if (struct.isSetJobId()) {
        optionals.set(1);
      }
      if (struct.isSetEstimatedDuration()) {
        optionals.set(2);
      }
      if (struct.isSetMaximumWaitingTime()) {
        optionals.set(3);
      }
      if (struct.isSetEnterTime()) {
        optionals.set(4);
      }
      if (struct.isSetSchedulerAddress()) {
        optionals.set(5);
      }
      if (struct.isSetImmediateExecution()) {
        optionals.set(6);
      }
      oprot.writeBitSet(optionals, 7);
      if (struct.isSetTaskIds()) {
        oprot.writeString(struct.taskIds);
      }
      if (struct.isSetJobId()) {
        oprot.writeString(struct.jobId);
      }
      if (struct.isSetEstimatedDuration()) {
        oprot.writeI64(struct.estimatedDuration);
      }
      if (struct.isSetMaximumWaitingTime()) {
        oprot.writeI64(struct.maximumWaitingTime);
      }
      if (struct.isSetEnterTime()) {
        oprot.writeI64(struct.enterTime);
      }
      if (struct.isSetSchedulerAddress()) {
        struct.schedulerAddress.write(oprot);
      }
      if (struct.isSetImmediateExecution()) {
        oprot.writeBool(struct.immediateExecution);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TFullProbe struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(7);
      if (incoming.get(0)) {
        struct.taskIds = iprot.readString();
        struct.setTaskIdsIsSet(true);
      }
      if (incoming.get(1)) {
        struct.jobId = iprot.readString();
        struct.setJobIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.estimatedDuration = iprot.readI64();
        struct.setEstimatedDurationIsSet(true);
      }
      if (incoming.get(3)) {
        struct.maximumWaitingTime = iprot.readI64();
        struct.setMaximumWaitingTimeIsSet(true);
      }
      if (incoming.get(4)) {
        struct.enterTime = iprot.readI64();
        struct.setEnterTimeIsSet(true);
      }
      if (incoming.get(5)) {
        struct.schedulerAddress = new THostPort();
        struct.schedulerAddress.read(iprot);
        struct.setSchedulerAddressIsSet(true);
      }
      if (incoming.get(6)) {
        struct.immediateExecution = iprot.readBool();
        struct.setImmediateExecutionIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

