package actions;



import java.sql.SQLException;
 
 

public class Action_GetUserByLoginAndPasswordHash extends ActionTemplate_GetUser
{
  public Action_GetUserByLoginAndPasswordHash(String login, String password_hash) throws SQLException
  {
		super("(email = ? OR nickname = ?) AND password_hash = ?");

    ps.setString(2, login);
		ps.setString(3, login);
    ps.setString(4, password_hash);
  }
}
