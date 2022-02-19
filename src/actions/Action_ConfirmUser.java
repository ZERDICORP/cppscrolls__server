package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_ConfirmUser extends SQLAction
{
  {
    super.query("UPDATE users SET confirmed = true WHERE id = ?");
  }
  
  public Action_ConfirmUser(String id)
  {   
    put(id);
  }
}
