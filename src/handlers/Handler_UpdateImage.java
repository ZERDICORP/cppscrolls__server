package handlers;



import java.sql.SQLException;
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
import zer.file.FType;

import validators.Validator_UpdateNickname;

import configs.AppConfig;

import constants.CStatus;
import constants.CField;
import constants.CMark;
 
import actions.Action_GetUserById;
import actions.Action_UpdateUserImage;
 
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
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();



    ArrayList<Model_User> users = new Action_GetUserById(
			tokenPayload.getString(CField.UID)
		).result();

    if (users.size() == 0)
    {
      res.body(resBody
        .put(CField.STATUS, CStatus.USER_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
    }
  
    Model_User user = users.get(0);



		String imageFileName = user.image;
		if (imageFileName == null)
		{
			imageFileName = user.nickname + "." + FType.JPG;

			new Action_UpdateUserImage(
				tokenPayload.getString(CField.UID),
				imageFileName
			);
		}



		if (req.body().length == 0)
		{
			res.body(resBody
        .put(CField.STATUS, CStatus.EMPTY_IMAGE_DATA.ordinal())
        .toString());
      return;
		}



		/*\
		 * Write image data to the file.
		 */
	
		try
		{
			OutputStream os = new FileOutputStream(AppConfig.IMAGES_FOLDER_PATH + imageFileName);
			os.write(req.body());
			os.close();
		}
		catch (IOException e) { e.printStackTrace(); }



		res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
	}
}
