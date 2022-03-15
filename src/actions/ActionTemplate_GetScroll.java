package actions;
 
 
 
import zer.sql.SQLAction;
 
  
  
public class ActionTemplate_GetScroll extends SQLAction
{ 
  {   
    super.query(
      "SELECT "
        + "s.*,"
        + "u.id AS author_id,"
        + "u.image AS author_image,"
				+ "usv.solution,"
        + "COUNT(usv.scroll_id) as views,"
        + "COUNT(CASE WHEN usv.bad_mark = true THEN 1 END) as bad_marks,"
        + "COUNT(CASE WHEN usv.bad_mark = true AND usv.user_id = ? THEN 1 END) > 0 as bad_mark,"
        + "COUNT(CASE WHEN usv.user_id = ? THEN 1 END) > 0 as visited "
			+ "?"
    );  
	} 
  
  public ActionTemplate_GetScroll(String user_id, String sqlCode)
  {
		put(user_id, 2);
    putSQL(sqlCode);
  }   
}
