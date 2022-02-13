package handlers;



import java.util.ArrayList;

import org.json.JSONObject;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;

import constants.Database;
import constants.Status;
import constants.Field;
import constants.Side;

import actions.SQLAction_GetBestUsers;
import actions.SQLAction_GetSides;

import models.SQLModel_User;
import models.SQLModel_Side;



@HTTPRoute
(
  pattern = "/rating",
  type = "GET",
  withAuthToken = true
)
public class HTTPHandler_Rating extends HTTPHandler
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

    ArrayList<SQLModel_Side> sides = SQLInjector.<SQLModel_Side>inject(SQLModel_Side.class, new SQLAction_GetSides());

    JSONObject darkSide = new JSONObject(sides.get(Side.DARK.ordinal()), new String[] { Field.SCORE });
    JSONObject brightSide = new JSONObject(sides.get(Side.BRIGHT.ordinal()), new String[] { Field.SCORE });



    /*
     * getting best users
     *\
     * The first element of the "bestUsers"
     * array is the best dark user, and the
     * second element is the best bright user.
     */

    ArrayList<SQLModel_User> bestUsers = SQLInjector.<SQLModel_User>inject(SQLModel_User.class, new SQLAction_GetBestUsers());

    SQLModel_User bestDarkUser = bestUsers.get(Side.DARK.ordinal());
    SQLModel_User bestBrightUser = bestUsers.get(Side.BRIGHT.ordinal());

    if (!bestDarkUser.nickname.equals(Database.DARK_ZERO_USER_NICKNAME))
      darkSide.put(Field.BEST_USER, new JSONObject(bestDarkUser, new String[] { Field.ID, Field.IMAGE }));

    if (!bestBrightUser.nickname.equals(Database.BRIGHT_ZERO_USER_NICKNAME))
      brightSide.put(Field.BEST_USER, new JSONObject(bestBrightUser, new String[] { Field.ID, Field.IMAGE }));



    res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .put(Field.DARK_SIDE, darkSide)
      .put(Field.BRIGHT_SIDE, brightSide)
      .toString());
  }
}
