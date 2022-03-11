package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_UpdateUserScore extends SQLAction
{
  {
    super.query("UPDATE users SET score = ? WHERE id = ?");
  }

  public Action_UpdateUserScore(String id, int score)
  {
		put(score);
    put(id);
  }
}
