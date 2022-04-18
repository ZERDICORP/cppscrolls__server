package handlers;



import java.util.ArrayList;
import java.sql.SQLException;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;

import validators.Validator_Confirm;

import configs.AppConfig;

import constants.CStatus;
import constants.CField;
import constants.Const;

import actions.Action_GetUserById;
import actions.Action_ConfirmUser;

import models.Model_User;

import tools.Token;
import tools.Tools;



@HTTPRoute
(
  pattern = "/user/confirm",
  type = "PUT"
)
public class Handler_Confirm extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
  {
    res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    CStatus status = Validator_Confirm.validate(req.bodyAsString());
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(req.bodyAsString());



    String payload = Token.access(
			reqBody.getString(CField.TOKEN),
			AppConfig.SECRET
		);

    if (payload == null)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }

		JSONObject confirmationTokenPayload = new JSONObject(payload);



		/*\
     * Check if the token has expired.
     */

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



    ArrayList<Model_User> users = new Action_GetUserById(
			confirmationTokenPayload.getString(CField.UID)
		).result();

    if (users.size() == 0 || users.get(0).confirmed == 1)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }

    Model_User user = users.get(0);
 


    new Action_ConfirmUser(user.id);



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.SIDE, user.side)
      .toString());
  }
}
