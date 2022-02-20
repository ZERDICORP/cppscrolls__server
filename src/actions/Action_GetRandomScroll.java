package actions;



import zer.sql.SQLAction;



public class Action_GetRandomScroll extends SQLAction
{
	{
		super.query("SELECT * FROM scrolls ORDER BY RAND() LIMIT 1");
	}
}
