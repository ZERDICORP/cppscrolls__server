package actions;
   
  
  
import zer.sql.SQLAction;
  
  
  
public class Action_GetUserByNickname extends SQLAction
{ 
  {   
    super.query("SELECT * FROM users WHERE nickname = ?");
  }   
  
  public Action_GetUserByNickname(String nickname) 
  {   
    put(nickname);
  }   
}
