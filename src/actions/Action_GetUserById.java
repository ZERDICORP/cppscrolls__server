package actions;
 
 
 
public class Action_GetUserById extends ActionTemplate_GetUser
{
  public Action_GetUserById(String id)
  {
		super("user.id = ?");

		put(id);
  }
}
