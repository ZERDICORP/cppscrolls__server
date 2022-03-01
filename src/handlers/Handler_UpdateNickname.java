package handlers;



import java.util.ArrayList;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import validators.Validator_UpdateNickname;

import constants.Status;
import constants.Field;
import constants.Regex;
 
import actions.Action_GetUserByNickname;
import actions.Action_UpdateUserNicknameById;
 
import models.Model_User;



@HTTPRoute
(
  pattern = "/user/nickname",
  type = "PUT",
  withAuthToken = true
)
public class Handler_UpdateNickname extends HTTPHandler
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

    Status status = Validator_UpdateNickname.validate(req.get("Body"));
    if (status != Status.OK)
    {
      res.setBody(resBody
        .put(Field.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(req.get("Body"));



    /*
     * checking for NICKNAME_ALREADY_IN_USE
     */

    if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserByNickname(reqBody.getString(Field.NICKNAME))).size() > 0)
    {
      res.setBody(resBody
        .put(Field.STATUS, Status.NICKNAME_ALREADY_IN_USE.ordinal())
        .toString());
      return;
    }



		SQLInjector.inject(new Action_UpdateUserNicknameById(tokenPayload.getString(Field.UID), reqBody.getString(Field.NICKNAME)));



		res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .toString());
	}
}
