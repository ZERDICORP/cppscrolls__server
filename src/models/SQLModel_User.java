package models;

import zer.sql.SQLModel;

public class SQLModel_User extends SQLModel
{
  public String id;
  public String nickname;
  public String email;
  public String password_hash;
  public String image;

  public int score;
  public int side;

  public boolean confirmed;
}
