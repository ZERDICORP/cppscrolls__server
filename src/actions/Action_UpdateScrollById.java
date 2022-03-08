package actions;



import zer.sql.SQLAction;



public class Action_UpdateScrollById extends SQLAction
{
  {
    super.query("UPDATE scrolls SET title = ?, description = ?, script_func = ?, test_func = ? WHERE id = ?");
  }

  public Action_UpdateScrollById(String id, String title, String description, String script_func, String test_func)
  {
    put(title);
    put(description);
		put(script_func);
		put(test_func);
    put(id);
  }
}
