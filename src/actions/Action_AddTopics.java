package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_AddTopics extends SQLAction
{
	private int paramIndex = 0;

  {
    super.query(
			"INSERT IGNORE INTO topics "
				+ "(id, name, side) "
			+ "VALUES "
				+ "(?, ?, ?)"
		);
  }

	public Action_AddTopics(int quantity) throws SQLException
	{
		for (int i = 1; i < quantity; ++i)
			super.query(super.query() + ", (?, ?, ?)");

		ps = SQLManager.preparedStatement(query());
	}

  public void add(String id, String name, int side) throws SQLException
  {
		ps.setString(paramIndex + 1, id);
		ps.setString(paramIndex + 2, name);
		ps.setInt(paramIndex + 3, side);

		paramIndex += 3;
  }

	public void exec() throws SQLException { SQLManager.exec(ps); }
}
