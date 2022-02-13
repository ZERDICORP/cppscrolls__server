package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.Status;
import constants.Field;
import constants.FieldValueType;



public class Validator_SignIn
{
  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

    requiredKeys.put(Field.LOGIN, FieldValueType.STR);
    requiredKeys.put(Field.PASSWORD, FieldValueType.STR);
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
     * checking for INVALID_LOGIN
     * --------------------------------------------
     *  Field "login" can be "email" or "nickname",
     *  so if login is neither a valid email
     *  address nor a valid nickname, then throw an
     *  error.
     */

    if (!ValidateTools.isValidNickname(jobj.getString(Field.LOGIN)) && !ValidateTools.isValidEmail(jobj.getString(Field.LOGIN)))
      return Status.INVALID_LOGIN;



    /*
     * checking for INVALID_PASSWORD
     */

    if (!ValidateTools.isValidPassword(jobj.getString(Field.PASSWORD)))
      return Status.INVALID_PASSWORD;

    return Status.OK;
  }
}
