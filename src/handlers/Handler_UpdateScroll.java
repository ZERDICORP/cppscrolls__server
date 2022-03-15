package handlers;



import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;
import org.json.JSONArray;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
import zer.sql.SQLInjector;
import zer.mail.MAILClient;

import constants.CStatus;
import constants.CField;
import constants.Const;
import constants.CMark;

import validators.Validator_UpdateScroll;

import actions.Action_GetScrollById;
import actions.Action_UpdateScrollById;
import actions.Action_AddTopics;
import actions.Action_DeleteScroll_TopicByScrollId;
import actions.Action_AddScroll_Topic;

import models.Model_Scroll;
import models.Model_Topic;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
  pattern = "/scroll",
  type = "PUT",
	marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_UpdateScroll extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

    res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    /*
     * request body validation
     */

    CStatus status = Validator_UpdateScroll.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetScrollById(
			tokenPayload.getString(CField.UID),
			reqBody.getString(CField.SCROLL_ID)
		));

		if (scrolls.size() == 0 || !scrolls.get(0).author_id.equals(tokenPayload.getString(CField.UID)))
		{
			res.body(resBody
        .put(CField.STATUS, CStatus.SCROLL_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
		}



		SQLInjector.inject(new Action_UpdateScrollById(
			reqBody.getString(CField.SCROLL_ID),
			reqBody.getString(CField.TITLE),
			reqBody.getString(CField.DESCRIPTION),
			reqBody.getString(CField.SCRIPT_FUNC),
			reqBody.getString(CField.TEST_FUNC)
		));



		/*\
		 * If the request contains a non-empty array of
		 * topics, then we must make the corresponding
		 * entries in the database.
		 */
		
		JSONArray topics = reqBody.getJSONArray(CField.TOPICS);
		if (topics.length() > 0)
		{
			/*\
			 * First, we add all topics to the database
			 * (topics that already exist will be ignored).
			 */

			Action_AddTopics action_addTopics = new Action_AddTopics(topics.length());
			for (int i = 0; i < topics.length(); ++i)
				action_addTopics.add(
					UUID.randomUUID().toString(),
					topics.getString(i),
					preloadedUser.getInt(CField.SIDE)
				);
			
			SQLInjector.inject(action_addTopics);
			


			SQLInjector.inject(new Action_DeleteScroll_TopicByScrollId(reqBody.getString(CField.SCROLL_ID)));



			/*\
			 * Now we add the relation of each theme_id to
			 * the current scroll_id (relation that already
			 * exists will be ignored).
			 */

			Action_AddScroll_Topic action_addScroll_topic = new Action_AddScroll_Topic(
				reqBody.getString(CField.SCROLL_ID),
				preloadedUser.getInt(CField.SIDE),
				topics.length()
			);

			for (int i = 0; i < topics.length(); ++i)
				action_addScroll_topic.add(topics.getString(i));

			SQLInjector.inject(action_addScroll_topic);
		}



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
  }
}
