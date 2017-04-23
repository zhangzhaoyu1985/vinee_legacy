/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package co.tagtalk.winemate.thriftfiles;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
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
import javax.annotation.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2017-02-24")
public class RewardItemResponse implements org.apache.thrift.TBase<RewardItemResponse, RewardItemResponse._Fields>, java.io.Serializable, Cloneable, Comparable<RewardItemResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("RewardItemResponse");

  private static final org.apache.thrift.protocol.TField CURRENT_POINTS_FIELD_DESC = new org.apache.thrift.protocol.TField("currentPoints", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField REWARD_ITEM_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("rewardItemList", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new RewardItemResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new RewardItemResponseTupleSchemeFactory());
  }

  public int currentPoints; // required
  public List<RewardSingleItem> rewardItemList; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CURRENT_POINTS((short)1, "currentPoints"),
    REWARD_ITEM_LIST((short)2, "rewardItemList");

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
        case 1: // CURRENT_POINTS
          return CURRENT_POINTS;
        case 2: // REWARD_ITEM_LIST
          return REWARD_ITEM_LIST;
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
  private static final int __CURRENTPOINTS_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CURRENT_POINTS, new org.apache.thrift.meta_data.FieldMetaData("currentPoints", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.REWARD_ITEM_LIST, new org.apache.thrift.meta_data.FieldMetaData("rewardItemList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, RewardSingleItem.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(RewardItemResponse.class, metaDataMap);
  }

  public RewardItemResponse() {
  }

  public RewardItemResponse(
    int currentPoints,
    List<RewardSingleItem> rewardItemList)
  {
    this();
    this.currentPoints = currentPoints;
    setCurrentPointsIsSet(true);
    this.rewardItemList = rewardItemList;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public RewardItemResponse(RewardItemResponse other) {
    __isset_bitfield = other.__isset_bitfield;
    this.currentPoints = other.currentPoints;
    if (other.isSetRewardItemList()) {
      List<RewardSingleItem> __this__rewardItemList = new ArrayList<RewardSingleItem>(other.rewardItemList.size());
      for (RewardSingleItem other_element : other.rewardItemList) {
        __this__rewardItemList.add(new RewardSingleItem(other_element));
      }
      this.rewardItemList = __this__rewardItemList;
    }
  }

  public RewardItemResponse deepCopy() {
    return new RewardItemResponse(this);
  }

  @Override
  public void clear() {
    setCurrentPointsIsSet(false);
    this.currentPoints = 0;
    this.rewardItemList = null;
  }

  public int getCurrentPoints() {
    return this.currentPoints;
  }

  public RewardItemResponse setCurrentPoints(int currentPoints) {
    this.currentPoints = currentPoints;
    setCurrentPointsIsSet(true);
    return this;
  }

  public void unsetCurrentPoints() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CURRENTPOINTS_ISSET_ID);
  }

  /** Returns true if field currentPoints is set (has been assigned a value) and false otherwise */
  public boolean isSetCurrentPoints() {
    return EncodingUtils.testBit(__isset_bitfield, __CURRENTPOINTS_ISSET_ID);
  }

  public void setCurrentPointsIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CURRENTPOINTS_ISSET_ID, value);
  }

  public int getRewardItemListSize() {
    return (this.rewardItemList == null) ? 0 : this.rewardItemList.size();
  }

  public java.util.Iterator<RewardSingleItem> getRewardItemListIterator() {
    return (this.rewardItemList == null) ? null : this.rewardItemList.iterator();
  }

  public void addToRewardItemList(RewardSingleItem elem) {
    if (this.rewardItemList == null) {
      this.rewardItemList = new ArrayList<RewardSingleItem>();
    }
    this.rewardItemList.add(elem);
  }

  public List<RewardSingleItem> getRewardItemList() {
    return this.rewardItemList;
  }

  public RewardItemResponse setRewardItemList(List<RewardSingleItem> rewardItemList) {
    this.rewardItemList = rewardItemList;
    return this;
  }

  public void unsetRewardItemList() {
    this.rewardItemList = null;
  }

  /** Returns true if field rewardItemList is set (has been assigned a value) and false otherwise */
  public boolean isSetRewardItemList() {
    return this.rewardItemList != null;
  }

  public void setRewardItemListIsSet(boolean value) {
    if (!value) {
      this.rewardItemList = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CURRENT_POINTS:
      if (value == null) {
        unsetCurrentPoints();
      } else {
        setCurrentPoints((Integer)value);
      }
      break;

    case REWARD_ITEM_LIST:
      if (value == null) {
        unsetRewardItemList();
      } else {
        setRewardItemList((List<RewardSingleItem>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CURRENT_POINTS:
      return getCurrentPoints();

    case REWARD_ITEM_LIST:
      return getRewardItemList();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CURRENT_POINTS:
      return isSetCurrentPoints();
    case REWARD_ITEM_LIST:
      return isSetRewardItemList();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof RewardItemResponse)
      return this.equals((RewardItemResponse)that);
    return false;
  }

  public boolean equals(RewardItemResponse that) {
    if (that == null)
      return false;

    boolean this_present_currentPoints = true;
    boolean that_present_currentPoints = true;
    if (this_present_currentPoints || that_present_currentPoints) {
      if (!(this_present_currentPoints && that_present_currentPoints))
        return false;
      if (this.currentPoints != that.currentPoints)
        return false;
    }

    boolean this_present_rewardItemList = true && this.isSetRewardItemList();
    boolean that_present_rewardItemList = true && that.isSetRewardItemList();
    if (this_present_rewardItemList || that_present_rewardItemList) {
      if (!(this_present_rewardItemList && that_present_rewardItemList))
        return false;
      if (!this.rewardItemList.equals(that.rewardItemList))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_currentPoints = true;
    list.add(present_currentPoints);
    if (present_currentPoints)
      list.add(currentPoints);

    boolean present_rewardItemList = true && (isSetRewardItemList());
    list.add(present_rewardItemList);
    if (present_rewardItemList)
      list.add(rewardItemList);

    return list.hashCode();
  }

  @Override
  public int compareTo(RewardItemResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetCurrentPoints()).compareTo(other.isSetCurrentPoints());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCurrentPoints()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.currentPoints, other.currentPoints);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRewardItemList()).compareTo(other.isSetRewardItemList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRewardItemList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.rewardItemList, other.rewardItemList);
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
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("RewardItemResponse(");
    boolean first = true;

    sb.append("currentPoints:");
    sb.append(this.currentPoints);
    first = false;
    if (!first) sb.append(", ");
    sb.append("rewardItemList:");
    if (this.rewardItemList == null) {
      sb.append("null");
    } else {
      sb.append(this.rewardItemList);
    }
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

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class RewardItemResponseStandardSchemeFactory implements SchemeFactory {
    public RewardItemResponseStandardScheme getScheme() {
      return new RewardItemResponseStandardScheme();
    }
  }

  private static class RewardItemResponseStandardScheme extends StandardScheme<RewardItemResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, RewardItemResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CURRENT_POINTS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.currentPoints = iprot.readI32();
              struct.setCurrentPointsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // REWARD_ITEM_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list104 = iprot.readListBegin();
                struct.rewardItemList = new ArrayList<RewardSingleItem>(_list104.size);
                RewardSingleItem _elem105;
                for (int _i106 = 0; _i106 < _list104.size; ++_i106)
                {
                  _elem105 = new RewardSingleItem();
                  _elem105.read(iprot);
                  struct.rewardItemList.add(_elem105);
                }
                iprot.readListEnd();
              }
              struct.setRewardItemListIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, RewardItemResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(CURRENT_POINTS_FIELD_DESC);
      oprot.writeI32(struct.currentPoints);
      oprot.writeFieldEnd();
      if (struct.rewardItemList != null) {
        oprot.writeFieldBegin(REWARD_ITEM_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.rewardItemList.size()));
          for (RewardSingleItem _iter107 : struct.rewardItemList)
          {
            _iter107.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class RewardItemResponseTupleSchemeFactory implements SchemeFactory {
    public RewardItemResponseTupleScheme getScheme() {
      return new RewardItemResponseTupleScheme();
    }
  }

  private static class RewardItemResponseTupleScheme extends TupleScheme<RewardItemResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, RewardItemResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetCurrentPoints()) {
        optionals.set(0);
      }
      if (struct.isSetRewardItemList()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetCurrentPoints()) {
        oprot.writeI32(struct.currentPoints);
      }
      if (struct.isSetRewardItemList()) {
        {
          oprot.writeI32(struct.rewardItemList.size());
          for (RewardSingleItem _iter108 : struct.rewardItemList)
          {
            _iter108.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, RewardItemResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.currentPoints = iprot.readI32();
        struct.setCurrentPointsIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list109 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.rewardItemList = new ArrayList<RewardSingleItem>(_list109.size);
          RewardSingleItem _elem110;
          for (int _i111 = 0; _i111 < _list109.size; ++_i111)
          {
            _elem110 = new RewardSingleItem();
            _elem110.read(iprot);
            struct.rewardItemList.add(_elem110);
          }
        }
        struct.setRewardItemListIsSet(true);
      }
    }
  }

}

