package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_IncScrollUnsuccessfulAttempts extends SQLAction
{
  {
    super.query("UPDATE scrolls SET unsuccessful_attempts = unsuccessful_attempts + 1 WHERE id = ?");
  }
 
  public Action_IncScrollUnsuccessfulAttempts(String id)
  {
		put(id);
  }
}
