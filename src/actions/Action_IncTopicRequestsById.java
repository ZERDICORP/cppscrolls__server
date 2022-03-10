package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_IncTopicRequestsById extends SQLAction
{
  {
    super.query("UPDATE topics SET requests = requests + 1 WHERE id = ?");
  }
 
  public Action_IncTopicRequestsById(String id)
  {
		put(id);
  }
}
