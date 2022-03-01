package handlers;



import java.util.ArrayList;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;
 
import validators.Validator_SignIn;
 
import constants.Status;
import constants.Field;
import constants.Server;
 
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
  public void handle(HTTPRequest req, HTTPResponse res)
  {   
    res.set("Content-Type", FType.JSON.mime());
   
    JSONObject resBody = new JSONObject();
 
 
 
    /*
     * request body validation
     */
 
    Status status = Validator_SignIn.validate(req.get("Body"));
    if (status != Status.OK)
    {   
      res.setBody(resBody
        .put(Field.STATUS, status.ordinal())
        .toString());
      return;
    }
     
    JSONObject reqBody = new JSONObject(req.get("Body"));



    /*
     * checking for WRONG_LOGIN_OR_PASSWORD
     */

    ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class,
      new Action_GetUserByLoginAndPasswordHash(reqBody.getString(Field.LOGIN), Tools.sha256(reqBody.getString(Field.PASSWORD))));
    if (users.size() == 0)
    {
      res.setBody(resBody
        .put(Field.STATUS, Status.WRONG_LOGIN_OR_PASSWORD.ordinal())
        .toString());
      return;
    }

    Model_User user = users.get(0);



    /*
     * checking for USER_NOT_CONFIRMED
     */

    if (user.confirmed == 0)
    {
      res.setBody(resBody
        .put(Field.STATUS, Status.USER_NOT_CONFIRMED.ordinal())
        .toString());
      return;
    } 



    /*
     * creating authentication token
     */

    JSONObject payload = new JSONObject();
    payload.put(Field.UID, user.id);
    
    String token = Token.build(payload.toString(), Server.SECRET);



    res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
			.put(Field.UID, user.id)
      .put(Field.TOKEN, token)
      .toString());
  }
}
