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
				+ "t.* "
			+ "FROM topics t "
				+ "LEFT JOIN scroll_topic st ON st.topic_id = t.id "
			+ "WHERE st.scroll_id = ?"
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
