package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateUserNickname extends SQLAction
{
	int updated;

  {
    super.query(
			"UPDATE users SET "
				+ "nickname = ? "
			+ "WHERE id = ?"
		);
  }

  public Action_UpdateUserNickname(String id, String nickname) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, nickname);
    ps.setString(2, id);

		updated = SQLManager.exec(ps);
  }
}
