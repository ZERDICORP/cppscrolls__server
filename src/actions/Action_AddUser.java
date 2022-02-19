package actions;



import zer.sql.SQLAction;



public class Action_AddUser extends SQLAction
{
  {
    super.query("INSERT INTO users (id, password_hash, nickname, email, side) values (?, ?, ?, ?, ?)");
  }

  public Action_AddUser(String id, String password_hash, String nickname, String email, int side)
  {
    put(id);
    put(password_hash);
    put(nickname);
    put(email);
    put(side);
  }
}
