package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetSides extends SQLAction
{
  {
    super.query("SELECT SUM(score) as score FROM users WHERE confirmed = true GROUP BY side ASC");
  }
}
