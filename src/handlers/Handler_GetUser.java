package handlers;



import java.util.ArrayList;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;
 
import constants.Status;
import constants.Field;
import constants.Regex;
 
import actions.Action_GetUserById;
 
import models.Model_User;



@HTTPRoute
(
  pattern = "/user/" + Regex.UUID,
  type = "GET",
  withAuthToken = true
)
public class Handler_GetUser extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		res.set("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(req.path(1)));
    if (users.size() == 0)
    {   
      res.setBody(resBody
        .put(Field.STATUS, Status.USER_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
    }   



		/*  
     * checking for USER_NOT_CONFIRMED
     */
  
    if (users.get(0).confirmed == 0)
    {   
      res.setBody(resBody
        .put(Field.STATUS, Status.USER_NOT_CONFIRMED.ordinal())
        .toString());
      return;
    }

		JSONObject user = new JSONObject(users.get(0), new String[] {"nickname", "image", "score", "side"});

		res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .put(Field.USER, user)
      .toString());
	}
}
