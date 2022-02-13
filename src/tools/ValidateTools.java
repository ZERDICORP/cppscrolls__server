package tools;



import java.util.Set;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import constants.Database;
import constants.Side;
import constants.FieldValueType;



public class ValidateTools
{
  public static boolean isValidSide(int test) { return test == Side.DARK.ordinal() || test == Side.BRIGHT.ordinal(); }
  public static boolean isValidPassword(String test) { return test.length() >= Database.MIN_PASSWORD_LENGTH; }
  public static boolean isValidEmail(String test) { return test.length() > 0 && test.length() <= Database.MAX_EMAIL_LENGTH && test.matches("^[.a-zA-Z0-9_-]*@[a.-zA-Z0-9_-]*\\.[a.-zA-Z0-9_-]*$"); }
  public static boolean isValidNickname(String test) { return test.length() > 0 && test.length() <= Database.MAX_NICKNAME_LENGTH && test.matches("^[a-zA-Z0-9_]*$"); }
  
  public static boolean isValidFieldTypes(Map<String, String> requiredKeys, JSONObject jobj)
  {
    try
    {
      for (Map.Entry<String, String> entry : requiredKeys.entrySet())
        switch (entry.getValue())
        {
          case FieldValueType.STR: jobj.getString(entry.getKey()); break;
          case FieldValueType.INT: jobj.getInt(entry.getKey()); break;
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
