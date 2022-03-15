package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_UpdateSolution extends SQLAction
{
  {
    super.query("UPDATE unique_scroll_visits SET solution = ? WHERE scroll_id = ? AND user_id = ?");
  }

  public Action_UpdateSolution(String scroll_id, String user_id, String solution)
  {
		put(solution);
		put(scroll_id);
    put(user_id);
  }
}
