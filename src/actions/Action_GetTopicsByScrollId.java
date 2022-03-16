package actions;
 
 
 
import java.util.ArrayList;
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;

import models.Model_Topic;



public class Action_GetTopicsByScrollId extends SQLAction
{
  {
    super.query(
			"SELECT "
				+ "topics.* "
			+ "FROM topics "
				+ "INNER JOIN scroll_topic ON topic_id = id "
			+ "WHERE scroll_id = ?"
		);
  }

  public Action_GetTopicsByScrollId(String scroll_id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, scroll_id);
  }

	public ArrayList<Model_Topic> result() throws SQLException
	{
		return SQLManager.<Model_Topic>exec(Model_Topic.class, ps);
	}
}
