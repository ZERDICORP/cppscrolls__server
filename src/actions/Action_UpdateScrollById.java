package actions;



import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateScrollById extends SQLAction
{
  {
    super.query(
			"UPDATE scrolls SET "
				+ "title = ?, "
				+ "description = ?, "
				+ "script_func = ?, "
				+ "test_func = ? "
			+ "WHERE id = ?"
		);
  }

  public Action_UpdateScrollById(String id, String title, String description,
		String script_func, String test_func) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

    ps.setString(1, title);
    ps.setString(2, description);
		ps.setString(3, script_func);
		ps.setString(4, test_func);
    ps.setString(5, id);

		SQLManager.exec(ps);
  }
}
