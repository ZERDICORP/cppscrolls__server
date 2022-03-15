package actions;



public class Action_GetRandomScroll extends ActionTemplate_GetScroll
{
	public Action_GetRandomScroll(String user_id, int side) 
  {
    super(user_id,
      "FROM scrolls s "
        + "LEFT JOIN users u ON u.id = s.author_id "
        + "LEFT JOIN unique_scroll_visits usv ON usv.scroll_id = s.id "
      + "WHERE "
				+ "s.bad_reputation = false AND "
				+ "s.side = ? "
      + "GROUP BY usv.scroll_id "
			+ "ORDER BY RAND() "
			+ "LIMIT 1"
    );  
  
    put(side);
  }	
}
