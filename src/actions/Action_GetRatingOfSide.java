package actions;
 
 
 
import zer.sql.SQLAction;



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

  public Action_GetRatingOfSide(int side)
  {
    put(side);
  }
}
