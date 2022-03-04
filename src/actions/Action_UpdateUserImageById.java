package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_UpdateUserImageById extends SQLAction
{
  {
    super.query("UPDATE users SET image = ? WHERE id = ?");
  }
  
  public Action_UpdateUserImageById(String id, String image)
  {
		put(image);
    put(id);
  }
}
