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

import constants.CStatus;
import constants.CField;
import constants.CServer;
import constants.CMark;
 
import actions.Action_GetUserById;
import actions.Action_UpdateUserPasswordHashById;
 
import models.Model_User;

import tools.Tools;



@HTTPRoute
(
  pattern = CServer.API_PREFIX + "/user/password",
  type = "PUT",
  marks = {
		CMark.WITH_AUTH_TOKEN
	}
)
public class Handler_UpdatePassword extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();
		


		/*
     * request body validation
     */

    CStatus status = Validator_UpdatePassword.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		/*
		 * checking for USER_DOES_NOT_EXIST
		 */
		
		ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(tokenPayload.getString(CField.UID)));
		if (users.size() == 0)
		{   
      res.body(resBody
        .put(CField.STATUS, CStatus.USER_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
    }

		Model_User user = users.get(0);


	
		/*
		 * checking for ACCESS_DENIED
		 */

    if (!user.password_hash.equals(Tools.sha256(reqBody.getString(CField.PASSWORD))))
		{   
      res.body(resBody
        .put(CField.STATUS, CStatus.ACCESS_DENIED.ordinal())
        .toString());
      return;
    }



		String password_hash = Tools.sha256(reqBody.getString(CField.NEW_PASSWORD));
		SQLInjector.inject(new Action_UpdateUserPasswordHashById(tokenPayload.getString(CField.UID), password_hash));



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
	}
}
