package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_UpdateUserScoreAndScrollCreationTimeById extends SQLAction
{
  {
    super.query("UPDATE users SET score = ?, scroll_creation_time = ? WHERE id = ?");
  }
  
  public Action_UpdateUserScoreAndScrollCreationTimeById(String id, int score, String scroll_creation_time)
  {
		put(score);
		put(scroll_creation_time);
    put(id);
  }
}
