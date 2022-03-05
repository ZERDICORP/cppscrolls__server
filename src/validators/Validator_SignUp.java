package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.CStatus;
import constants.CField;
import constants.CFieldValueType;


public class Validator_SignUp
{
  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

    requiredKeys.put(CField.NICKNAME, CFieldValueType.STR);
    requiredKeys.put(CField.EMAIL, CFieldValueType.STR);
    requiredKeys.put(CField.PASSWORD, CFieldValueType.STR);
    requiredKeys.put(CField.SIDE, CFieldValueType.INT);
  }

  public static CStatus validate(String body)
  {
    /*
     * checking for INVALID_REQUEST
     */

    if (body == null || !ValidateTools.isValidJSONObject(body))
      return CStatus.INVALID_REQUEST;

    JSONObject jobj = new JSONObject(body);

    if (!ValidateTools.isIdenticalKeys(requiredKeys.keySet(), jobj.keySet()))
      return CStatus.INVALID_REQUEST;



    /*
     * checking for INVALID_FIELD_TYPE
     */

    if (!ValidateTools.isValidFieldTypes(requiredKeys, jobj))
      return CStatus.INVALID_FIELD_TYPE;



    /*
     * checking for INVALID_NICKNAME
     */

    if (!ValidateTools.isValidNickname(jobj.getString(CField.NICKNAME)))
      return CStatus.INVALID_NICKNAME;



    /*
     * checking for INVALID_EMAIL
     */

    if (!ValidateTools.isValidEmail(jobj.getString(CField.EMAIL)))
      return CStatus.INVALID_EMAIL;



    /*
     * checking for INVALID_PASSWORD
     */

    if (!ValidateTools.isValidPassword(jobj.getString(CField.PASSWORD)))
      return CStatus.INVALID_PASSWORD;



    /*
     * checking for INVALID_SIDE
     */

    if (!ValidateTools.isValidSide(jobj.getInt(CField.SIDE)))
      return CStatus.INVALID_SIDE;
    
    return CStatus.OK;
  }
}
