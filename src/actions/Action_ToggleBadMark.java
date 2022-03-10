package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_ToggleBadMark extends SQLAction
{
  {
    super.query("UPDATE unique_scroll_visits SET bad_mark = NOT bad_mark WHERE scroll_id = ? AND user_id = ?");
  }
  
  public Action_ToggleBadMark(String scroll_id, String user_id)
  {
		put(scroll_id);
    put(user_id);
  }
}
