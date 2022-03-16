package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateUserBio extends SQLAction
{
	int updated;

  {
    super.query(
			"UPDATE users SET "
				+ "bio = NULLIF(?, '') "
			+ "WHERE id = ?"
		);
  }
  
  public Action_UpdateUserBio(String id, String bio) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());
	
		ps.setString(1, bio);
    ps.setString(2, id);

		updated = SQLManager.exec(ps);
  }
}
