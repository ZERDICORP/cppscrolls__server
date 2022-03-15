package zer.sql;



public abstract class SQLAction
{
	private String query;
	protected void query(String query) { this.query = query; }
	public String query() { return query; }
	
	protected void putSQL(String sqlCode) { query = query.replaceFirst("\\?", sqlCode); }
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
