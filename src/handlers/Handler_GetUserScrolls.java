package handlers;



import java.util.ArrayList;
	
import org.json.JSONObject;
import org.json.JSONArray;
	
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
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
	pattern = "/user/" + CRegex.UUID + "/scrolls/" + CRegex.PAGE,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetUserScrolls extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		if (SQLInjector.<Model_User>inject(Model_User.class, new Action_GetUserById(req.path(1))).size() == 0)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.USER_DOES_NOT_EXIST.ordinal())
				.toString());
			return;
		}



		ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetScrollsByAuthorId(
			req.path(1),
			tokenPayload.getString(CField.UID),
			Const.SCROLLS_PAGE_SIZE,
			Const.SCROLLS_PAGE_SIZE * req.pathInt(3)
		));



		JSONArray scrollsJSON = new JSONArray();
		for (Model_Scroll scroll : scrolls)
			scrollsJSON.put(new JSONObject(scroll, new String[] {
				CField.ID,
				CField.TITLE,
				CField.DESCRIPTION,
				CField.SUCCESSFUL_ATTEMPTS,
				CField.UNSUCCESSFUL_ATTEMPTS,
				CField.AUTHOR_IMAGE,
				CField.BAD_MARKS,
				CField.VIEWS
			}));



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.SCROLLS, scrollsJSON)
			.toString());
	}
}
