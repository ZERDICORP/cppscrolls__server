package actions;
 
 
 
import zer.sql.SQLAction;
import zer.sql.SQLActionType;



public class SQLAction_GetUserByLoginAndPasswordHash extends SQLAction
{
  {
    super.type = SQLActionType.WITH_RESULT;
    super.query = "SELECT * FROM users WHERE (email = ? OR nickname = ?) AND password_hash = ?";
  }
  
  public SQLAction_GetUserByLoginAndPasswordHash(String login, String password_hash)
  {   
    put(login);
    put(login);
    put(password_hash);
  }
}
