package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetUserByLoginAndPasswordHash extends SQLAction
{
  {
    super.query("SELECT * FROM users WHERE (email = ? OR nickname = ?) AND password_hash = ?");
  }
  
  public Action_GetUserByLoginAndPasswordHash(String login, String password_hash)
  {   
    put(login);
    put(login);
    put(password_hash);
  }
}
