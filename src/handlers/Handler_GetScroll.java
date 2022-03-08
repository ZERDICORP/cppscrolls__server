package handlers;



import java.util.ArrayList;
	
import org.json.JSONObject;
	
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
 
import actions.Action_GetUserById;
 
import models.Model_Scroll;



@HTTPRoute
(
	pattern = "/scroll/" + CRegex.UUID,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetScroll extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		/*
		 * checking for SCROLL_DOES_NOT_EXIST
		 */

		ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetScrollById(req.path(1)));
		if (scrolls.size() == 0)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.SCROLL_DOES_NOT_EXIST.ordinal())
				.toString());
			return;
		}
		
		Model_Scroll scroll = scrolls.get(0);



		JSONObject scrollJSON = new JSONObject(scroll, new String[] { CField.NICKNAME, CField.BIO, CField.IMAGE, CField.SCORE, CField.SIDE });



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.USER, scrollJSON)
			.toString());
	}
}