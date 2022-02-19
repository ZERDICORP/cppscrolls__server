package handlers;



import java.util.ArrayList;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import constants.Status;
import constants.Field;
import constants.Side;

import actions.Action_GetBestUsers;
import actions.Action_GetSides;

import models.Model_User;
import models.Model_Side;



@HTTPRoute
(
  pattern = "/rating",
  type = "GET",
  withAuthToken = true
)
public class Handler_Rating extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
    res.set("Content-Type", FType.JSON.mime());
    
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

    JSONObject darkSide = new JSONObject(sides.get(Side.DARK.ordinal()), new String[] { Field.SCORE });
    JSONObject brightSide = new JSONObject(sides.get(Side.BRIGHT.ordinal()), new String[] { Field.SCORE });



    /*
     * getting best users
     *\
     * The first element of the "bestUsers"
     * array is the best dark user, and the
     * second element is the best bright user.
     */

    ArrayList<Model_User> bestUsers = SQLInjector.<Model_User>inject(Model_User.class, new Action_GetBestUsers());

    Model_User bestDarkUser = bestUsers.get(Side.DARK.ordinal());
    Model_User bestBrightUser = bestUsers.get(Side.BRIGHT.ordinal());

    if (!bestDarkUser.id.equals(String.valueOf(Side.DARK.ordinal())))
      darkSide.put(Field.BEST_USER, new JSONObject(bestDarkUser, new String[] { Field.ID, Field.IMAGE }));

    if (!bestBrightUser.id.equals(String.valueOf(Side.BRIGHT.ordinal())))
      brightSide.put(Field.BEST_USER, new JSONObject(bestBrightUser, new String[] { Field.ID, Field.IMAGE }));



    res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .put(Field.DARK_SIDE, darkSide)
      .put(Field.BRIGHT_SIDE, brightSide)
      .toString());
  }
}
