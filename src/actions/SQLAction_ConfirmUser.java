package actions;
 
 
 
import zer.sql.SQLAction;
import zer.sql.SQLActionType;



public class SQLAction_ConfirmUser extends SQLAction
{
  {
    super.type = SQLActionType.WITHOUT_RESULT;
    super.query = "UPDATE users SET confirmed = true WHERE id = ?";
  }
  
  public SQLAction_ConfirmUser(String id)
  {   
    put(id);
  }
}
