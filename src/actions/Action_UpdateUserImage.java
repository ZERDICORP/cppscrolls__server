package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateUserImage extends SQLAction
{
	int updated;

  {
    super.query(
			"UPDATE users SET "
				+ "image = ? "
			+ "WHERE id = ?"
		);
  }
  
  public Action_UpdateUserImage(String id, String image) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, image);
    ps.setString(2, id);

		updated = SQLManager.exec(ps);
  }
}
