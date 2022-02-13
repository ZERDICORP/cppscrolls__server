package actions;



import zer.sql.SQLAction;
import zer.sql.SQLActionType;



public class SQLAction_AddUser extends SQLAction
{
  {
    super.type = SQLActionType.WITHOUT_RESULT;
    super.query = "INSERT INTO users (id, password_hash, nickname, email, side) values (?, ?, ?, ?, ?)";
  }

  public SQLAction_AddUser(String id, String password_hash, String nickname, String email, int side)
  {
    put(id);
    put(password_hash);
    put(nickname);
    put(email);
    put(side);
  }
}
