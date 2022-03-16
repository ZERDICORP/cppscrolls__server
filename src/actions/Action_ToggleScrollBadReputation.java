package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_ToggleScrollBadReputation extends SQLAction
{
  {
    super.query(
			"UPDATE scrolls SET "
				+ "bad_reputation = NOT bad_reputation "
			+ "WHERE id = ?"
		);
  }
  
  public Action_ToggleScrollBadReputation(String id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, id);

		SQLManager.exec(ps);
  }
}
