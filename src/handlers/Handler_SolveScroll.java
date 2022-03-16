package handlers;



import java.sql.SQLException;
import java.util.ArrayList;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;

import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.file.FType;
import zer.file.FTool;
import zer.exec.EXECTask;
import zer.exec.EXECResult;
import zer.exec.EXECResultCode;

import configs.AppConfig;

import constants.CStatus;
import constants.CField;
import constants.Const;
import constants.CMark;

import validators.Validator_SolveScroll;

import actions.Action_GetScrollById;
import actions.Action_UpdateUserScore;
import actions.Action_UpdateSolution;
import actions.Action_UpdateUserScore;
import actions.Action_IncScrollSuccessfulAttempts;
import actions.Action_IncScrollUnsuccessfulAttempts;

import models.Model_Scroll;
import models.Model_Topic;

import tools.Tools;
import tools.Token;



@HTTPRoute
(
	pattern = "/solve",
	type = "POST",
	marks = {
		CMark.WITH_AUTH_TOKEN,
		CMark.WITH_PRELOADED_USER
	}
)
public class Handler_SolveScroll extends HTTPHandler
{
	@Override
	public void handle(HTTPRequest req, HTTPResponse res) throws SQLException
	{
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));
		JSONObject preloadedUser = new JSONObject(req.headers().get("Preloaded-User"));

		res.headers().put("Content-Type", FType.JSON.mime());
		
		JSONObject resBody = new JSONObject();

		String bodyAsString = req.bodyAsString();



		CStatus status = Validator_SolveScroll.validate(bodyAsString);
		if (status != CStatus.OK)
		{
			res.body(resBody
				.put(CField.STATUS, status.ordinal())
				.toString());
			return;
		}

		JSONObject reqBody = new JSONObject(bodyAsString);



		ArrayList<Model_Scroll> scrolls = new Action_GetScrollById(
			tokenPayload.getString(CField.UID),
			reqBody.getString(CField.SCROLL_ID)
		).result();

		if (scrolls.size() == 0)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.SCROLL_DOES_NOT_EXIST.ordinal())
				.toString());
			return;
		}

		Model_Scroll scroll = scrolls.get(0);



		String solutionFilePath = AppConfig.SOLUTIONS_FOLDER_PATH + preloadedUser.getString(CField.NICKNAME) + ".cpp";

		try
		{
			OutputStream os = new FileOutputStream(solutionFilePath);
			os.write((scroll.test_func + "\n\n" + reqBody.getString(CField.SOLUTION)).getBytes());
			os.close();
		}
		catch (IOException e) { e.printStackTrace(); }



		EXECResult execResult = EXECTask.exec(
			Tools.replaceParams(
				AppConfig.DOCKER_RUN_COMMAND,
				new String[] { System.getProperty("user.dir") + "/" + solutionFilePath }
			),
			Const.EXEC_TIMEOUT
		);

		FTool.delete(solutionFilePath);

		if (execResult.code() != EXECResultCode.OK)
		{
			new Action_IncScrollUnsuccessfulAttempts(scroll.id);

			res.body(resBody
				.put(CField.STATUS, CStatus.OK.ordinal())
				.put(CField.ERROR, true)
				.put(CField.OUTPUT, execResult.message())
				.toString());
			return;
		}


	
		if (scroll.solution == null)
			new Action_UpdateUserScore(
				tokenPayload.getString(CField.UID),
				preloadedUser.getInt(CField.SCORE) + Const.POINTS_FOR_SOLVING_SCROLL
			);



		if
		(
		 	scroll.solution == null ||
			!reqBody.getString(CField.SOLUTION).equals(scroll.solution.toString())
		)
			new Action_UpdateSolution(
				scroll.id,
				tokenPayload.getString(CField.UID),
				reqBody.getString(CField.SOLUTION)
			);



		new Action_IncScrollSuccessfulAttempts(scroll.id);



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.ERROR, false)
			.put(CField.OUTPUT, execResult.message())
			.put(CField.TIME, execResult.time())
			.toString());
	}
}
