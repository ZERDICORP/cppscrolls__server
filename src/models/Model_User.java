package models;

import zer.sql.SQLModel;

public class Model_User extends SQLModel
{
  public String id;
  public String nickname;
	public String bio;
  public String email;
  public String password_hash;
  public String image;

  public int score;
  public int side;

  public byte confirmed;
}
