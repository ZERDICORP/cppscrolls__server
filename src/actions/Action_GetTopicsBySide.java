package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetTopicsBySide extends SQLAction
{
  {
    super.query("SELECT * FROM topics WHERE side = ? ORDER BY (requests) DESC LIMIT ? OFFSET ?");
  }
  
  public Action_GetTopicsBySide(int side, int limit, int offset)
  {
		put(side);
		put(limit);
		put(offset);
  }
}
