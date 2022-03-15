package actions;
 
 
 
import zer.sql.SQLAction;
  
import constants.Const;
  
  

class ActionTemplate_GetUser extends SQLAction
{ 
  {   
    super.query(
      "SELECT "
        + "user.*, "
        + "(" 
          + "SELECT "
            + "(COUNT(CASE WHEN s.bad_reputation THEN 1 END) * ?) "
          + "FROM scrolls s "
					+ "WHERE s.author_id = user.id"
        + ") as points_loss "
      + "FROM users user "
      + "WHERE ? "
      + "GROUP BY user.id"
    );  
  }   
  
  public ActionTemplate_GetUser(String condition) 
  {
    put(Const.POINTS_LOSS_FOR_BAD_SCROLL);
		putSQL(condition);
  }   
}
