package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.Status;
import constants.Field;
import constants.FieldValueType;


public class Validator_SignUp
{
  /*
   * map with pair key ~ type
   */

  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

    requiredKeys.put(Field.NICKNAME, FieldValueType.STR);
    requiredKeys.put(Field.EMAIL, FieldValueType.STR);
    requiredKeys.put(Field.PASSWORD, FieldValueType.STR);
    requiredKeys.put(Field.SIDE, FieldValueType.INT);
  }

  public static Status validate(String body)
  {
    /*
     * checking for INVALID_REQUEST
     */

    if (body == null || !ValidateTools.isValidJSONObject(body))
      return Status.INVALID_REQUEST;

    JSONObject jobj = new JSONObject(body);

    if (!ValidateTools.isIdenticalKeys(requiredKeys.keySet(), jobj.keySet()))
      return Status.INVALID_REQUEST;



    /*
     * checking for INVALID_FIELD_TYPE
     */

    if (!ValidateTools.isValidFieldTypes(requiredKeys, jobj))
      return Status.INVALID_FIELD_TYPE;



    /*
     * checking for INVALID_NICKNAME
     */

    if (!ValidateTools.isValidNickname(jobj.getString(Field.NICKNAME)))
      return Status.INVALID_NICKNAME;



    /*
     * checking for INVALID_EMAIL
     */

    if (!ValidateTools.isValidEmail(jobj.getString(Field.EMAIL)))
      return Status.INVALID_EMAIL;



    /*
     * checking for INVALID_PASSWORD
     */

    if (!ValidateTools.isValidPassword(jobj.getString(Field.PASSWORD)))
      return Status.INVALID_PASSWORD;



    /*
     * checking for INVALID_SIDE
     */

    if (!ValidateTools.isValidSide(jobj.getInt(Field.SIDE)))
      return Status.INVALID_SIDE;
    
    return Status.OK;
  }
}
