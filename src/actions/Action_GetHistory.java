package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetHistory extends SQLAction
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
      + "FROM unique_scroll_visits usv "
        + "LEFT JOIN scrolls sc ON sc.id = usv.scroll_id "
        + "LEFT JOIN users u1 ON u1.id = sc.author_id "
      + "WHERE usv.user_id = ? "
      + "GROUP BY usv.scroll_id "
      + "LIMIT ? OFFSET ?"
    );  
  }
  
  public Action_GetHistory(String user_id, int limit, int offset)
  {
		put(user_id, 3);
		put(limit);
		put(offset);
  }
}
