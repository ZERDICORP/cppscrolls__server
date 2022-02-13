package actions;
 
 
 
import zer.sql.SQLAction;
import zer.sql.SQLActionType;

import constants.Side;



public class SQLAction_GetBestUsers extends SQLAction
{
  {
    super.type = SQLActionType.WITH_RESULT;
    super.query = "(SELECT * FROM users WHERE score = (SELECT MAX(score) score FROM users WHERE side = ?) AND side = ? LIMIT 1)";
    super.query = super.query + " UNION " + super.query;
  }

  public SQLAction_GetBestUsers()
  {
    put(Side.DARK.ordinal(), 2);
    put(Side.BRIGHT.ordinal(), 2);
  }
}
