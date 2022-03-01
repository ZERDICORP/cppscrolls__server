package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_UpdateUserBioById extends SQLAction
{
  {
    super.query("UPDATE users SET bio = ? WHERE id = ?");
  }
  
  public Action_UpdateUserBioById(String id, String bio)
  {
		put(bio);
    put(id);
  }
}
