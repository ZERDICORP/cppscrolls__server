package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.Status;
import constants.Field;
import constants.FieldValueType;



public class Validator_UpdateNickname
{
  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

    requiredKeys.put(Field.NICKNAME, FieldValueType.STR);
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
 
    return Status.OK;
  }
}
