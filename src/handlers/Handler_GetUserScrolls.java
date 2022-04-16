package handlers;



import java.util.ArrayList;
import java.sql.SQLException;

import org.json.JSONObject;
import org.json.JSONArray;
	
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
 
import constants.CStatus;
import constants.CField;
import constants.CRegex;
import constants.CMark;
import constants.Const;
 
import actions.Action_GetUserById;
import actions.Action_GetScrollsByAuthorId;
 
import models.Model_User;
import models.Model_Scroll;



@HTTPRoute
(
	pattern = "/user/" + CRegex.HASH + "/scrolls/" + CRegex.PAGE,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetUserScrolls extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		if (!req.path(1).equals(tokenPayload.getString(CField.UID)))
		{
			ArrayList<Model_User> users = new Action_GetUserById(
				req.path(1)
			).result();

			if (users.size() == 0)
			{
				res.body(resBody
					.put(CField.STATUS, CStatus.USER_DOES_NOT_EXIST.ordinal())
					.toString());
				return;
			}

			Model_User user = users.get(0);



			resBody.put(CField.AUTHOR_IMAGE, user.image);
		}



		ArrayList<Model_Scroll> scrolls = new Action_GetScrollsByAuthorId(
			tokenPayload.getString(CField.UID),
			req.path(1),
			req.pathInt(3)
		).result();



		JSONArray scrollsJSON = new JSONArray();
		for (Model_Scroll scroll : scrolls)
		{
			JSONObject scrollJSON = new JSONObject(scroll, new String[] {
				CField.ID,
				CField.TITLE,
				CField.DESCRIPTION,
				CField.SUCCESSFUL_ATTEMPTS,
				CField.UNSUCCESSFUL_ATTEMPTS,
				CField.BAD_MARKS,
				CField.VIEWS,
				CField.BAD_REPUTATION
			});
			
			if (scroll.description.length() >= Const.DESCRIPTION_PREVIEW_LENGTH)
				scrollJSON.put(
					CField.DESCRIPTION,
					scroll.description.toString().substring(0, Const.DESCRIPTION_PREVIEW_LENGTH)
				);

			scrollJSON.put(CField.SOLVED, scroll.solution != null);

			scrollsJSON.put(scrollJSON);
		}



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.SCROLLS, scrollsJSON)
			.toString());
	}
}
