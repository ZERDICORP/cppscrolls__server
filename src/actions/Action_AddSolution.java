package actions;



import zer.sql.SQLAction;



public class Action_AddSolution extends SQLAction
{
  {
    super.query(
			"INSERT INTO solutions "
				+ "(scroll_id, author_id, script, time) "
			+ "VALUES "
				+ "(?, ?, ?, ?)"
		);
  }

  public Action_AddSolution(String scroll_id, String author_id, String script, int time)
  {
    put(scroll_id);
    put(author_id);
    put(script);
    put(time);
  }
}
