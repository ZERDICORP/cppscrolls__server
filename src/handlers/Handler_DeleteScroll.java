package handlers;



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
import constants.Const;
import constants.CMark;

import validators.Validator_DeleteScroll;

import actions.Action_DeleteScrollByScrollAndUserId;
import actions.Action_DeleteScroll_TopicByScrollId;
import actions.Action_DeleteUniqueScrollVisitsByScrollId;
import actions.Action_UpdateUserScore;

import models.Model_Scroll;
import models.Model_Topic;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
  pattern = "/scroll",
  type = "DELETE",
	marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_DeleteScroll extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
  {
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

    res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    CStatus status = Validator_DeleteScroll.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		/*
		 * checking for SCROLL_DOES_NOT_EXIST
		 *\
		 * Here we are deleting a row from the
		 * "scrolls" table by scroll_id and user_id
		 * and then returning it. If no row is
		 * returned, it means that the scroll with
		 * the specified scroll_id does not exist,
		 * OR the author_id column of the found
		 * scroll does not match the user_id passed.
		 */

		if
		(
			new Action_DeleteScrollByScrollAndUserId(
				reqBody.getString(CField.SCROLL_ID),
				tokenPayload.getString(CField.UID)
			).updated == 0
		)
		{
			res.body(resBody
        .put(CField.STATUS, CStatus.SCROLL_DOES_NOT_EXIST.ordinal())
        .toString());
      return;
		}



		/*
		 * In addition to the scroll, we need to
		 * delete all records in the scroll_topic
		 * table whose scroll_id column matches
		 * the id of the removed scroll.
		 */

		new Action_DeleteScroll_TopicByScrollId(reqBody.getString(CField.SCROLL_ID));



		/*\
		 * Here we remove all unique_scroll_visit
		 * rows where the scroll_id column matches
		 * the specified scroll_id.
		 */

		new Action_DeleteUniqueScrollVisitsByScrollId(reqBody.getString(CField.SCROLL_ID));



		new Action_UpdateUserScore(
			tokenPayload.getString(CField.UID),
			preloadedUser.getInt(CField.SCORE) - Const.POINTS_FOR_SCROLL_CREATING
		);



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
  }
}
