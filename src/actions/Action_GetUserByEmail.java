package actions;
   
  

import java.sql.SQLException;



public class Action_GetUserByEmail extends ActionTemplate_GetUser
{ 
  public Action_GetUserByEmail(String email) throws SQLException 
  {
		super("user.email = ?");

    ps.setString(2, email);
  }
}
