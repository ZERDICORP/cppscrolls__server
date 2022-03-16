package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_AddScroll_Topic extends SQLAction
{
	int paramIndex = 2;

  {
    super.query(
			"INSERT IGNORE INTO scroll_topic "
				+ "(scroll_id, topic_id) "
			+ "SELECT "
				+ "?, "
				+ "id "
			+ "FROM topics "
			+ "WHERE "
				+ "side = ? AND "
				+ "name = ?"
		);
  }

	public Action_AddScroll_Topic(String scroll_id, int side, int quantity) throws SQLException
	{
		for (int i = 1; i < quantity; ++i)
			super.query(super.query() + " OR name = ?");

		ps = SQLManager.preparedStatement(query());

		ps.setString(1, scroll_id);
		ps.setInt(2, side);
	}

  public void add(String name) throws SQLException
  {
		ps.setString(paramIndex + 1, name);

		paramIndex++;
  }

	public void exec() throws SQLException { SQLManager.exec(ps); }
}
