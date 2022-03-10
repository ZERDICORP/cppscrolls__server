package tools;



import java.util.Set;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import constants.CSide;
import constants.CFieldValueType;
import constants.CRegex;



public class ValidateTools
{
  public static boolean isValidSide(int test) { return String.valueOf(test).matches(CRegex.SIDE); }
  public static boolean isValidPassword(String test) { return test.matches(CRegex.PASSWORD); }
  public static boolean isValidEmail(String test) { return test.matches(CRegex.EMAIL); }
  public static boolean isValidNickname(String test) { return test.matches(CRegex.NICKNAME); }
 	public static boolean isValidTitle(String test) { return test.matches(CRegex.TITLE); }
	public static boolean isValidId(String test) { return test.matches("^" + CRegex.UUID + "$"); }

	public static boolean isValidTopics(JSONArray topics)
	{
		for (int i = 0; i < topics.length(); ++i)
			if (!topics.getString(i).matches("^" + CRegex.TOPIC + "$"))
				return false;
		return true;
	}

  public static boolean isValidFieldTypes(Map<String, String> requiredKeys, JSONObject jobj)
  {
    try
    {
      for (Map.Entry<String, String> entry : requiredKeys.entrySet())
        switch (entry.getValue())
        {
          case CFieldValueType.STR: jobj.getString(entry.getKey()); break;
          case CFieldValueType.INT: jobj.getInt(entry.getKey()); break;
					case CFieldValueType.ARR_OF_STR:
					{
						JSONArray jarr = jobj.getJSONArray(entry.getKey());
						for (int i = 0; i < jarr.length(); ++i)
							jarr.getString(i);
						
						break;
					}
        } 
    }
    catch (JSONException e) { return false; }
    return true;
  }

  public static boolean isIdenticalKeys(Set<String> requiredKeys, Set<String> keys)
  {
    keys.retainAll(requiredKeys);
    return keys.size() == requiredKeys.size();
  }

  public static boolean isValidJSONObject(String test)
  {
    try { new JSONObject(test); }
    catch (JSONException e) { return false; }
    return true;
  }

  public static boolean isValidJSONArray(String test)
  {
    try { new JSONArray(test); }
    catch (JSONException e) { return false; }
    return true;
  }
}
