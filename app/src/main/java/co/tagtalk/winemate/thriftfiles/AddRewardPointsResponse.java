/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package co.tagtalk.winemate.thriftfiles;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum AddRewardPointsResponse implements org.apache.thrift.TEnum {
  Success(1),
  AlreadyEarned(2);

  private final int value;

  private AddRewardPointsResponse(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static AddRewardPointsResponse findByValue(int value) { 
    switch (value) {
      case 1:
        return Success;
      case 2:
        return AlreadyEarned;
      default:
        return null;
    }
  }
}
