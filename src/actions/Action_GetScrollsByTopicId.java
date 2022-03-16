package actions;



import java.sql.SQLException;

import constants.Const;
 
 
 
public class Action_GetScrollsByTopicId extends ActionTemplate_GetScroll
{
	public Action_GetScrollsByTopicId(String user_id, String topic_id, int page) throws SQLException
  {   
    super(
      "FROM scroll_topic st "
				+ "LEFT JOIN scrolls s ON s.id = st.scroll_id "
				+ "LEFT JOIN users u ON u.id = s.author_id "
				+ "LEFT JOIN unique_scroll_visits usv ON usv.scroll_id = s.id "
      + "WHERE st.topic_id = ? "
			+ "GROUP BY usv.scroll_id "
			+ "ORDER BY "
				+ "(case when s.bad_reputation then 1 else 0 end), "
				+ "(s.successful_attempts + s.unsuccessful_attempts) desc "
			+ "LIMIT ? OFFSET ?"
    );
 	
		ps.setString(1, user_id);
		ps.setString(2, user_id);
    ps.setString(3, topic_id);
    ps.setInt(4, Const.SCROLLS_PAGE_SIZE);
    ps.setInt(5, Const.SCROLLS_PAGE_SIZE * page);
  }	
}
