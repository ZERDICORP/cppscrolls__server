package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_DeleteScrollByScrollAndUserId extends SQLAction
{
  {
    super.query("DELETE FROM scrolls WHERE id = ? AND author_id = ? RETURNING *");
  }

	public Action_DeleteScrollByScrollAndUserId(String scroll_id, String author_id)
	{
		put(scroll_id);
		put(author_id);
	}
}
