package handlers;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
 
import actions.Action_GetScrollById;
import actions.Action_GetUserById;
import actions.Action_GetTopicsByScrollId;
import actions.Action_AddUniqueScrollVisit;
 
import models.Model_Scroll;
import models.Model_Topic;



@HTTPRoute
(
	pattern = "/scroll/" + CRegex.UUID,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetScroll extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		ArrayList<Model_Scroll> scrolls = new Action_GetScrollById(
			tokenPayload.getString(CField.UID),
			req.path(1)
		).result();

		if (scrolls.size() == 0)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.SCROLL_DOES_NOT_EXIST.ordinal())
				.toString());
			return;
		}
		
		Model_Scroll scroll = scrolls.get(0);



		if (scroll.visited == 0)
		{
			new Action_AddUniqueScrollVisit(
				scroll.id,
				tokenPayload.getString(CField.UID)
			);

			scroll.views++;
		}



		JSONObject scrollJSON = new JSONObject(scroll, new String[] {
			CField.TITLE,
			CField.DESCRIPTION,
			CField.SCRIPT_FUNC,
			CField.TEST_FUNC,
			CField.SUCCESSFUL_ATTEMPTS,
			CField.UNSUCCESSFUL_ATTEMPTS,
			CField.AUTHOR_ID,
			CField.AUTHOR_IMAGE,
			CField.VIEWS,
			CField.BAD_MARKS,
			CField.BAD_MARK,
			CField.BAD_REPUTATION,
			CField.SOLUTION
		});



		List<String> topics = new Action_GetTopicsByScrollId(scroll.id)
			.result()
			.stream()
			.map(o -> o.name)
			.collect(Collectors.toList());

		scrollJSON.put(CField.TOPICS, new JSONArray(topics));



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.SCROLL, scrollJSON)
			.toString());
	}
}
