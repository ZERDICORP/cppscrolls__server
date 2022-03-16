package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_DeleteScroll_TopicByScrollId extends SQLAction
{
  {
    super.query(
			"DELETE FROM scroll_topic "
			+ "WHERE scroll_id = ?"
		);
  }

	public Action_DeleteScroll_TopicByScrollId(String scroll_id) throws SQLException
	{
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, scroll_id);

		SQLManager.exec(ps);
	}
}
