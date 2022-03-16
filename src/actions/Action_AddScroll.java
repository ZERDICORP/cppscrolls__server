package actions;



import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_AddScroll extends SQLAction
{
  {
    super.query(
			"INSERT INTO scrolls "
				+ "(id, author_id, title, description, script_func, test_func, side) "
			+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?)"
		);
  }

  public Action_AddScroll(String id, String author_id, String title, String description,
		String script_func, String test_func, int side) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

    ps.setString(1, id);
    ps.setString(2, author_id);
    ps.setString(3, title);
    ps.setString(4, description);
		ps.setString(5, script_func);
		ps.setString(6, test_func);
    ps.setInt(7, side);

		SQLManager.exec(ps);
  }
}
