package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_ToggleBadMark extends SQLAction
{
  {
    super.query(
			"UPDATE unique_scroll_visits SET "
				+ "bad_mark = NOT bad_mark "
			+ "WHERE "
				+ "scroll_id = ? AND "
				+ "user_id = ?"
		);
  }
  
  public Action_ToggleBadMark(String scroll_id, String user_id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, scroll_id);
    ps.setString(2, user_id);

		SQLManager.exec(ps);
  }
}
