package handlers;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
	
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
 
import actions.Action_GetTopicsByPatternAndSide;
 
import models.Model_Topic;



@HTTPRoute
(
	pattern = "/matched_topics/" + CRegex.TOPIC,
	type = "GET",
	marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_GetMatchedTopics extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		List<String> topics = SQLInjector.<Model_Topic>inject(Model_Topic.class, new Action_GetTopicsByPatternAndSide(
			req.path(1),
			preloadedUser.getInt(CField.SIDE)
		))
			.stream()
			.map(o -> o.name)
			.collect(Collectors.toList());



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.TOPICS, new JSONArray(topics))
			.toString());
	}
}
