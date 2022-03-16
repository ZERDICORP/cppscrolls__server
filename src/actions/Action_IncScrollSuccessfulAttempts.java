package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_IncScrollSuccessfulAttempts extends SQLAction
{
  {
    super.query(
			"UPDATE scrolls SET "
				+ "successful_attempts = successful_attempts + 1 "
			+ "WHERE id = ?"
		);
  }
 
  public Action_IncScrollSuccessfulAttempts(String id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, id);

		SQLManager.exec(ps);
  }
}
