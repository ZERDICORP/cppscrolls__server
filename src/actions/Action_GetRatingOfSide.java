package actions;
 
 
 
import java.sql.SQLException;
import java.util.ArrayList;

import zer.sql.SQLAction;
import zer.sql.SQLManager;

import models.Model_Rating;



public class Action_GetRatingOfSide extends SQLAction
{
  {
    super.query(
      "SELECT "
        + "(SELECT SUM(score) FROM users u WHERE u.side = user.side) AS score,"
        + "user.id AS best_user_id,"
        + "user.image AS best_user_image "
      + "FROM users user "
      + "WHERE "
				+ "user.side = ? AND "
				+ "user.score != 0 AND "
        + "user.score = (SELECT MAX(score) FROM users u WHERE u.side = user.side) "
      + "LIMIT 1"
    );
  }

  public Action_GetRatingOfSide(int side) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

    ps.setInt(1, side);
  }

	public ArrayList<Model_Rating> result() throws SQLException
	{
		return SQLManager.<Model_Rating>exec(Model_Rating.class, ps);
	}
}
