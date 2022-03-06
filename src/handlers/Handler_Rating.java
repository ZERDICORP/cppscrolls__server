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

import actions.Action_GetBestUsers;
import actions.Action_GetSides;

import models.Model_User;
import models.Model_Side;



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


    
    /*
     * getting a sides
     *\
     * The sides are created from the users
     * table. In a database, we always have
     * two zero users: a dark zero user and a
     * bright zero user. This is the reason
     * why we will always get two sides from
     * query below. And since the output is
     * sorted by the "side" column (from
     * smallest to largest), the first
     * element of the "sides" array is the
     * dark side (0), and the second element
     * is the bright side (1).
     */

    ArrayList<Model_Side> sides = SQLInjector.<Model_Side>inject(Model_Side.class, new Action_GetSides());

    JSONObject darkSide = new JSONObject(sides.get(CSide.DARK.ordinal()), new String[] { CField.SCORE });
    JSONObject brightSide = new JSONObject(sides.get(CSide.BRIGHT.ordinal()), new String[] { CField.SCORE });



    /*
     * getting best users
     *\
     * The first element of the "bestUsers"
     * array is the best dark user, and the
     * second element is the best bright user.
     */

    ArrayList<Model_User> bestUsers = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetBestUsers());

    Model_User bestDarkUser = bestUsers.get(CSide.DARK.ordinal());
    Model_User bestBrightUser = bestUsers.get(CSide.BRIGHT.ordinal());

    if (!bestDarkUser.id.equals(String.valueOf(CSide.DARK.ordinal())))
      darkSide.put(CField.BEST_USER, new JSONObject(bestDarkUser, new String[] { CField.ID, CField.IMAGE }));

    if (!bestBrightUser.id.equals(String.valueOf(CSide.BRIGHT.ordinal())))
      brightSide.put(CField.BEST_USER, new JSONObject(bestBrightUser, new String[] { CField.ID, CField.IMAGE }));



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.DARK_SIDE, darkSide)
      .put(CField.BRIGHT_SIDE, brightSide)
      .toString());
  }
}
