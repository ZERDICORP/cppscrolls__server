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
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();


	
		ArrayList<Model_Scroll> scrolls = new Action_GetHistory(
			tokenPayload.getString(CField.UID),
			req.pathInt(2)
		).result();



		JSONArray scrollsJSON = new JSONArray();
    for (Model_Scroll scroll : scrolls)
    {   
      JSONObject scrollJSON = new JSONObject(scroll, new String[] {
        CField.ID,
        CField.TITLE,
				CField.AUTHOR_ID,
				CField.AUTHOR_IMAGE,
        CField.DESCRIPTION,
        CField.SUCCESSFUL_ATTEMPTS,
        CField.UNSUCCESSFUL_ATTEMPTS,
        CField.BAD_MARKS,
        CField.VIEWS,
        CField.BAD_REPUTATION
      }); 
          
      scrollJSON.put(CField.SOLVED, scroll.solution != null);
  
      scrollsJSON.put(scrollJSON);
    }



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.SCROLLS, scrollsJSON)
			.toString());
	}
}
