package zer.sql;



import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.NoSuchMethodException;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;



public class SQLManager
{
	private static String user;
	private static String password;
	private static String jdbcDriver;
	private static String connectionString;
	private static Connection connection; 

	public static void auth(String u, String p)
	{
		user = u;
		password = p;
	}

	public static void connect(String jd, String cs)
	{
		jdbcDriver = jd;
		connectionString = cs;

		try
		{
			Class.forName(jdbcDriver);

			getConnection();
		}
		catch (SQLException | ClassNotFoundException e)
		{
			System.out.println("[sql:warn] Can't connect to sql server..");
			e.printStackTrace();
		}
	}

	public static PreparedStatement preparedStatement(String sql) throws SQLException
	{
		wakeup();

		return connection.prepareStatement(sql);
	}
	
	private static void getConnection() throws SQLException
	{
		connection = DriverManager.getConnection(connectionString, user, password);
	}

	/*
	 * Wake up the connection if it is closed.
	 */

	private static void reconnect()
	{
		System.out.println("[sql:warn] Connection stale.. reconnecting");
		
		try
		{
			getConnection();
		}
		catch (SQLException e)
		{
			System.out.println("[sql:warn] Can't reconnect to sql server..");
			e.printStackTrace();
		}
	}

	private static void wakeup()
	{
		try
		{
			connection.createStatement().executeQuery("SELECT 1");
		}
		catch (SQLException e)
		{
			reconnect();
		}
	}

	public static int exec(PreparedStatement ps) throws SQLException
  { 
		return ps.executeUpdate();
  }

	public static <TModel extends SQLModel> ArrayList<TModel> exec(Class<TModel> modelClazz, PreparedStatement ps) throws SQLException
  {
    try 
    {
      ArrayList<TModel> resultArray = new ArrayList<TModel>();
 			
			ResultSet set = ps.executeQuery();

      while (set.next())
      {   
        TModel model = modelClazz.getDeclaredConstructor().newInstance();
 
        Field[] fields = model.getClass().getFields();
        for (Field field : fields)
          field.set(model, set.getObject(field.getName()));
  
        resultArray.add(model);
      }   
  
      return resultArray;
    }   
    catch
		(
			InstantiationException | IllegalAccessException |
			NoSuchMethodException | InvocationTargetException e
		)
		{
			e.printStackTrace();
		}

		return null;
  }	
}
