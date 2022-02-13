package zer.sql;



import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.NoSuchMethodException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class SQLInjector extends SQLConfig
{
  public static <TModel extends SQLModel> ArrayList<TModel> inject(Class<TModel> modelClazz, SQLAction action)
	{
    try
    {
      /*
      / Wake up the connection if it is closed.
      */
      statement.execute("SELECT 1");
    }
    catch (SQLException e) { e.printStackTrace(); }

		try
		{
			if (action.type == SQLActionType.WITHOUT_RESULT)
			{
				statement.execute(action.query);
				return null;
			}

			ArrayList<TModel> resultArray = new ArrayList<TModel>();

			ResultSet set = statement.executeQuery(action.query);
			while (set.next())
			{
				TModel model = modelClazz.getDeclaredConstructor().newInstance();

				Field[] fields = model.getClass().getFields();
				for (Field field : fields)
				{
					switch (field.getType().toString())
					{
						case "int":
							field.set(model, set.getInt(field.getName()));
							break;

            case "boolean":
							field.set(model, set.getBoolean(field.getName()));
							break;

						case "class java.lang.String":
							field.set(model, set.getString(field.getName()));
							break;
					}
				}

				resultArray.add(model);
			}
			return resultArray;
		}
		catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) { e.printStackTrace(); }
		return null;
	}
};
