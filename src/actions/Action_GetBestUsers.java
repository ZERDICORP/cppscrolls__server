package actions;
 
 
 
import zer.sql.SQLAction;

import constants.Side;



public class Action_GetBestUsers extends SQLAction
{
  {
    super.query("(SELECT * FROM users WHERE score = (SELECT MAX(score) score FROM users WHERE side = ?) AND side = ? LIMIT 1)");
    super.query(super.query() + " UNION " + super.query());
  }

  public Action_GetBestUsers()
  {
    put(Side.DARK.ordinal(), 2);
    put(Side.BRIGHT.ordinal(), 2);
  }
}
