package actions;
   
  
  
import zer.sql.SQLAction;

  
public class Action_GetUserByEmail extends SQLAction
{ 
  {   
    super.query("SELECT * FROM users WHERE email = ?");
  }   
  
  public Action_GetUserByEmail(String email) 
  {   
    put(email);
  }   
}
