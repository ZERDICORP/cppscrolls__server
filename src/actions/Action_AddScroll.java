package actions;



import zer.sql.SQLAction;



public class Action_AddScroll extends SQLAction
{
  {
    super.query("INSERT INTO scrolls (id, author_id, side, title, description, script_func, test_func) values (?, ?, ?, ?, ?, ?, ?)");
  }

  public Action_AddScroll(String id, String author_id, int side, String title, String description, String script_func, String test_func)
  {
    put(id);
    put(author_id);
    put(side);
    put(title);
    put(description);
		put(script_func);
		put(test_func);
  }
}
