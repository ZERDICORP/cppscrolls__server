package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_IncTopicRequests extends SQLAction
{
  {
    super.query("UPDATE topics SET requests = requests + 1 WHERE id = ?");
  }
 
  public Action_IncTopicRequests(String id)
  {
		put(id);
  }
}
