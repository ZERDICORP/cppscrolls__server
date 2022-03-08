package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetScrollById extends SQLAction
{
  {
    super.query("SELECT * FROM scrolls WHERE id = ?");
  }
  
  public Action_GetScrollById(String id)
  {   
    put(id);
  }
}
