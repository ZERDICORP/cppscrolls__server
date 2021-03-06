package handlers;



import java.sql.SQLException;
import java.util.ArrayList;
import java.io.File;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
import zer.file.FTool;
 
import validators.Validator_Confirm;

import configs.AppConfig;

import constants.CStatus;
import constants.CField;
import constants.CMark;
import constants.Const;
 
import actions.Action_DeleteUserById;
 
import models.Model_User;
 
import tools.Token;
import tools.Tools;



@HTTPRoute
(
  pattern = "/user/delete/confirm",
  type = "PUT",
	marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_ConfirmAccountDeletion extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
  {
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

    res.headers().put("Content-Type", FType.JSON.mime());
   
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();
 
 
 
    CStatus status = Validator_Confirm.validate(bodyAsString);
    if (status != CStatus.OK)
    {   
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }
     
    JSONObject reqBody = new JSONObject(bodyAsString);



		String payload = Token.access(reqBody.getString(CField.TOKEN), AppConfig.SECRET);
    if (payload == null)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return;
    }   
 
    JSONObject confirmationTokenPayload = new JSONObject(payload);
 
 
 
    /*\ 
     * Check if the token has expired.
     */
 
    if
		(
			Tools.daysPassed(confirmationTokenPayload.
				getString(CField.TOKEN_CREATION_DATE)) > Const.CONFIRMATION_TOKEN_EXPIRATION_DAYS
		)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.TOKEN_EXPIRED.ordinal())
        .toString());
      return;
    }



		new Action_DeleteUserById(tokenPayload.getString(CField.UID));



		/*\
		 * Removing user image.
		 */

		if (preloadedUser.has(CField.IMAGE))
			FTool.delete(AppConfig.IMAGES_FOLDER_PATH + preloadedUser.getString(CField.IMAGE));



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.SIDE, preloadedUser.getInt(CField.SIDE))
      .toString());
  }
}
