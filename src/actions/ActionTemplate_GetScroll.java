package actions;
 
 
 
import java.util.ArrayList;
import java.sql.SQLException;

import zer.sql.SQLAction;
import zer.sql.SQLManager;

import models.Model_Scroll;
 
  
  
public class ActionTemplate_GetScroll extends SQLAction
{ 
  {   
    super.query(
      "SELECT "
        + "s.*,"
        + "u.id AS author_id,"
        + "u.image AS author_image,"
				+ "(SELECT solution FROM unique_scroll_visits WHERE scroll_id = s.id AND user_id = ?) as solution,"
        + "COUNT(usv.scroll_id) as views,"
        + "COUNT(CASE WHEN usv.bad_mark = true THEN 1 END) as bad_marks,"
        + "COUNT(CASE WHEN usv.bad_mark = true AND usv.user_id = ? THEN 1 END) > 0 as bad_mark,"
        + "COUNT(CASE WHEN usv.user_id = ? THEN 1 END) > 0 as visited "
			+ "{sql}"
    );  
	} 
  
  public ActionTemplate_GetScroll(String sql) throws SQLException
  {
		put(sql);	

		ps = SQLManager.preparedStatement(query());
  }

	public ArrayList<Model_Scroll> result() throws SQLException
	{
		return SQLManager.<Model_Scroll>exec(Model_Scroll.class, ps);
	}	
}
