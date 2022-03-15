package actions;
 
 

public class Action_GetUserByLoginAndPasswordHash extends ActionTemplate_GetUser
{
  public Action_GetUserByLoginAndPasswordHash(String login, String password_hash)
  {
		super("(email = ? OR nickname = ?) AND password_hash = ?");

    put(login, 2);
    put(password_hash);
  }
}
