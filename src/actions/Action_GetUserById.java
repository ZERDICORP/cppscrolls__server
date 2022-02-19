package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetUserById extends SQLAction
{
  {
    super.query("SELECT * FROM users WHERE id = ?");
  }
  
  public Action_GetUserById(String id)
  {   
    put(id);
  }
}
