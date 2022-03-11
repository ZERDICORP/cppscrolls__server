package handlers;



import java.util.ArrayList;
import java.io.File;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;
 
import validators.Validator_DeleteAccount;

import configs.AppConfig;

import constants.CStatus;
import constants.CField;
import constants.CMark;
 
import actions.Action_DeleteUserById;
 
import models.Model_User;
 
import tools.Token;
import tools.Tools;



@HTTPRoute
(
  pattern = "/user",
  type = "DELETE",
	marks = {
    CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
  }
)
public class Handler_DeleteAccount extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

    res.headers().put("Content-Type", FType.JSON.mime());
   
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();
 
 
 
    /*
     * request body validation
     */
 
    CStatus status = Validator_DeleteAccount.validate(bodyAsString);
    if (status != CStatus.OK)
    {   
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }
     
    JSONObject reqBody = new JSONObject(bodyAsString);



    /*
     * checking for ACCESS_DENIED
     */

		if (!preloadedUser.getString(CField.PASSWORD_HASH).equals(Tools.sha256(reqBody.getString(CField.PASSWORD))))
		{
			res.body(resBody
        .put(CField.STATUS, CStatus.ACCESS_DENIED.ordinal())
        .toString());
      return;
		}



		SQLInjector.inject(new Action_DeleteUserById(tokenPayload.getString(CField.UID)));



		/*
		 * removing user image
		 */

		if (preloadedUser.has(CField.IMAGE))
		{
			File userImageFile = new File(AppConfig.IMAGES_FOLDER_PATH + preloadedUser.getString(CField.IMAGE));
			if (userImageFile.exists())
				userImageFile.delete();
		}



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
  }
}
