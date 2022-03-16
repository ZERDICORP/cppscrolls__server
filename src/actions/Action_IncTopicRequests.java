package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_IncTopicRequests extends SQLAction
{
  {
    super.query(
			"UPDATE topics SET "
				+ "requests = requests + 1 "
			+ "WHERE id = ?"
		);
  }
 
  public Action_IncTopicRequests(String id) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, id);

		SQLManager.exec(ps);
  }
}
