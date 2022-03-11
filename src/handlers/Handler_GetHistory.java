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
import constants.CServer;

import actions.Action_GetHistory;
 
import models.Model_Scroll;



@HTTPRoute
(
	pattern = "/user/history/" + CRegex.PAGE,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetHistory extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();


	
		ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetHistory(
			tokenPayload.getString(CField.UID),
			CServer.SCROLLS_PAGE_SIZE,
			CServer.SCROLLS_PAGE_SIZE * req.pathInt(2)
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
