package actions;
 
 
 
import java.sql.SQLException;
import java.util.ArrayList;

import zer.sql.SQLAction;
import zer.sql.SQLManager;

import models.Model_Topic;



public class Action_GetTopicBySideAndName extends SQLAction
{
  {
    super.query(
			"SELECT "
				+ "* "
			+ "FROM topics "
			+ "WHERE "
				+ "side = ? AND "
				+ "name = ?"
		);
  }
 
  public Action_GetTopicBySideAndName(int side, String name) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setInt(1, side);
		ps.setString(2, name);
  }

	public ArrayList<Model_Topic> result() throws SQLException
	{
		return SQLManager.<Model_Topic>exec(Model_Topic.class, ps);
	}
}
