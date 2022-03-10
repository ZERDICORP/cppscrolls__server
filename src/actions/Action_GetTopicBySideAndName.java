package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetTopicBySideAndName extends SQLAction
{
  {
    super.query("SELECT * FROM topics WHERE side = ? AND name = ?");
  }
 
  public Action_GetTopicBySideAndName(int side, String name)
  {
		put(side);
		put(name);
  }
}
