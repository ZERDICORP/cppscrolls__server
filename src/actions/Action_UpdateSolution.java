package actions;
 
 
 
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;



public class Action_UpdateSolution extends SQLAction
{
  {
    super.query("UPDATE unique_scroll_visits SET solution = ? WHERE scroll_id = ? AND user_id = ?");
  }

  public Action_UpdateSolution(String scroll_id, String user_id, String solution) throws SQLException
  {
		ps = SQLManager.preparedStatement(query());

		ps.setString(1, solution);
		ps.setString(2, scroll_id);
    ps.setString(3, user_id);

		SQLManager.exec(ps);
  }
}
