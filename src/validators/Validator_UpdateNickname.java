package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.CStatus;
import constants.CField;
import constants.CFieldValueType;



public class Validator_UpdateNickname
{
  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

    requiredKeys.put(CField.NICKNAME, CFieldValueType.STR);
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
 
    return CStatus.OK;
  }
}
