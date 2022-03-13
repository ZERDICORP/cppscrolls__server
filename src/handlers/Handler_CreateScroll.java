package handlers;



import java.util.UUID;
import java.util.ArrayList;
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

import validators.Validator_CreateScroll;

import actions.Action_AddScroll;
import actions.Action_UpdateUserScoreAndScrollCreationTimeById;
import actions.Action_AddTopics;
import actions.Action_AddScroll_Topic;
import actions.Action_DeleteScroll_TopicByScrollId;

import models.Model_Scroll;
import models.Model_Topic;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
  pattern = "/scroll",
  type = "POST",
	marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_CreateScroll extends HTTPHandler
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

    CStatus status = Validator_CreateScroll.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		/*
		 * checking for SCROLL_CREATION_LIMIT
		 *\
		 * In this step, we check if a certain number
		 * of hours have passed since the current user
		 * last created the scroll. In fact, this is
		 * an artificial restriction on the creation
		 * of scrolls. This scheme is necessary to
		 * prevent spam.
		 */

		 if (preloadedUser.has(CField.SCROLL_CREATION_TIME) &&
		 	Tools.hoursPassed(preloadedUser.getString(CField.SCROLL_CREATION_TIME)) <= Const.SCROLL_CREATION_TIMEOUT)
		 {
		 	res.body(resBody
         .put(CField.STATUS, CStatus.SCROLL_CREATION_LIMIT.ordinal())
         .toString());
		 	return;
		 }



    String scroll_id = UUID.randomUUID().toString();

		SQLInjector.inject(new Action_AddScroll(
			scroll_id,
			tokenPayload.getString(CField.UID),
			preloadedUser.getInt(CField.SIDE),
			reqBody.getString(CField.TITLE),
			reqBody.getString(CField.DESCRIPTION),
			reqBody.getString(CField.SCRIPT_FUNC),
			reqBody.getString(CField.TEST_FUNC)
		));



		/*\
		 * By creating a new scroll, the user receives
		 * some points. The time of creation is also
		 * recorded.
		 */

		SQLInjector.inject(new Action_UpdateUserScoreAndScrollCreationTimeById(
			tokenPayload.getString(CField.UID),
			preloadedUser.getInt(CField.SCORE) + Const.POINTS_FOR_SCROLL_CREATING,
			new Timestamp(System.currentTimeMillis()).toString()
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
   
 
 
      SQLInjector.inject(new Action_DeleteScroll_TopicByScrollId(scroll_id));



      /*\
       * Now we add the relation of each theme_id to
       * the current scroll_id (relation that already
       * exists will be ignored).
       */

      Action_AddScroll_Topic action_addScroll_topic = new Action_AddScroll_Topic(
        scroll_id,
        preloadedUser.getInt(CField.SIDE),
        topics.length()
      );

      for (int i = 0; i < topics.length(); ++i)
        action_addScroll_topic.add(topics.getString(i));

      SQLInjector.inject(action_addScroll_topic);
    }



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.SCROLL_ID, scroll_id)
      .toString());
  }
}
