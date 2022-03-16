package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_AddUniqueScrollVisit extends SQLAction
{
  {
    super.query(
			"INSERT INTO unique_scroll_visits "
				+ "(scroll_id, user_id) "
			+ "VALUES "
				+ "(?, ?)"
		);
  }

  public Action_AddUniqueScrollVisit(String scroll_id, String user_id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, scroll_id);
		ps.setString(2, user_id);

		SQLManager.exec(ps);
  }
}
