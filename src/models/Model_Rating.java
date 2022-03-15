package models;



import java.math.BigDecimal;

import zer.sql.SQLModel;



public class Model_Rating extends SQLModel
{
	public String best_user_id;
	public String best_user_image;

  public BigDecimal score;
}
