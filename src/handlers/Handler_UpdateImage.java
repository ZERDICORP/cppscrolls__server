package handlers;



import java.util.ArrayList;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import validators.Validator_UpdateNickname;

import constants.Status;
import constants.Field;
import constants.Regex;
 
import actions.Action_GetUserByNickname;
import actions.Action_UpdateUserNicknameById;
 
import models.Model_User;

import tools.Tools;



@HTTPRoute
(
  pattern = "/user/image",
  type = "PUT",
  withAuthToken = true
)
public class Handler_UpdateImage extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.get("Authentication-Token-Payload"));

		res.set("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		/*
     * checking for INVALID_IMAGE
     */

    // if (Tools.isValidImage(req.get("Body")))
    // {
    //   res.setBody(resBody
    //     .put(Field.STATUS, Status.INVALID_IMAGE.ordinal())
    //     .toString());
    //   return;   
    // }

	
		// String fileName = System.currentTimeMillis() + ".png";
		// 
		// byte[] data = req.get("Body").getBytes();

		// try
		// {
		// 	OutputStream outputStream = new FileOutputStream("images/" + fileName);
		// 	outputStream.write(data);
		// }
		// catch (IOException e) { e.printStackTrace(); }

		// SQLInjector.inject(new Action_UpdateUserImage(tokenPayload.getString(Field.UID), reqBody.getString(Field.NICKNAME)));

		res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .toString());
	}
}
