package handlers;



import java.util.ArrayList;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import validators.Validator_UpdateBio;

import constants.Status;
import constants.Field;
import constants.Regex;
 
import actions.Action_GetUserByNickname;
import actions.Action_UpdateUserBioById;
 
import models.Model_User;



@HTTPRoute
(
  pattern = "/user/bio",
  type = "PUT",
  withAuthToken = true
)
public class Handler_UpdateBio extends HTTPHandler
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

    Status status = Validator_UpdateBio.validate(req.get("Body"));
    if (status != Status.OK)
    {
      res.setBody(resBody
        .put(Field.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(req.get("Body"));



		SQLInjector.inject(new Action_UpdateUserBioById(tokenPayload.getString("uid"), reqBody.getString("bio")));



		res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .toString());
	}
}
