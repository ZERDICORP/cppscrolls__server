package actions;



import java.sql.SQLException;


 
public class Action_GetUserById extends ActionTemplate_GetUser
{
  public Action_GetUserById(String id) throws SQLException
  {
		super("user.id = ?");

		ps.setString(2, id);
  }
}
