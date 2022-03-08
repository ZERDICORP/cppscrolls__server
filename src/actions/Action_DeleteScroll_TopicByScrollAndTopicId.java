package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_DeleteScroll_TopicByScrollAndTopicId extends SQLAction
{
  {
    super.query("DELETE FROM scroll_topic WHERE (scroll_id = ? AND topic_id != ?)");
  }

	public Action_DeleteScroll_TopicByScrollAndTopicId(int quantity)
	{
		for (int i = 1; i < quantity; ++i)
			super.query(super.query() + " AND (scroll_id = ? AND topic_id != ?)");
	}

  public void add(String scroll_id, String topic_id)
  {
		put(scroll_id);
		put(topic_id);
  }
}
