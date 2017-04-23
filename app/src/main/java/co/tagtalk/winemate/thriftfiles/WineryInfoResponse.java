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
public class WineryInfoResponse implements org.apache.thrift.TBase<WineryInfoResponse, WineryInfoResponse._Fields>, java.io.Serializable, Cloneable, Comparable<WineryInfoResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("WineryInfoResponse");

  private static final org.apache.thrift.protocol.TField WINERY_WINE_LIST_FIELD_DESC = new org.apache.thrift.protocol.TField("wineryWineList", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField WINERY_INFO_CONTENTS_FIELD_DESC = new org.apache.thrift.protocol.TField("wineryInfoContents", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField WINERY_PHOTO_URLS_FIELD_DESC = new org.apache.thrift.protocol.TField("wineryPhotoUrls", org.apache.thrift.protocol.TType.LIST, (short)3);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new WineryInfoResponseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new WineryInfoResponseTupleSchemeFactory());
  }

  public List<WineryInfoResponseSingleItem> wineryWineList; // required
  public List<WineryInfoSingleContent> wineryInfoContents; // required
  public List<String> wineryPhotoUrls; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    WINERY_WINE_LIST((short)1, "wineryWineList"),
    WINERY_INFO_CONTENTS((short)2, "wineryInfoContents"),
    WINERY_PHOTO_URLS((short)3, "wineryPhotoUrls");

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
        case 1: // WINERY_WINE_LIST
          return WINERY_WINE_LIST;
        case 2: // WINERY_INFO_CONTENTS
          return WINERY_INFO_CONTENTS;
        case 3: // WINERY_PHOTO_URLS
          return WINERY_PHOTO_URLS;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.WINERY_WINE_LIST, new org.apache.thrift.meta_data.FieldMetaData("wineryWineList", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, WineryInfoResponseSingleItem.class))));
    tmpMap.put(_Fields.WINERY_INFO_CONTENTS, new org.apache.thrift.meta_data.FieldMetaData("wineryInfoContents", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, WineryInfoSingleContent.class))));
    tmpMap.put(_Fields.WINERY_PHOTO_URLS, new org.apache.thrift.meta_data.FieldMetaData("wineryPhotoUrls", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(WineryInfoResponse.class, metaDataMap);
  }

  public WineryInfoResponse() {
  }

  public WineryInfoResponse(
    List<WineryInfoResponseSingleItem> wineryWineList,
    List<WineryInfoSingleContent> wineryInfoContents,
    List<String> wineryPhotoUrls)
  {
    this();
    this.wineryWineList = wineryWineList;
    this.wineryInfoContents = wineryInfoContents;
    this.wineryPhotoUrls = wineryPhotoUrls;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public WineryInfoResponse(WineryInfoResponse other) {
    if (other.isSetWineryWineList()) {
      List<WineryInfoResponseSingleItem> __this__wineryWineList = new ArrayList<WineryInfoResponseSingleItem>(other.wineryWineList.size());
      for (WineryInfoResponseSingleItem other_element : other.wineryWineList) {
        __this__wineryWineList.add(new WineryInfoResponseSingleItem(other_element));
      }
      this.wineryWineList = __this__wineryWineList;
    }
    if (other.isSetWineryInfoContents()) {
      List<WineryInfoSingleContent> __this__wineryInfoContents = new ArrayList<WineryInfoSingleContent>(other.wineryInfoContents.size());
      for (WineryInfoSingleContent other_element : other.wineryInfoContents) {
        __this__wineryInfoContents.add(new WineryInfoSingleContent(other_element));
      }
      this.wineryInfoContents = __this__wineryInfoContents;
    }
    if (other.isSetWineryPhotoUrls()) {
      List<String> __this__wineryPhotoUrls = new ArrayList<String>(other.wineryPhotoUrls);
      this.wineryPhotoUrls = __this__wineryPhotoUrls;
    }
  }

  public WineryInfoResponse deepCopy() {
    return new WineryInfoResponse(this);
  }

  @Override
  public void clear() {
    this.wineryWineList = null;
    this.wineryInfoContents = null;
    this.wineryPhotoUrls = null;
  }

  public int getWineryWineListSize() {
    return (this.wineryWineList == null) ? 0 : this.wineryWineList.size();
  }

  public java.util.Iterator<WineryInfoResponseSingleItem> getWineryWineListIterator() {
    return (this.wineryWineList == null) ? null : this.wineryWineList.iterator();
  }

  public void addToWineryWineList(WineryInfoResponseSingleItem elem) {
    if (this.wineryWineList == null) {
      this.wineryWineList = new ArrayList<WineryInfoResponseSingleItem>();
    }
    this.wineryWineList.add(elem);
  }

  public List<WineryInfoResponseSingleItem> getWineryWineList() {
    return this.wineryWineList;
  }

  public WineryInfoResponse setWineryWineList(List<WineryInfoResponseSingleItem> wineryWineList) {
    this.wineryWineList = wineryWineList;
    return this;
  }

  public void unsetWineryWineList() {
    this.wineryWineList = null;
  }

  /** Returns true if field wineryWineList is set (has been assigned a value) and false otherwise */
  public boolean isSetWineryWineList() {
    return this.wineryWineList != null;
  }

  public void setWineryWineListIsSet(boolean value) {
    if (!value) {
      this.wineryWineList = null;
    }
  }

  public int getWineryInfoContentsSize() {
    return (this.wineryInfoContents == null) ? 0 : this.wineryInfoContents.size();
  }

  public java.util.Iterator<WineryInfoSingleContent> getWineryInfoContentsIterator() {
    return (this.wineryInfoContents == null) ? null : this.wineryInfoContents.iterator();
  }

  public void addToWineryInfoContents(WineryInfoSingleContent elem) {
    if (this.wineryInfoContents == null) {
      this.wineryInfoContents = new ArrayList<WineryInfoSingleContent>();
    }
    this.wineryInfoContents.add(elem);
  }

  public List<WineryInfoSingleContent> getWineryInfoContents() {
    return this.wineryInfoContents;
  }

  public WineryInfoResponse setWineryInfoContents(List<WineryInfoSingleContent> wineryInfoContents) {
    this.wineryInfoContents = wineryInfoContents;
    return this;
  }

  public void unsetWineryInfoContents() {
    this.wineryInfoContents = null;
  }

  /** Returns true if field wineryInfoContents is set (has been assigned a value) and false otherwise */
  public boolean isSetWineryInfoContents() {
    return this.wineryInfoContents != null;
  }

  public void setWineryInfoContentsIsSet(boolean value) {
    if (!value) {
      this.wineryInfoContents = null;
    }
  }

  public int getWineryPhotoUrlsSize() {
    return (this.wineryPhotoUrls == null) ? 0 : this.wineryPhotoUrls.size();
  }

  public java.util.Iterator<String> getWineryPhotoUrlsIterator() {
    return (this.wineryPhotoUrls == null) ? null : this.wineryPhotoUrls.iterator();
  }

  public void addToWineryPhotoUrls(String elem) {
    if (this.wineryPhotoUrls == null) {
      this.wineryPhotoUrls = new ArrayList<String>();
    }
    this.wineryPhotoUrls.add(elem);
  }

  public List<String> getWineryPhotoUrls() {
    return this.wineryPhotoUrls;
  }

  public WineryInfoResponse setWineryPhotoUrls(List<String> wineryPhotoUrls) {
    this.wineryPhotoUrls = wineryPhotoUrls;
    return this;
  }

  public void unsetWineryPhotoUrls() {
    this.wineryPhotoUrls = null;
  }

  /** Returns true if field wineryPhotoUrls is set (has been assigned a value) and false otherwise */
  public boolean isSetWineryPhotoUrls() {
    return this.wineryPhotoUrls != null;
  }

  public void setWineryPhotoUrlsIsSet(boolean value) {
    if (!value) {
      this.wineryPhotoUrls = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case WINERY_WINE_LIST:
      if (value == null) {
        unsetWineryWineList();
      } else {
        setWineryWineList((List<WineryInfoResponseSingleItem>)value);
      }
      break;

    case WINERY_INFO_CONTENTS:
      if (value == null) {
        unsetWineryInfoContents();
      } else {
        setWineryInfoContents((List<WineryInfoSingleContent>)value);
      }
      break;

    case WINERY_PHOTO_URLS:
      if (value == null) {
        unsetWineryPhotoUrls();
      } else {
        setWineryPhotoUrls((List<String>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case WINERY_WINE_LIST:
      return getWineryWineList();

    case WINERY_INFO_CONTENTS:
      return getWineryInfoContents();

    case WINERY_PHOTO_URLS:
      return getWineryPhotoUrls();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case WINERY_WINE_LIST:
      return isSetWineryWineList();
    case WINERY_INFO_CONTENTS:
      return isSetWineryInfoContents();
    case WINERY_PHOTO_URLS:
      return isSetWineryPhotoUrls();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof WineryInfoResponse)
      return this.equals((WineryInfoResponse)that);
    return false;
  }

  public boolean equals(WineryInfoResponse that) {
    if (that == null)
      return false;

    boolean this_present_wineryWineList = true && this.isSetWineryWineList();
    boolean that_present_wineryWineList = true && that.isSetWineryWineList();
    if (this_present_wineryWineList || that_present_wineryWineList) {
      if (!(this_present_wineryWineList && that_present_wineryWineList))
        return false;
      if (!this.wineryWineList.equals(that.wineryWineList))
        return false;
    }

    boolean this_present_wineryInfoContents = true && this.isSetWineryInfoContents();
    boolean that_present_wineryInfoContents = true && that.isSetWineryInfoContents();
    if (this_present_wineryInfoContents || that_present_wineryInfoContents) {
      if (!(this_present_wineryInfoContents && that_present_wineryInfoContents))
        return false;
      if (!this.wineryInfoContents.equals(that.wineryInfoContents))
        return false;
    }

    boolean this_present_wineryPhotoUrls = true && this.isSetWineryPhotoUrls();
    boolean that_present_wineryPhotoUrls = true && that.isSetWineryPhotoUrls();
    if (this_present_wineryPhotoUrls || that_present_wineryPhotoUrls) {
      if (!(this_present_wineryPhotoUrls && that_present_wineryPhotoUrls))
        return false;
      if (!this.wineryPhotoUrls.equals(that.wineryPhotoUrls))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_wineryWineList = true && (isSetWineryWineList());
    list.add(present_wineryWineList);
    if (present_wineryWineList)
      list.add(wineryWineList);

    boolean present_wineryInfoContents = true && (isSetWineryInfoContents());
    list.add(present_wineryInfoContents);
    if (present_wineryInfoContents)
      list.add(wineryInfoContents);

    boolean present_wineryPhotoUrls = true && (isSetWineryPhotoUrls());
    list.add(present_wineryPhotoUrls);
    if (present_wineryPhotoUrls)
      list.add(wineryPhotoUrls);

    return list.hashCode();
  }

  @Override
  public int compareTo(WineryInfoResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetWineryWineList()).compareTo(other.isSetWineryWineList());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetWineryWineList()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.wineryWineList, other.wineryWineList);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetWineryInfoContents()).compareTo(other.isSetWineryInfoContents());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetWineryInfoContents()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.wineryInfoContents, other.wineryInfoContents);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetWineryPhotoUrls()).compareTo(other.isSetWineryPhotoUrls());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetWineryPhotoUrls()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.wineryPhotoUrls, other.wineryPhotoUrls);
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
    StringBuilder sb = new StringBuilder("WineryInfoResponse(");
    boolean first = true;

    sb.append("wineryWineList:");
    if (this.wineryWineList == null) {
      sb.append("null");
    } else {
      sb.append(this.wineryWineList);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("wineryInfoContents:");
    if (this.wineryInfoContents == null) {
      sb.append("null");
    } else {
      sb.append(this.wineryInfoContents);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("wineryPhotoUrls:");
    if (this.wineryPhotoUrls == null) {
      sb.append("null");
    } else {
      sb.append(this.wineryPhotoUrls);
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class WineryInfoResponseStandardSchemeFactory implements SchemeFactory {
    public WineryInfoResponseStandardScheme getScheme() {
      return new WineryInfoResponseStandardScheme();
    }
  }

  private static class WineryInfoResponseStandardScheme extends StandardScheme<WineryInfoResponse> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, WineryInfoResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // WINERY_WINE_LIST
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list48 = iprot.readListBegin();
                struct.wineryWineList = new ArrayList<WineryInfoResponseSingleItem>(_list48.size);
                WineryInfoResponseSingleItem _elem49;
                for (int _i50 = 0; _i50 < _list48.size; ++_i50)
                {
                  _elem49 = new WineryInfoResponseSingleItem();
                  _elem49.read(iprot);
                  struct.wineryWineList.add(_elem49);
                }
                iprot.readListEnd();
              }
              struct.setWineryWineListIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // WINERY_INFO_CONTENTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list51 = iprot.readListBegin();
                struct.wineryInfoContents = new ArrayList<WineryInfoSingleContent>(_list51.size);
                WineryInfoSingleContent _elem52;
                for (int _i53 = 0; _i53 < _list51.size; ++_i53)
                {
                  _elem52 = new WineryInfoSingleContent();
                  _elem52.read(iprot);
                  struct.wineryInfoContents.add(_elem52);
                }
                iprot.readListEnd();
              }
              struct.setWineryInfoContentsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // WINERY_PHOTO_URLS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list54 = iprot.readListBegin();
                struct.wineryPhotoUrls = new ArrayList<String>(_list54.size);
                String _elem55;
                for (int _i56 = 0; _i56 < _list54.size; ++_i56)
                {
                  _elem55 = iprot.readString();
                  struct.wineryPhotoUrls.add(_elem55);
                }
                iprot.readListEnd();
              }
              struct.setWineryPhotoUrlsIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, WineryInfoResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.wineryWineList != null) {
        oprot.writeFieldBegin(WINERY_WINE_LIST_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.wineryWineList.size()));
          for (WineryInfoResponseSingleItem _iter57 : struct.wineryWineList)
          {
            _iter57.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.wineryInfoContents != null) {
        oprot.writeFieldBegin(WINERY_INFO_CONTENTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.wineryInfoContents.size()));
          for (WineryInfoSingleContent _iter58 : struct.wineryInfoContents)
          {
            _iter58.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.wineryPhotoUrls != null) {
        oprot.writeFieldBegin(WINERY_PHOTO_URLS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, struct.wineryPhotoUrls.size()));
          for (String _iter59 : struct.wineryPhotoUrls)
          {
            oprot.writeString(_iter59);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class WineryInfoResponseTupleSchemeFactory implements SchemeFactory {
    public WineryInfoResponseTupleScheme getScheme() {
      return new WineryInfoResponseTupleScheme();
    }
  }

  private static class WineryInfoResponseTupleScheme extends TupleScheme<WineryInfoResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, WineryInfoResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetWineryWineList()) {
        optionals.set(0);
      }
      if (struct.isSetWineryInfoContents()) {
        optionals.set(1);
      }
      if (struct.isSetWineryPhotoUrls()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetWineryWineList()) {
        {
          oprot.writeI32(struct.wineryWineList.size());
          for (WineryInfoResponseSingleItem _iter60 : struct.wineryWineList)
          {
            _iter60.write(oprot);
          }
        }
      }
      if (struct.isSetWineryInfoContents()) {
        {
          oprot.writeI32(struct.wineryInfoContents.size());
          for (WineryInfoSingleContent _iter61 : struct.wineryInfoContents)
          {
            _iter61.write(oprot);
          }
        }
      }
      if (struct.isSetWineryPhotoUrls()) {
        {
          oprot.writeI32(struct.wineryPhotoUrls.size());
          for (String _iter62 : struct.wineryPhotoUrls)
          {
            oprot.writeString(_iter62);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, WineryInfoResponse struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list63 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.wineryWineList = new ArrayList<WineryInfoResponseSingleItem>(_list63.size);
          WineryInfoResponseSingleItem _elem64;
          for (int _i65 = 0; _i65 < _list63.size; ++_i65)
          {
            _elem64 = new WineryInfoResponseSingleItem();
            _elem64.read(iprot);
            struct.wineryWineList.add(_elem64);
          }
        }
        struct.setWineryWineListIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list66 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.wineryInfoContents = new ArrayList<WineryInfoSingleContent>(_list66.size);
          WineryInfoSingleContent _elem67;
          for (int _i68 = 0; _i68 < _list66.size; ++_i68)
          {
            _elem67 = new WineryInfoSingleContent();
            _elem67.read(iprot);
            struct.wineryInfoContents.add(_elem67);
          }
        }
        struct.setWineryInfoContentsIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list69 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
          struct.wineryPhotoUrls = new ArrayList<String>(_list69.size);
          String _elem70;
          for (int _i71 = 0; _i71 < _list69.size; ++_i71)
          {
            _elem70 = iprot.readString();
            struct.wineryPhotoUrls.add(_elem70);
          }
        }
        struct.setWineryPhotoUrlsIsSet(true);
      }
    }
  }

}

