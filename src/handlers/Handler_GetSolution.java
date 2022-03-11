package handlers;



import java.util.ArrayList;

import org.json.JSONObject;
import org.json.JSONArray;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
import zer.sql.SQLInjector;

import constants.CStatus;
import constants.CField;
import constants.CRegex;
import constants.CServer;
import constants.CMark;

import actions.Action_GetSolutionByScrollId;

import models.Model_Scroll;
import models.Model_Topic;
import models.Model_Solution;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
	pattern = "/solution/" + CRegex.UUID,
	type = "GET",
	marks = {	CMark.WITH_AUTH_TOKEN	}
)
public class Handler_GetSolution extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res)
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();



		ArrayList<Model_Solution> solutions = SQLInjector.<Model_Solution>inject(Model_Solution.class, new Action_GetSolutionByScrollId(
			req.path(1)
		));
		
		if (solutions.size() == 0)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.SCROLL_HAS_NO_SOLUTION.ordinal())
				.toString());
			return;
		}

		Model_Solution solution = solutions.get(0);



		JSONObject solutionJSON = new JSONObject(solution, new String[] {
			CField.AUTHOR_ID,
			CField.AUTHOR_IMAGE,
			CField.SCRIPT,
			CField.TIME
		});



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.SOLUTION, solutionJSON)
			.toString());

	}
}
