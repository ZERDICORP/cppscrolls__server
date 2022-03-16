package handlers;



import java.util.ArrayList;
import java.sql.SQLException;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;

import validators.Validator_UpdateNickname;

import constants.CStatus;
import constants.CField;
import constants.CMark;
 
import actions.Action_GetUserByNickname;
import actions.Action_UpdateUserNickname;
 
import models.Model_User;



@HTTPRoute
(
  pattern = "/user/nickname",
  type = "PUT",
  marks = { CMark.WITH_AUTH_TOKEN	}
)
public class Handler_UpdateNickname extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    CStatus status = Validator_UpdateNickname.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



    if (new Action_GetUserByNickname(reqBody.getString(CField.NICKNAME)).result().size() > 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.NICKNAME_ALREADY_IN_USE.ordinal())
        .toString());
      return;
    }



		new Action_UpdateUserNickname(
			tokenPayload.getString(CField.UID),
			reqBody.getString(CField.NICKNAME)
		);



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
	}
}
