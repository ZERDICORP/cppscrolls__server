package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateUserScoreAndScrollCreationTime extends SQLAction
{
  {
    super.query(
			"UPDATE users SET "
				+ "score = ?, "
				+ "scroll_creation_time = ? "
			+ "WHERE id = ?"
		);
  }
  
  public Action_UpdateUserScoreAndScrollCreationTime(String id, int score,
		String scroll_creation_time) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setInt(1, score);
		ps.setString(2, scroll_creation_time);
    ps.setString(3, id);

		SQLManager.exec(ps);
  }
}
