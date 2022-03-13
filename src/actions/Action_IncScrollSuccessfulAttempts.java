package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_IncScrollSuccessfulAttempts extends SQLAction
{
  {
    super.query("UPDATE scrolls SET successful_attempts = successful_attempts + 1 WHERE id = ?");
  }
 
  public Action_IncScrollSuccessfulAttempts(String id)
  {
		put(id);
  }
}
