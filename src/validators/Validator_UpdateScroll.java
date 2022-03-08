package validators;



import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import tools.ValidateTools;

import constants.CStatus;
import constants.CField;
import constants.CFieldValueType;


public class Validator_UpdateScroll
{
  private static Map<String, String> requiredKeys;

  static
  {
    requiredKeys = new HashMap<>();

		requiredKeys.put(CField.SCROLL_ID, CFieldValueType.STR);
    requiredKeys.put(CField.TITLE, CFieldValueType.STR);
    requiredKeys.put(CField.DESCRIPTION, CFieldValueType.STR);
    requiredKeys.put(CField.SCRIPT_FUNC, CFieldValueType.STR);
    requiredKeys.put(CField.TEST_FUNC, CFieldValueType.STR);
		requiredKeys.put(CField.TOPICS, CFieldValueType.ARR_OF_STR);
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
     * checking for INVALID_ID
     */

    if (!ValidateTools.isValidId(jobj.getString(CField.SCROLL_ID)))
      return CStatus.INVALID_ID;



    /*
     * checking for INVALID_TITLE
     */

    if (!ValidateTools.isValidTitle(jobj.getString(CField.TITLE)))
      return CStatus.INVALID_TITLE;



    return CStatus.OK;
  }
}
