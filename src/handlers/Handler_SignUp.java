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

import constants.Status;
import constants.Field;
import constants.Server;

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
    res.set("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();



    /*
     * request body validation
     */

    Status status = Validator_SignUp.validate(req.get("Body"));
    if (status != Status.OK)
    {
      res.setBody(resBody
        .put(Field.STATUS, status.ordinal())
        .toString());
      return;   
    }

    JSONObject reqBody = new JSONObject(req.get("Body"));



    /*
     * checking for USER_ALREADY_EXIST
     */
 
    if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserByEmail(reqBody.getString(Field.EMAIL))).size() > 0)
    {   
      res.setBody(resBody
        .put(Field.STATUS, Status.USER_ALREADY_EXIST.ordinal())
        .toString());
      return;
    }   
    


    /*
     * checking for NICKNAME_ALREADY_IN_USE
     */
 
    if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserByNickname(reqBody.getString(Field.NICKNAME))).size() > 0)
    {
      res.setBody(resBody
        .put(Field.STATUS, Status.NICKNAME_ALREADY_IN_USE.ordinal())
        .toString());
      return;
    }



    /*
     * adding user to database
     */

    String id = UUID.randomUUID().toString();
    String password_hash = Tools.sha256(reqBody.getString(Field.PASSWORD));

    SQLInjector.inject(new Action_AddUser(id, password_hash,
      reqBody.getString(Field.NICKNAME), reqBody.getString(Field.EMAIL), reqBody.getInt(Field.SIDE)));



    /*
     * creating confirmation token
     */
    
    String token = Token.build(id, Server.SECRET);



    /*
     * sending confirmation email
     */

    MAILClient.send(reqBody.getString(Field.EMAIL), "CPP Scrolls", token);



    res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .toString());
  }
}
