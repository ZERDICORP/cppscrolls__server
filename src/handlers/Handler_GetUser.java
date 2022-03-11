package handlers;



import java.util.ArrayList;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;
 
import constants.CStatus;
import constants.CField;
import constants.CRegex;
import constants.CMark;
import constants.Const;
 
import actions.Action_GetUserById;
 
import models.Model_User;



@HTTPRoute
(
  pattern = "/user/" + CRegex.UUID,
  type = "GET",
  marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_GetUser extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

		res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();
	


		/*\
		 * If the incoming id is equal to the id
		 * from the token, then we can not make a
		 * request to the database, but simply use
		 * the data that we received in the middleware.
		 */

		if (req.path(1).equals(tokenPayload.getString(CField.UID))) 
		{
			JSONObject userJSON = new JSONObject(preloadedUser, new String[] {
				CField.NICKNAME,
				CField.BIO,
				CField.IMAGE,
				CField.SCORE,
				CField.SIDE
			});

			res.body(resBody
				.put(CField.STATUS, CStatus.OK.ordinal())
				.put(CField.USER, userJSON)
				.toString());
			return;
		}



		/*
     * checking for USER_DOES_NOT_EXIST
     */

		ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(req.path(1)));
    if (users.size() == 0)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.USER_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
    }   
		
		Model_User user = users.get(0);



		JSONObject userJSON = new JSONObject(user, new String[] {
			CField.NICKNAME,
			CField.BIO,
			CField.IMAGE,
			CField.SCORE,
			CField.SIDE
		});



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.USER, userJSON)
      .toString());
	}
}
