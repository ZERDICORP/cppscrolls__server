package actions;
   
  
  
import zer.sql.SQLAction;
import zer.sql.SQLActionType;
  
  
  
public class SQLAction_GetUserByNickname extends SQLAction
{ 
  {   
    super.type = SQLActionType.WITH_RESULT;
    super.query = "SELECT * FROM users WHERE nickname = ?";
  }   
  
  public SQLAction_GetUserByNickname(String nickname) 
  {   
    put(nickname);
  }   
}
