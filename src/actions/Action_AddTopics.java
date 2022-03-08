package actions;
 
 
 
import zer.sql.SQLAction;



public class Action_AddTopics extends SQLAction
{
  {
    super.query("INSERT IGNORE INTO topics (id, name, side) VALUES (?, ?, ?)");
  }

	public Action_AddTopics(int quantity)
	{
		for (int i = 1; i < quantity; ++i)
			super.query(super.query() + ", (?, ?, ?)");
	}

  public void add(String id, String name, int side)
  {
		put(id);
		put(name);
		put(side);
  }
}
