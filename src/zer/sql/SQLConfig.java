package zer.sql;

import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;



public class SQLConfig
{
  protected static Statement statement;
	protected static String password = new String();
	protected static String dbName = new String();
	protected static String host = "localhost";
	protected static String port = "3306";
	protected static String user = "root";

	public static void setPassword(String p) { password = p; }
	public static void setDatabase(String d) { dbName = d; }
  public static void connect()
  {
    try
		{
			Class.forName("org.mariadb.jdbc.Driver");
			Connection connection = DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + dbName + "?autoReconnect=true", user, password);
			statement = connection.createStatement();
		}
		catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
  }
}
