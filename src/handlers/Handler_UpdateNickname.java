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

import constants.CStatus;
import constants.CField;
import constants.CServer;
import constants.CMark;
 
import actions.Action_GetUserByNickname;
import actions.Action_UpdateUserNicknameById;
 
import models.Model_User;



@HTTPRoute
(
  pattern = CServer.API_PREFIX + "/user/nickname",
  type = "PUT",
  marks = {
		CMark.WITH_AUTH_TOKEN
	}
)
public class Handler_UpdateNickname extends HTTPHandler
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

    CStatus status = Validator_UpdateNickname.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



    /*
     * checking for NICKNAME_ALREADY_IN_USE
     */

    if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserByNickname(reqBody.getString(CField.NICKNAME))).size() > 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.NICKNAME_ALREADY_IN_USE.ordinal())
        .toString());
      return;
    }



		SQLInjector.inject(new Action_UpdateUserNicknameById(tokenPayload.getString(CField.UID), reqBody.getString(CField.NICKNAME)));



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
	}
}
