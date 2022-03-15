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

import validators.Validator_BadMark;

import actions.Action_GetScrollById;
import actions.Action_UpdateUserScore;
import actions.Action_ToggleScrollBadReputation;
import actions.Action_ToggleBadMark;

import models.Model_Scroll;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
  pattern = "/bad_mark",
  type = "POST",
	marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_BadMark extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

    res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    CStatus status = Validator_BadMark.validate(bodyAsString);
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
			res.body(resBody
        .put(CField.STATUS, CStatus.SCROLL_HAS_NOT_BEEN_VISITED.ordinal())
        .toString());
      return;
		}



		/*\
		 * If we didn't set a bad mark, the scroll
		 * has a good reputation, and by setting
		 * a bad mark, the reputation will become
		 * bad - we should switch the scroll's
		 * bad_reputation to true and punish the
		 * scroll's author by deducting
		 * POINTS_LOSS_FOR_BAD_SCROLL from his points.
		 */
			
		if (
			scroll.bad_mark == 0 &&
			scroll.bad_reputation == 0 &&
			((scroll.bad_marks + 1) / ((float) scroll.views / 100)) > 50
		)
		{
			SQLInjector.inject(new Action_UpdateUserScore(
				tokenPayload.getString(CField.UID),
				preloadedUser.getInt(CField.SCORE) - Const.POINTS_LOSS_FOR_BAD_SCROLL
			));

			SQLInjector.inject(new Action_ToggleScrollBadReputation(
				reqBody.getString(CField.SCROLL_ID)
			));
		}



		/*\
		 * The same as in the previous case,
		 * only in reverse.
		 */

		if (
			scroll.bad_mark == 1 &&
			scroll.bad_reputation == 1 &&
			((scroll.bad_marks - 1) / ((float) scroll.views / 100)) <= 50
		)
		{
			SQLInjector.inject(new Action_UpdateUserScore(
				tokenPayload.getString(CField.UID),
				preloadedUser.getInt(CField.SCORE) + Const.POINTS_LOSS_FOR_BAD_SCROLL
			));

			SQLInjector.inject(new Action_ToggleScrollBadReputation(
				reqBody.getString(CField.SCROLL_ID)
			));
		}



		SQLInjector.inject(new Action_ToggleBadMark(
			reqBody.getString(CField.SCROLL_ID),
			tokenPayload.getString(CField.UID)
		));



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
  }
}
