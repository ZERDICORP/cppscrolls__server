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
import constants.CSide;
import constants.CMark;

import actions.Action_GetRatingOfSide;

import models.Model_Rating;



@HTTPRoute
(
  pattern = "/rating",
  type = "GET",
  marks = {	CMark.WITH_AUTH_TOKEN }
)
public class Handler_Rating extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
    res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();
		
		JSONObject darkSideJSON = new JSONObject();
		darkSideJSON.put(CField.SCORE, 0);
		
		JSONObject brightSideJSON = new JSONObject();
		brightSideJSON.put(CField.SCORE, 0);



    ArrayList<Model_Rating> darkSideRating = SQLInjector.<Model_Rating>inject(
			Model_Rating.class,
			new Action_GetRatingOfSide(CSide.DARK.ordinal())
		);

		if (darkSideRating.size() > 0)
			darkSideJSON = new JSONObject(darkSideRating.get(0), new String[] {
				CField.SCORE,
				CField.BEST_USER_ID,
				CField.BEST_USER_IMAGE
			});



		ArrayList<Model_Rating> brightSideRating = SQLInjector.<Model_Rating>inject(
			Model_Rating.class,
			new Action_GetRatingOfSide(CSide.BRIGHT.ordinal())
		);

		if (brightSideRating.size() > 0)
			brightSideJSON = new JSONObject(brightSideRating.get(0), new String[] {
				CField.SCORE,
				CField.BEST_USER_ID,
				CField.BEST_USER_IMAGE
			});



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.DARK_SIDE, darkSideJSON)
      .put(CField.BRIGHT_SIDE, brightSideJSON)
      .toString());
  }
}
