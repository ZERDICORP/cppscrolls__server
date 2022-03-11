package models;



import zer.sql.SQLModel;
import org.mariadb.jdbc.MariaDbClob;



public class Model_Solution extends SQLModel
{
	public String scroll_id;
	public String author_id;
	public String author_image;

	public MariaDbClob script;

	public Integer time;
}
