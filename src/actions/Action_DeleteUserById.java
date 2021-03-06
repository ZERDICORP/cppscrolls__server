package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_DeleteUserById extends SQLAction
{
	int updated;

  {
    super.query(
			"DELETE FROM users "
			+ "WHERE id = ?"
		);
  }
  
  public Action_DeleteUserById(String id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

    ps.setString(1, id);

		updated = SQLManager.exec(ps);
  }
}
