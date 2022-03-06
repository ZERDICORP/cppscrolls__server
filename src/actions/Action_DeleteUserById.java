package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_DeleteUserById extends SQLAction
{
  {
    super.query("DELETE FROM users WHERE id = ?");
  }
  
  public Action_DeleteUserById(String id)
  {
    put(id);
  }
}
