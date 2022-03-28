package actions;
 
 
 
import java.sql.SQLException;

import constants.Const;



public class Action_GetHistory extends ActionTemplate_GetScroll
{
	public Action_GetHistory(String user_id, int page) throws SQLException
  {   
    super(
      "FROM unique_scroll_visits usv "
        + "LEFT JOIN scrolls s ON s.id = usv.scroll_id "
        + "LEFT JOIN users u ON u.id = s.author_id "
      + "WHERE usv.user_id = ? "
      + "GROUP BY usv.scroll_id "
      + "LIMIT ? OFFSET ?"
    );
 		
		ps.setString(1, user_id);
		ps.setString(2, user_id);
		ps.setString(3, user_id);
		ps.setString(4, user_id);
    ps.setInt(5, Const.SCROLLS_PAGE_SIZE);
    ps.setInt(6, Const.SCROLLS_PAGE_SIZE * page);
  }	
}
