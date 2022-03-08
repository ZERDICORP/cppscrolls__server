package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_DeleteSolutionByScrollId extends SQLAction
{
  {
    super.query("DELETE FROM solutions WHERE scroll_id = ?");
  }

	public Action_DeleteSolutionByScrollId(String scroll_id)
	{
		put(scroll_id);
	}
}
