package actions;
 
 
 
import java.util.ArrayList;
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;

import constants.Const;

import models.Model_User;



class ActionTemplate_GetUser extends SQLAction
{
  {
    super.query(
      "SELECT "
        + "user.*, "
        + "(" 
          + "SELECT "
            + "(COUNT(CASE WHEN s.bad_reputation THEN 1 END) * ?) "
          + "FROM scrolls s "
					+ "WHERE s.author_id = user.id"
        + ") as points_loss "
      + "FROM users user "
      + "WHERE {sql} "
      + "GROUP BY user.id"
    );  
  }

  public ActionTemplate_GetUser(String sql) throws SQLException 
  {
		put(sql);

		ps = SQLManager.preparedStatement(query());

		ps.setInt(1, Const.POINTS_LOSS_FOR_BAD_SCROLL);
  }

	public ArrayList<Model_User> result() throws SQLException
	{
		return SQLManager.<Model_User>exec(Model_User.class, ps);
	}
}
