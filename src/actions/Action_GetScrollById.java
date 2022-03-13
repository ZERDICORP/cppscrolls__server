package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetScrollById extends SQLAction
{
  {
    super.query(
			"SELECT "
				+ "sc.*,"
				+ "u1.id AS author_id,"
				+ "u1.image AS author_image,"
				+ "COUNT(usv.scroll_id) as views,"
				+ "COUNT(CASE WHEN usv.bad_mark = true THEN 1 END) as bad_marks,"
				+ "COUNT(CASE WHEN usv.bad_mark = true AND usv.user_id = ? THEN 1 END) > 0 as bad_mark,"
				+ "COUNT(CASE WHEN usv.user_id = ? THEN 1 END) > 0 as visited "
			+ "FROM scrolls AS sc "
				+ "LEFT JOIN users u1 ON u1.id = sc.author_id "
				+ "LEFT JOIN unique_scroll_visits usv ON usv.scroll_id = sc.id "
			+ "WHERE sc.id = ? "
			+ "GROUP BY usv.scroll_id"
		);
  }
  
  public Action_GetScrollById(String id, String user_id)
  {
		put(user_id);
		put(user_id);
    put(id);
  }
}
