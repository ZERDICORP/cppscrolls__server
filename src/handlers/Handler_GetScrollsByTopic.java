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
 
import actions.Action_GetTopicBySideAndName;
import actions.Action_IncTopicRequests;
import actions.Action_GetScrollsByTopicId;
 
import models.Model_Topic;
import models.Model_Scroll;



@HTTPRoute
(
	pattern = "/scrolls/" + CRegex.SIDE + "/" + CRegex.TOPIC + "/" + CRegex.PAGE,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetScrollsByTopic extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		ArrayList<Model_Topic> topics = new Action_GetTopicBySideAndName(
			req.pathInt(1),
			req.path(2)
		).result();

		if (topics.size() == 0)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.TOPIC_DOES_NOT_EXIST.ordinal())
				.toString());
			return;
		}

		Model_Topic topic = topics.get(0);



		new Action_IncTopicRequests(topic.id);



		ArrayList<Model_Scroll> scrolls = new Action_GetScrollsByTopicId(
			tokenPayload.getString(CField.UID),
			topic.id,
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
        CField.BAD_REPUTATION,
				CField.AUTHOR_ID,
				CField.AUTHOR_IMAGE
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
