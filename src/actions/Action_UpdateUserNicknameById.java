package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_UpdateUserNicknameById extends SQLAction
{
  {
    super.query("UPDATE users SET nickname = ? WHERE id = ?");
  }
  
  public Action_UpdateUserNicknameById(String id, String nickname)
  {
		put(nickname);
    put(id);
  }
}
