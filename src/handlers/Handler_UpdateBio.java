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

import constants.CStatus;
import constants.CField;
import constants.CServer;
import constants.CMark;
 
import actions.Action_GetUserByNickname;
import actions.Action_UpdateUserBioById;
 
import models.Model_User;



@HTTPRoute
(
  pattern = CServer.API_PREFIX + "/user/bio",
  type = "PUT",
  marks = {
		CMark.WITH_AUTH_TOKEN
	}
)
public class Handler_UpdateBio extends HTTPHandler
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

    CStatus status = Validator_UpdateBio.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		SQLInjector.inject(new Action_UpdateUserBioById(tokenPayload.getString(CField.UID), reqBody.getString(CField.BIO)));



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
	}
}
