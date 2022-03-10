package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetTopicsByPatternAndSide extends SQLAction
{
  {
    super.query("SELECT * FROM topics WHERE name LIKE ? AND side = ? ORDER BY (requests) DESC LIMIT 10");
  }
  
  public Action_GetTopicsByPatternAndSide(String pattern, int side)
  {
		put("%" + pattern + "%");
		put(side);
  }
}
