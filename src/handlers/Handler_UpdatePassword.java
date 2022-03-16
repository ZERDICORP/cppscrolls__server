package handlers;



import java.util.ArrayList;
import java.sql.SQLException;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;

import validators.Validator_UpdatePassword;

import constants.CStatus;
import constants.CField;
import constants.CMark;
 
import actions.Action_GetUserById;
import actions.Action_UpdateUserPasswordHash;
 
import models.Model_User;

import tools.Tools;



@HTTPRoute
(
  pattern = "/user/password",
  type = "PUT",
  marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_UpdatePassword extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    CStatus status = Validator_UpdatePassword.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		ArrayList<Model_User> users = new Action_GetUserById(
			tokenPayload.getString(CField.UID)
		).result();

		if (users.size() == 0)
		{   
      res.body(resBody
        .put(CField.STATUS, CStatus.USER_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
    }

		Model_User user = users.get(0);



		String passwordHash = Tools.sha256(reqBody.getString(CField.NEW_PASSWORD));
    if (!user.password_hash.equals(passwordHash))
		{   
      res.body(resBody
        .put(CField.STATUS, CStatus.ACCESS_DENIED.ordinal())
        .toString());
      return;
    }



		new Action_UpdateUserPasswordHash(
			tokenPayload.getString(CField.UID),
			passwordHash
		);



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
	}
}
