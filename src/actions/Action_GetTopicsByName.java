package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_GetTopicsByName extends SQLAction
{
  {
    super.query("SELECT * FROM topics WHERE name = ?");
  }

	public Action_GetTopicsByName(int quantity)
	{
		for (int i = 1; i < quantity; ++i)
			super.query(super.query() + " OR name = ?");
	}

  public void add(String name)
  {
		put(name);
  }
}
