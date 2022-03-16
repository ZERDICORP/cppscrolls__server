package actions;



import java.sql.SQLException;



public class Action_GetUserByNickname extends ActionTemplate_GetUser
{
  public Action_GetUserByNickname(String nickname) throws SQLException 
  {
		super("user.nickname = ?");

		ps.setString(2, nickname);
  }
}
