package models;



import zer.sql.SQLModel;
import org.mariadb.jdbc.MariaDbClob;



public class Model_Scroll extends SQLModel
{
	public String id;
	public String author_id;
	public String title;

	public MariaDbClob description;
	public MariaDbClob script_func;
	public MariaDbClob test_func;

	public int side;
	public int successful_attempts;
	public int unsuccessful_attempts;
}
