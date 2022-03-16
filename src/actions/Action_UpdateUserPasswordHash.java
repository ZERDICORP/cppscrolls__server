package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateUserPasswordHash extends SQLAction
{
  public int updated;

	{
    super.query(
			"UPDATE users SET "
				+ "password_hash = ? "
			+ "WHERE id = ?"
		);
  }
  
  public Action_UpdateUserPasswordHash(String id, String password_hash) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());
		
		ps.setString(1, password_hash);
    ps.setString(2, id);

		updated = SQLManager.exec(ps);
  }
}
