package handlers;



import java.util.ArrayList;
import java.sql.SQLException;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
import zer.mail.MAILClient;

import validators.Validator_Confirm;

import constants.CStatus;
import constants.CField;
import constants.CMark;
import constants.Const;

import configs.AppConfig;
 
import actions.Action_GetUserById;
import actions.Action_UpdateUserPasswordHash;
 
import models.Model_User;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
  pattern = "/user/password_update/confirm",
  type = "PUT",
  marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_ConfirmPasswordUpdate extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

		res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    CStatus status = Validator_Confirm.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		String payload = Token.access(reqBody.getString(CField.TOKEN), AppConfig.SECRET);
    if (payload == null)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }

    JSONObject confirmationTokenPayload = new JSONObject(payload);



    if
    (
      Tools.daysPassed(confirmationTokenPayload.
        getString(CField.TOKEN_CREATION_DATE)) > Const.CONFIRMATION_TOKEN_EXPIRATION_DAYS
    )
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.TOKEN_EXPIRED.ordinal())
        .toString());
      return;
    }



		if
    (
      preloadedUser
        .getString(CField.PASSWORD_HASH)
        .equals(confirmationTokenPayload.getString(CField.PASSWORD_HASH))
    )
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.DATA_IS_NOT_CHANGED.ordinal())
        .toString());
      return;
    }



		new Action_UpdateUserPasswordHash(
			tokenPayload.getString(CField.UID),
			confirmationTokenPayload.getString(CField.PASSWORD_HASH)
		);



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.SIDE, preloadedUser.getInt(CField.SIDE))
      .toString());
	}
}
