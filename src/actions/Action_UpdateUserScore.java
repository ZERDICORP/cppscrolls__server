package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateUserScore extends SQLAction
{
  {
    super.query(
			"UPDATE users SET "
				+ "score = ? "
			+ "WHERE id = ?"
		);
  }

  public Action_UpdateUserScore(String id, int score) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setInt(1, score);
    ps.setString(2, id);

		SQLManager.exec(ps);
  }
}
