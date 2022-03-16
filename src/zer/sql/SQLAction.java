package zer.sql;



import java.sql.PreparedStatement;
import java.sql.SQLException;



public abstract class SQLAction
{
	protected PreparedStatement ps;
	private String query;

	public String query() { return query; }

	protected void query(String query) { this.query = query; }
	protected void put(String sql) { query = query.replaceFirst("\\{sql}", sql); }
};
