package middlewares;



import java.sql.SQLException;
import java.util.Arrays;
import java.util.ArrayList;

import org.json.JSONObject;

import zer.http.HTTPMiddleware;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.http.HTTPRoute;
import zer.file.FType;

import configs.AppConfig;

import constants.Const;
import constants.CStatus;
import constants.CField;
import constants.CMark;

import tools.Token;

import actions.Action_GetUserById;

import models.Model_User;

import tools.Tools;



public class Middleware_Auth extends HTTPMiddleware
{
  @Override
  public boolean process(HTTPRequest req, HTTPResponse res, HTTPRoute ann) throws SQLException
  {
    if (!Arrays.stream(ann.marks()).anyMatch(s -> s.equals(CMark.WITH_AUTH_TOKEN)))
      return true;
		
		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



    String payload = Token.access(req.headers().get("Authentication-Token"), AppConfig.SECRET);
    if (payload == null)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return false;
    }

		JSONObject tokenPayload = new JSONObject(payload);



		/*\
		 * Check if the token has expired.
		 */

		if (Tools.daysPassed(tokenPayload.getString(CField.TOKEN_CREATION_DATE)) > Const.TOKEN_EXPIRATION_DAYS)
		{
			res.body(resBody
        .put(CField.STATUS, CStatus.TOKEN_EXPIRED.ordinal())
        .toString());
      return false;
		}



		req.headers().put("Authentication-Token-Payload", payload);



		ArrayList<Model_User> users = new Action_GetUserById(
			tokenPayload.getString(CField.UID)
		).result();

    if (users.size() == 0)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.ACCOUNT_REMOVED.ordinal())
        .toString());
      return false;
    }
	
		Model_User user = users.get(0);
	


		if (Arrays.stream(ann.marks()).anyMatch(s -> s.equals(CMark.WITH_PRELOADED_USER)))
			req.headers().put("Preloaded-User", new JSONObject(user, new String[] {
				CField.NICKNAME,
				CField.EMAIL,
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
