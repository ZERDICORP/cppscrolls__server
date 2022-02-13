package zer.sql;



public abstract class SQLAction
{
	public SQLActionType type;
	public String query;

  protected void put(String value) { query = query.replaceFirst("\\?", "\"" + value + "\""); }
  protected void put(int value) { query = query.replaceFirst("\\?", String.valueOf(value)); }

  protected void put(String value, int count)
  {
    for (int i = 0; i < count; ++i)
      put(value);
  }

  protected void put(int value, int count)
  {
    for (int i = 0; i < count; ++i)
      put(value);
  }
};
