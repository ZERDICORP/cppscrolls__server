package actions;
   
  
  
public class Action_GetUserByEmail extends ActionTemplate_GetUser
{ 
  public Action_GetUserByEmail(String email) 
  {
		super("user.email = ?");

    put(email);
  }   
}
