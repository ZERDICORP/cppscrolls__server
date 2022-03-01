package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.Status;
import constants.Field;
import constants.FieldValueType;



public class Validator_UpdatePassword
{
  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

    requiredKeys.put(Field.PASSWORD, FieldValueType.STR);
		requiredKeys.put(Field.NEW_PASSWORD, FieldValueType.STR);
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
     * checking for INVALID_PASSWORD
     */

    if (!ValidateTools.isValidPassword(jobj.getString(Field.PASSWORD)) || !ValidateTools.isValidPassword(jobj.getString(Field.NEW_PASSWORD)))
      return Status.INVALID_PASSWORD;
 
    return Status.OK;
  }
}
