package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_IncScrollUnsuccessfulAttempts extends SQLAction
{
  {
    super.query(
			"UPDATE scrolls SET "
				+ "unsuccessful_attempts = unsuccessful_attempts + 1 "
			+ "WHERE id = ?"
		);
  }
 
  public Action_IncScrollUnsuccessfulAttempts(String id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, id);

		SQLManager.exec(ps);
  }
}
