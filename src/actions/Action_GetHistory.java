package actions;
 
 
 
import constants.Const;



public class Action_GetHistory extends ActionTemplate_GetScroll
{
	public Action_GetHistory(String user_id, int page)
  {   
    super(user_id,
      "FROM unique_scroll_visits usv "
        + "LEFT JOIN scrolls s ON s.id = usv.scroll_id "
        + "LEFT JOIN users u ON u.id = s.author_id "
      + "WHERE usv.user_id = ? "
      + "GROUP BY usv.scroll_id "
      + "LIMIT ? OFFSET ?"
    );  
 		
		put(user_id);
    put(Const.SCROLLS_PAGE_SIZE);
    put(Const.SCROLLS_PAGE_SIZE * page);
  }	
}
