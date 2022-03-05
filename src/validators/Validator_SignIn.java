package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.CStatus;
import constants.CField;
import constants.CFieldValueType;



public class Validator_SignIn
{
  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

    requiredKeys.put(CField.LOGIN, CFieldValueType.STR);
    requiredKeys.put(CField.PASSWORD, CFieldValueType.STR);
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
     * checking for INVALID_LOGIN
     *\
     * Field "login" can be "email" or "nickname",
     * so if login is neither a valid email
     * address nor a valid nickname, then throw an
     * error.
     */

    if (!ValidateTools.isValidNickname(jobj.getString(CField.LOGIN)) && !ValidateTools.isValidEmail(jobj.getString(CField.LOGIN)))
      return CStatus.INVALID_LOGIN;



    /*
     * checking for INVALID_PASSWORD
     */

    if (!ValidateTools.isValidPassword(jobj.getString(CField.PASSWORD)))
      return CStatus.INVALID_PASSWORD;

    return CStatus.OK;
  }
}
