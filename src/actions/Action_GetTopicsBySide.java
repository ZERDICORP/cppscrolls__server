package actions;
 
 
 
import java.sql.SQLException;
import java.util.ArrayList;

import zer.sql.SQLAction;
import zer.sql.SQLManager;

import constants.Const;

import models.Model_Topic;



public class Action_GetTopicsBySide extends SQLAction
{
  {
    super.query(
			"SELECT "
				+ "* "
			+ "FROM topics "
			+ "WHERE "
				+ "side = ? "
			+ "ORDER BY (requests) "
			+ "DESC "
			+ "LIMIT ? OFFSET ?"
		);
  }
  
  public Action_GetTopicsBySide(int side, int page) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setInt(1, side);
		ps.setInt(2, Const.TOPICS_PAGE_SIZE);
		ps.setInt(3, Const.TOPICS_PAGE_SIZE * page);
  }

	public ArrayList<Model_Topic> result() throws SQLException
	{
		return SQLManager.<Model_Topic>exec(Model_Topic.class, ps);
	}
}
