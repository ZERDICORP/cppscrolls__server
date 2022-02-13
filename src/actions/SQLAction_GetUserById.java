package actions;
 
 
 
import zer.sql.SQLAction;
import zer.sql.SQLActionType;



public class SQLAction_GetUserById extends SQLAction
{
  {
    super.type = SQLActionType.WITH_RESULT;
    super.query = "SELECT * FROM users WHERE id = ?";
  }
  
  public SQLAction_GetUserById(String id)
  {   
    put(id);
  }
}
