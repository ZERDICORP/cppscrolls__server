package handlers;



import java.util.ArrayList;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import validators.Validator_Confirm;

import constants.CStatus;
import constants.CField;
import constants.CServer;

import actions.Action_GetUserById;
import actions.Action_ConfirmUser;

import models.Model_User;

import tools.Token;



@HTTPRoute
(
  pattern = "/user/confirm",
  type = "POST"
)
public class Handler_Confirm extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
    res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();


    /*
     * request body validation
     */

    CStatus status = Validator_Confirm.validate(req.bodyAsString());
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(req.bodyAsString());



    /*
     * checking for INVALID_TOKEN
     *\
     * The variable "payload" stores the user ID.
     */

    String payload = Token.access(reqBody.getString(CField.TOKEN), CServer.SECRET);
    if (payload == null)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }

    ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(payload));
    if (users.size() == 0 || users.get(0).confirmed == 1)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }

    Model_User user = users.get(0);
 


    /*
     * user confirmation
     */

    SQLInjector.inject(new Action_ConfirmUser(user.id));



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.SIDE, user.side)
      .toString());
  }
}
