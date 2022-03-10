package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetTopicsByScrollId extends SQLAction
{
  {
    super.query("SELECT topics.* FROM topics INNER JOIN scroll_topic ON topic_id = id WHERE scroll_id = ?");
  }

  public Action_GetTopicsByScrollId(String scroll_id)
  {
		put(scroll_id);
  }
}
