package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_DeleteScrollByScrollAndUserId extends SQLAction
{
	public int updated;

  {
    super.query(
			"DELETE FROM scrolls "
			+ "WHERE "
				+ "id = ? AND "
				+ "author_id = ?"
		);
  }

	public Action_DeleteScrollByScrollAndUserId(String scroll_id, String author_id) throws SQLException
	{
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, scroll_id);
		ps.setString(2, author_id);

		updated = SQLManager.exec(ps);
	}
}
