package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_ToggleScrollBadReputation extends SQLAction
{
  {
    super.query("UPDATE scrolls SET bad_reputation = NOT bad_reputation WHERE id = ?");
  }
  
  public Action_ToggleScrollBadReputation(String id)
  {
		put(id);
  }
}
