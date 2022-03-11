package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetSolutionByScrollId extends SQLAction
{
  {
    super.query(
			"SELECT "
				+ "s.*,"
				+ "u.image as author_image "
			+ "FROM solutions AS s "
				+ "LEFT JOIN users u ON u.id = s.author_id "
			+ "WHERE s.scroll_id = ?"
		);
  }
 
  public Action_GetSolutionByScrollId(String scroll_id)
  {
		put(scroll_id);
  }
}
