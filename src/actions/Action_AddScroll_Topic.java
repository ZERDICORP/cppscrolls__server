package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_AddScroll_Topic extends SQLAction
{
  {
    super.query("INSERT IGNORE INTO scroll_topic (scroll_id, topic_id) VALUES (?, ?)");
  }

	public Action_AddScroll_Topic(int quantity)
	{
		for (int i = 1; i < quantity; ++i)
			super.query(super.query() + ", (?, ?)");
	}

  public void add(String scroll_id, String topic_id)
  {
		put(scroll_id);
		put(topic_id);
  }
}
