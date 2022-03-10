package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_AddScroll_Topic extends SQLAction
{
  {
    super.query("INSERT IGNORE INTO scroll_topic (scroll_id, topic_id) SELECT ?, id FROM topics WHERE side = ? AND name = ?");
  }

	public Action_AddScroll_Topic(String scroll_id, int side, int quantity)
	{
		put(scroll_id);
		put(side);

		for (int i = 1; i < quantity; ++i)
			super.query(super.query() + " OR name = ?");
	}

  public void add(String name)
  {
		put(name);
  }
}
