package handlers;



import java.util.ArrayList;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import validators.Validator_UpdateNickname;

import constants.CStatus;
import constants.CField;
import constants.CServer;
import constants.CMark;
 
import actions.Action_GetUserById;
import actions.Action_UpdateUserImageById;
 
import models.Model_User;

import tools.Tools;



@HTTPRoute
(
  pattern = "/user/image",
  type = "PUT",
  marks = { CMark.WITH_AUTH_TOKEN	}
)
public class Handler_UpdateImage extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();



		/*
     * checking for USER_DOES_NOT_EXIST
     */

    ArrayList<Model_User> users = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(tokenPayload.getString(CField.UID)));
    if (users.size() == 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.USER_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
    }
  
    Model_User user = users.get(0);



		/*
		 * if user have default image, we generate
		 * new image name and change image of the user
		 */

		String imageFileName = user.image;
		if (imageFileName == null)
		{
			imageFileName = user.nickname + ".jpg";
			SQLInjector.inject(new Action_UpdateUserImageById(tokenPayload.getString(CField.UID), imageFileName));
		}



		/*
		 * write image to file called {imageFileName}.jpg
		 */

		try
		{
			OutputStream os = new FileOutputStream(CServer.IMAGES_FOLDER_PATH + imageFileName);
			os.write(req.body());
			os.close();
		}
		catch (IOException e) { e.printStackTrace(); }



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
	}
}
