package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_UpdateUserPasswordHashById extends SQLAction
{
  {
    super.query("UPDATE users SET password_hash = ? WHERE id = ?");
  }
  
  public Action_UpdateUserPasswordHashById(String id, String password_hash)
  {
		put(password_hash);
    put(id);
  }
}
