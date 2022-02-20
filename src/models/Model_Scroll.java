package models;



import zer.sql.SQLModel;
import org.mariadb.jdbc.MariaDbClob;



public class Model_Scroll extends SQLModel
{
	public String id;
	public String creator_id;
	public String title;
	public MariaDbClob description;
	public MariaDbClob script_func;
	public MariaDbClob test_func;
}
