package actions;
 
 
 
import java.sql.SQLException;
import java.util.ArrayList;

import zer.sql.SQLAction;
import zer.sql.SQLManager;

import models.Model_Topic;



public class Action_GetTopicsByPatternAndSide extends SQLAction
{
  {
    super.query(
			"SELECT "
				+ "* "
			+ "FROM topics "
			+ "WHERE "
				+ "name LIKE ? AND "
				+ "side = ? "
			+ "ORDER BY (requests) "
			+ "DESC "
			+ "LIMIT 10"
		);
  }
  
  public Action_GetTopicsByPatternAndSide(String pattern, int side) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, "%" + pattern + "%");
		ps.setInt(2, side);
  }

	public ArrayList<Model_Topic> result() throws SQLException
	{
		return SQLManager.<Model_Topic>exec(Model_Topic.class, ps);
	}
}
