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
 
import actions.SQLAction_GetUserByLoginAndPasswordHash;
 
import models.SQLModel_User;
 
import tools.Token;
import tools.Tools;



@HTTPRoute
(
  pattern = "/signin",
  type = "POST"
)
public class HTTPHandler_SignIn extends HTTPHandler
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

    ArrayList<SQLModel_User> users = SQLInjector.<SQLModel_User>inject(SQLModel_User.class,
      new SQLAction_GetUserByLoginAndPasswordHash(reqBody.getString(Field.LOGIN), Tools.sha256(reqBody.getString(Field.PASSWORD))));
    if (users.size() == 0)
    {
      res.setBody(resBody
        .put(Field.STATUS, Status.WRONG_LOGIN_OR_PASSWORD.ordinal())
        .toString());
      return;
    }

    SQLModel_User user = users.get(0);



    /*
     * checking for USER_NOT_CONFIRMED
     */

    if (!user.confirmed)
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
    payload.put(Field.ID, user.id);
    
    String token = Token.build(payload.toString(), Server.SECRET);



    res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .put(Field.TOKEN, token)
      .toString());
  }
}
