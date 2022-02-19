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

import constants.Status;
import constants.Field;
import constants.Server;

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
    res.set("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();



    /*
     * request body validation
     */

    Status status = Validator_Confirm.validate(req.get("Body"));
    if (status != Status.OK)
    {
      res.setBody(resBody
        .put(Field.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(req.get("Body"));



    /*
     * checking for INVALID_TOKEN
     *\
     * The variable "payload" stores the user ID.
     */

    String payload = Token.access(reqBody.getString(Field.TOKEN), Server.SECRET);
    if (payload == null)
    {
      res.setBody(resBody
        .put(Field.STATUS, Status.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }

    ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(payload));
    if (users.size() == 0 || users.get(0).confirmed == 1)
    {
      res.setBody(resBody
        .put(Field.STATUS, Status.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }

    Model_User user = users.get(0);
 


    /*
     * user confirmation
     */

    SQLInjector.inject(new Action_ConfirmUser(user.id));



    res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .put(Field.SIDE, user.side)
      .toString());
  }
}
