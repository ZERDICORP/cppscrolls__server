package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_DeleteUniqueScrollVisitsByScrollId extends SQLAction
{
  {
    super.query(
			"DELETE FROM unique_scroll_visits "
			+ "WHERE scroll_id = ?"
		);
  }

	public Action_DeleteUniqueScrollVisitsByScrollId(String scroll_id) throws SQLException
	{
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, scroll_id);

		SQLManager.exec(ps);
	}
}
