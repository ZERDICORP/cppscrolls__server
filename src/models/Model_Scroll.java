package models;



import zer.sql.SQLModel;
import org.mariadb.jdbc.MariaDbClob;



public class Model_Scroll extends SQLModel
{
	public String id;
	public String author_id;
	public String author_image;
	public String solution_id;
	public String solution_author_image;
	public String title;

	public MariaDbClob description;
	public MariaDbClob script_func;
	public MariaDbClob test_func;

	public Integer side;
	public Integer successful_attempts;
	public Integer unsuccessful_attempts;

	public Long views;
	public Long bad_marks;

	public Integer bad_mark;
	public Integer visited;
}
