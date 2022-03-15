package middlewares;



import java.util.Arrays;
import java.util.ArrayList;

import org.json.JSONObject;

import zer.http.HTTPMiddleware;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.http.HTTPRoute;
import zer.sql.SQLInjector;
import zer.file.FType;

import configs.AppConfig;

import constants.CStatus;
import constants.CField;
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

    String payload = Token.access(req.headers().get("Authentication-Token"), AppConfig.SECRET);
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
  	

		ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(tokenPayload.getString(CField.UID)));	
    if (users.size() == 0)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.ACCOUNT_REMOVED.ordinal())
        .toString());
      return false;
    }
	
		Model_User user = users.get(0);



    req.headers().put("Authentication-Token-Payload", payload);


	
		if (Arrays.stream(ann.marks()).anyMatch(s -> s.equals(CMark.WITH_PRELOADED_USER)))
			req.headers().put("Preloaded-User", new JSONObject(user, new String[] {
				CField.NICKNAME,
				CField.BIO,
				CField.IMAGE,
				CField.POINTS_LOSS,
				CField.SCORE,
				CField.SIDE,
				CField.PASSWORD_HASH,
				CField.SCROLL_CREATION_TIME
			}).toString());	



    return true;
  }
}
