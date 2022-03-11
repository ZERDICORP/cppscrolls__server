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

import actions.Action_ToggleBadMark;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
  pattern = "/bad_mark",
  type = "POST",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_BadMark extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

    res.headers().put("Content-Type", FType.JSON.mime());
    
    JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



    /*
     * request body validation
     */

    CStatus status = Validator_BadMark.validate(bodyAsString);
    if (status != CStatus.OK)
    {
      res.body(resBody
        .put(CField.STATUS, status.ordinal())
        .toString());
      return;
    }

    JSONObject reqBody = new JSONObject(bodyAsString);



		SQLInjector.inject(new Action_ToggleBadMark(
			reqBody.getString(CField.SCROLL_ID),
			tokenPayload.getString(CField.UID)
		));



		/*
		 * checking for SCROLL_HAS_NOT_BEEN_VISITED
		 *\
		 * If no row has been updated, it means the
		 * scroll has not been visited.
		 */

		if (SQLInjector.rowsUpdated() <= 0)
		{
			res.body(resBody
        .put(CField.STATUS, CStatus.SCROLL_HAS_NOT_BEEN_VISITED.ordinal())
        .toString());
      return;
		}



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .toString());
  }
}
