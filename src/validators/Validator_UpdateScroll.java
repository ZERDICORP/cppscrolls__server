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
    if (body == null || !ValidateTools.isValidJSONObject(body))
      return CStatus.INVALID_REQUEST;

    JSONObject jobj = new JSONObject(body);

    if (!ValidateTools.isIdenticalKeys(requiredKeys.keySet(), jobj.keySet()))
      return CStatus.INVALID_REQUEST;

		if (!ValidateTools.isValidFieldTypes(requiredKeys, jobj))
      return CStatus.INVALID_FIELD_TYPE;

    if (!ValidateTools.isValidId(jobj.getString(CField.SCROLL_ID)))
      return CStatus.INVALID_ID;

    if (!ValidateTools.isValidTitle(jobj.getString(CField.TITLE)))
      return CStatus.INVALID_TITLE;

    if (!ValidateTools.isValidTopics(jobj.getJSONArray(CField.TOPICS)))
      return CStatus.INVALID_TOPICS;

		return CStatus.OK;
  }
}
