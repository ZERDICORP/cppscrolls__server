package actions;



import java.sql.SQLException;

import constants.Const;
 
 
 
public class Action_GetScrollsByAuthorId extends ActionTemplate_GetScroll
{
  public Action_GetScrollsByAuthorId(String user_id, String author_id, int page) throws SQLException
  {
		super(
			"FROM scrolls s "
				+ "LEFT JOIN users u ON u.id = s.author_id "
				+ "LEFT JOIN unique_scroll_visits usv ON usv.scroll_id = s.id "
      + "WHERE s.author_id = ? "
			+ "GROUP BY usv.scroll_id "
			+ "LIMIT ? OFFSET ?"
		);
		
		ps.setString(1, user_id);
		ps.setString(2, user_id);
		ps.setString(3, author_id);
		ps.setInt(4, Const.SCROLLS_PAGE_SIZE);
		ps.setInt(5, Const.SCROLLS_PAGE_SIZE * page);
  }
}
