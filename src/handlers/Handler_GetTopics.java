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
 
import actions.Action_GetTopicsBySide;
 
import models.Model_Topic;



@HTTPRoute
(
	pattern = "/topics/" + CRegex.SIDE + "/" + CRegex.PAGE,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetTopics extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		List<String> topics = new Action_GetTopicsBySide(
			req.pathInt(1),
			req.pathInt(2)
		)
			.result()
			.stream()
			.map(o -> o.name)
			.collect(Collectors.toList());



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.TOPICS, new JSONArray(topics))
			.toString());
	}
}
