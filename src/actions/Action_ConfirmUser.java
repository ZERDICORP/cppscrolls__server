package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_ConfirmUser extends SQLAction
{
	int updated;

  {
    super.query(
			"UPDATE users SET "
				+ "confirmed = true "
			+ "WHERE id = ?");
  }
  
  public Action_ConfirmUser(String id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

    ps.setString(1, id);

		updated = SQLManager.exec(ps);
  }
}
