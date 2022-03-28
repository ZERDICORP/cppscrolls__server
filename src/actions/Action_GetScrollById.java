package actions;



import java.sql.SQLException;
 
 
 
public class Action_GetScrollById extends ActionTemplate_GetScroll
{
  public Action_GetScrollById(String user_id, String id) throws SQLException
  {
		super(
			"FROM scrolls s "
				+ "LEFT JOIN users u ON u.id = s.author_id "
				+ "LEFT JOIN unique_scroll_visits usv ON usv.scroll_id = s.id "
			+ "WHERE s.id = ? "
			+ "GROUP BY usv.scroll_id"
		);

		ps.setString(1, user_id);
    ps.setString(2, user_id);
		ps.setString(3, user_id);
		ps.setString(4, id);
  }
}
