package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_DeleteUniqueScrollVisitsByScrollId extends SQLAction
{
  {
    super.query("DELETE FROM unique_scroll_visits WHERE scroll_id = ?");
  }

	public Action_DeleteUniqueScrollVisitsByScrollId(String scroll_id)
	{
		put(scroll_id);
	}
}
