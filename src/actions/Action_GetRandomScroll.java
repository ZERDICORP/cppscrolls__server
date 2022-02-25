package actions;



import zer.sql.SQLAction;



public class Action_GetRandomScroll extends SQLAction
{
	{
		super.query("SELECT * FROM scrolls WHERE side = ? ORDER BY RAND() LIMIT 1");
	}

	public Action_GetRandomScroll(int side)
	{
		put(side);
	}
}
