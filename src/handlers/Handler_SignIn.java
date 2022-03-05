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
 
import constants.CStatus;
import constants.CField;
import constants.CServer;
 
import actions.Action_GetUserByLoginAndPasswordHash;
 
import models.Model_User;
 
import tools.Token;
import tools.Tools;



@HTTPRoute
(
  pattern = CServer.API_PREFIX + "/signin",
  type = "POST"
)
public class Handler_SignIn extends HTTPHandler
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
 
    CStatus status = Validator_SignIn.validate(bodyAsString);
    if (status != CStatus.OK)
    {   
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }
     
    JSONObject reqBody = new JSONObject(bodyAsString);



    /*
     * checking for WRONG_LOGIN_OR_PASSWORD
     */

    ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class,
      new Action_GetUserByLoginAndPasswordHash(reqBody.getString(CField.LOGIN), Tools.sha256(reqBody.getString(CField.PASSWORD))));
    if (users.size() == 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.WRONG_LOGIN_OR_PASSWORD.ordinal())
        .toString());
      return;
    }

    Model_User user = users.get(0);



    /*
     * checking for USER_NOT_CONFIRMED
     */

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
    
    String token = Token.build(payload.toString(), CServer.SECRET);



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.UID, user.id)
      .put(CField.TOKEN, token)
      .toString());
  }
}
