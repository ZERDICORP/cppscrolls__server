package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetScrollsByAuthorId extends SQLAction
{
  {
		super.query(
      "SELECT "
        + "sc.*,"
        + "u1.id AS author_id,"
        + "u1.image AS author_image,"
        + "so.id AS solution_id,"
        + "u2.image AS solution_author_image,"
        + "COUNT(usv.scroll_id) as views,"
        + "COUNT(CASE WHEN usv.bad_mark = true THEN 1 END) as bad_marks,"
        + "COUNT(CASE WHEN usv.bad_mark = true AND usv.user_id = ? THEN 1 END) > 0 as bad_mark,"
        + "COUNT(CASE WHEN usv.user_id = ? THEN 1 END) > 0 as visited "
			+ "FROM scrolls sc "
				+ "LEFT JOIN users u1 ON u1.id = sc.author_id "
				+ "LEFT JOIN solutions so ON so.scroll_id = sc.id "
				+ "LEFT JOIN users u2 ON so.author_id = u2.id "
				+ "LEFT JOIN unique_scroll_visits usv ON usv.scroll_id = sc.id "
      + "WHERE sc.author_id = ? "
			+ "GROUP BY usv.scroll_id "
			+ "LIMIT ? OFFSET ?"
    );
  }
  
  public Action_GetScrollsByAuthorId(String author_id, String user_id, int limit, int offset)
  {
		put(user_id, 2);
		put(author_id);
		put(limit);
		put(offset);
  }
}
