package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_AddUniqueScrollVisit extends SQLAction
{
  {
    super.query("INSERT INTO unique_scroll_visits (scroll_id, user_id) VALUES (?, ?)");
  }

  public Action_AddUniqueScrollVisit(String scroll_id, String user_id)
  {
		put(scroll_id);
		put(user_id);
  }
}
