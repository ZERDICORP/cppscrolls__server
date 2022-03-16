package handlers;



import java.util.ArrayList;
import java.sql.Timestamp;
import java.sql.SQLException;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
 
import validators.Validator_SignIn;
 
import configs.AppConfig;

import constants.CStatus;
import constants.CField;
 
import actions.Action_GetUserByLoginAndPasswordHash;
 
import models.Model_User;
 
import tools.Token;
import tools.Tools;



@HTTPRoute
(
  pattern = "/signin",
  type = "POST"
)
public class Handler_SignIn extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
  {   
    res.headers().put("Content-Type", FType.JSON.mime());
   
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();
 
 
 
    CStatus status = Validator_SignIn.validate(bodyAsString);
    if (status != CStatus.OK)
    {   
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }
     
    JSONObject reqBody = new JSONObject(bodyAsString);



    ArrayList<Model_User> users = new Action_GetUserByLoginAndPasswordHash(
			reqBody.getString(CField.LOGIN),
			Tools.sha256(reqBody.getString(CField.PASSWORD))
		).result();

    if (users.size() == 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.WRONG_LOGIN_OR_PASSWORD.ordinal())
        .toString());
      return;
    }

    Model_User user = users.get(0);



    if (user.confirmed == 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.USER_NOT_CONFIRMED.ordinal())
        .toString());
      return;
    } 



    /*
     * creating authentication token
     */

    JSONObject payload = new JSONObject();
    payload.put(CField.UID, user.id);
		payload.put(CField.TOKEN_CREATION_DATE, Tools.currentTimestamp().toString());

    String token = Token.build(payload.toString(), AppConfig.SECRET);



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.UID, user.id)
      .put(CField.TOKEN, token)
      .toString());
  }
}
