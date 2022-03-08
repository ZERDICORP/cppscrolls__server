package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_DeleteScroll_TopicByScrollId extends SQLAction
{
  {
    super.query("DELETE FROM scroll_topic WHERE scroll_id = ?");
  }

	public Action_DeleteScroll_TopicByScrollId(String scroll_id)
	{
		put(scroll_id);
	}
}
