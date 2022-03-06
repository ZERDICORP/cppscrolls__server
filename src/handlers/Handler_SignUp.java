package handlers;



import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;
import java.util.UUID;
import java.util.ArrayList;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
import zer.sql.SQLInjector;
import zer.mail.MAILClient;

import validators.Validator_SignUp;

import constants.CStatus;
import constants.CField;
import constants.CServer;

import actions.Action_GetUserByEmail;
import actions.Action_GetUserByNickname;
import actions.Action_AddUser;

import models.Model_User;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
  pattern = "/signup",
  type = "POST"
)
public class Handler_SignUp extends HTTPHandler
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

    CStatus status = Validator_SignUp.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



    /*
     * checking for USER_ALREADY_EXIST
     */
 
    if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserByEmail(reqBody.getString(CField.EMAIL))).size() > 0)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.USER_ALREADY_EXIST.ordinal())
        .toString());
      return;
    }   
    


    /*
     * checking for NICKNAME_ALREADY_IN_USE
     */
 
    if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserByNickname(reqBody.getString(CField.NICKNAME))).size() > 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.NICKNAME_ALREADY_IN_USE.ordinal())
        .toString());
      return;
    }



    /*
     * adding user to database
     */

    String id = UUID.randomUUID().toString();
    String password_hash = Tools.sha256(reqBody.getString(CField.PASSWORD));

    SQLInjector.inject(new Action_AddUser(id, password_hash,
      reqBody.getString(CField.NICKNAME), reqBody.getString(CField.EMAIL), reqBody.getInt(CField.SIDE)));



    /*
     * creating confirmation token
     */
    
    String token = Token.build(id, CServer.SECRET);



    /*
     * sending confirmation email
     */

    MAILClient.send(reqBody.getString(CField.EMAIL), "CPP Scrolls", token);



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
  }
}
