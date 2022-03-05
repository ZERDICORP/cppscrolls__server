package middlewares;



import java.util.Arrays;

import org.json.JSONObject;

import zer.http.HTTPMiddleware;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.http.HTTPRoute;
import zer.sql.SQLInjector;
import zer.file.FType;

import constants.CStatus;
import constants.CField;
import constants.CServer;
import constants.CMark;

import tools.Token;

import actions.Action_GetUserById;

import models.Model_User;



public class Middleware_Auth extends HTTPMiddleware
{
  @Override
  public boolean process(HTTPRequest req, HTTPResponse res, HTTPRoute ann)
  {
    if (!Arrays.stream(ann.marks()).anyMatch(s -> s.equals(CMark.WITH_AUTH_TOKEN)))
      return true;
		
		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



    /*
     * checking for INVALID_TOKEN
     */

    String payload = Token.access(req.headers().get("Authentication-Token"), CServer.SECRET);
    if (payload == null)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return false;
    }

		JSONObject tokenPayload = new JSONObject(payload);



		/*  
     * checking for ACCOUNT_REMOVED
     */
  
    if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(tokenPayload.getString(CField.UID))).size() == 0)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.ACCOUNT_REMOVED.ordinal())
        .toString());
      return false;
    }



    req.headers().put("Authentication-Token-Payload", payload);



    return true;
  }
}
