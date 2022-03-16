package actions;



import java.sql.SQLException;
  
import zer.sql.SQLAction;
import zer.sql.SQLManager;
  
import constants.Const;
  
import models.Model_User;



public class Action_AddUser extends SQLAction
{
	int updated;

  {
    super.query(
			"INSERT "
			+ "INTO users "
				+ "(id, password_hash, nickname, email, side) "
			+ "VALUES "
				+ "(?, ?, ?, ?, ?)"
		);
  }

  public Action_AddUser(String id, String password_hash, String nickname, String email, int side) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

    ps.setString(1, id);
    ps.setString(2, password_hash);
    ps.setString(3, nickname);
    ps.setString(4, email);
    ps.setInt(5, side);

		updated = SQLManager.exec(ps);
  }
}
