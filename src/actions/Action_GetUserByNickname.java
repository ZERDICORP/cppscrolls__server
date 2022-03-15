package actions;



public class Action_GetUserByNickname extends ActionTemplate_GetUser
{
  public Action_GetUserByNickname(String nickname) 
  {
		super("user.nickname = ?");

		put(nickname);
  }   
}
