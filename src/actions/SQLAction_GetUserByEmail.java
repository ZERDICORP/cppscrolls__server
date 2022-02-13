package actions;
   
  
  
import zer.sql.SQLAction;
import zer.sql.SQLActionType;
  
  
  
public class SQLAction_GetUserByEmail extends SQLAction
{ 
  {   
    super.type = SQLActionType.WITH_RESULT;
    super.query = "SELECT * FROM users WHERE email = ?";
  }   
  
  public SQLAction_GetUserByEmail(String email) 
  {   
    put(email);
  }   
}
