package actions;



import constants.Const;
 
 
 
public class Action_GetScrollsByAuthorId extends ActionTemplate_GetScroll
{
  public Action_GetScrollsByAuthorId(String user_id, String author_id, int page)
  {
		super(user_id,
			"FROM scrolls s "
				+ "LEFT JOIN users u ON u.id = s.author_id "
				+ "LEFT JOIN unique_scroll_visits usv ON usv.scroll_id = s.id "
      + "WHERE s.author_id = ? "
			+ "GROUP BY usv.scroll_id "
			+ "LIMIT ? OFFSET ?"
		);

		put(author_id);
		put(Const.SCROLLS_PAGE_SIZE);
		put(Const.SCROLLS_PAGE_SIZE * page);
  }
}
