package actions;
 
 
 
import zer.sql.SQLAction;
import zer.sql.SQLActionType;



public class SQLAction_GetSides extends SQLAction
{
  {
    super.type = SQLActionType.WITH_RESULT;
    super.query = "SELECT SUM(score) as score FROM users WHERE confirmed = true GROUP BY side ASC";
  }
}
