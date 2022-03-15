package models;



import zer.sql.SQLModel;
import org.mariadb.jdbc.MariaDbClob;



public class Model_Scroll extends SQLModel
{
	public String id;
	public String author_id;
	public String author_image;
	public String title;

	public MariaDbClob description;
	public MariaDbClob script_func;
	public MariaDbClob test_func;
	public MariaDbClob solution;
	
	public Integer side;
	public Integer successful_attempts;
	public Integer unsuccessful_attempts;
	public Integer bad_mark;
	public Integer visited;

	public Byte bad_reputation;

	public Long views;
	public Long bad_marks;
}
