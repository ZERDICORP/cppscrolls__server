package handlers;



import java.util.ArrayList;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import validators.Validator_UpdatePassword;

import constants.Status;
import constants.Field;
import constants.Regex;
 
import actions.Action_GetUserById;
import actions.Action_UpdateUserPasswordHashById;
 
import models.Model_User;

import tools.Tools;



@HTTPRoute
(
  pattern = "/user/password",
  type = "PUT",
  withAuthToken = true
)
public class Handler_UpdatePassword extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.get("Authentication-Token-Payload"));

		res.set("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();
		


		/*
     * request body validation
     */

    Status status = Validator_UpdatePassword.validate(req.get("Body"));
    if (status != Status.OK)
    {
      res.setBody(resBody
        .put(Field.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(req.get("Body"));



		/*
		 * checking for USER_DOES_NOT_EXIST
		 */
		
		ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(tokenPayload.getString(Field.UID)));
		if (users.size() == 0)
		{   
      res.setBody(resBody
        .put(Field.STATUS, Status.USER_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
    }

		Model_User user = users.get(0);


	
		/*
		 * checking for ACCESS_DENIED
		 */

    if (!user.password_hash.equals(Tools.sha256(reqBody.getString(Field.PASSWORD))))
		{   
      res.setBody(resBody
        .put(Field.STATUS, Status.ACCESS_DENIED.ordinal())
        .toString());
      return;
    }



		String password_hash = Tools.sha256(reqBody.getString(Field.NEW_PASSWORD));
		SQLInjector.inject(new Action_UpdateUserPasswordHashById(tokenPayload.getString(Field.UID), password_hash));



		res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .toString());
	}
}
