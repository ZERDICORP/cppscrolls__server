package handlers;



import java.util.UUID;
import java.util.ArrayList;
import java.sql.SQLException;
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
import zer.sql.SQLInjector;
import zer.mail.MAILClient;
import zer.exec.EXECTask;
import zer.exec.EXECResult;
import zer.exec.EXECResultCode;

import constants.CStatus;
import constants.CField;
import constants.CServer;
import constants.CMark;

import validators.Validator_SolveScroll;

import actions.Action_GetScrollById;
import actions.Action_GetSolutionByScrollId;
import actions.Action_AddSolution;

import models.Model_Scroll;
import models.Model_Topic;
import models.Model_Solution;

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

		CStatus status = Validator_SolveScroll.validate(bodyAsString);
		if (status != CStatus.OK)
		{
			res.body(resBody
				.put(CField.STATUS, status.ordinal())
				.toString());
			return;
		}

		JSONObject reqBody = new JSONObject(bodyAsString);



		ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetScrollById(
			reqBody.getString(CField.SCROLL_ID),
			tokenPayload.getString(CField.UID)
		));

		if (scrolls.size() == 0)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.SCROLL_DOES_NOT_EXIST.ordinal())
				.toString());
			return;
		}

		Model_Scroll scroll = scrolls.get(0);



		String fileName = CServer.SOLUTIONS_FOLDER_PATH + preloadedUser.getString(CField.NICKNAME);
		String base = FTool.readPlain(CServer.BASE_CPP_FILE_PATH);

		try
		{
			String solution = base.replace("%CODE%", reqBody.getString(CField.SCRIPT) + "\n\n" + scroll.test_func);

			OutputStream os = new FileOutputStream(fileName + ".cpp");
			os.write(solution.getBytes());
			os.close();
		}
		catch (IOException e) { e.printStackTrace(); }



		EXECResult compileResult = EXECTask.exec("g++ " + fileName + ".cpp -o " + fileName);
		if (compileResult.code() != EXECResultCode.OK)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.OK.ordinal())
				.put(CField.ERROR, true)
				.put(CField.OUTPUT, compileResult.message())
				.toString());
			return;
		}



		EXECResult execResult = EXECTask.exec(fileName, CServer.EXEC_TIMEOUT);
		if (execResult.code() != EXECResultCode.OK)
		{
			res.body(resBody
				.put(CField.STATUS, CStatus.OK.ordinal())
				.put(CField.ERROR, true)
				.put(CField.OUTPUT, execResult.message())
				.toString());
			return;
		}



		ArrayList<Model_Solution> solutions = SQLInjector.<Model_Solution>inject(Model_Solution.class, new Action_GetSolutionByScrollId(
			reqBody.getString(CField.SCROLL_ID)
		));
		
		boolean bestSolution = solutions.size() == 0 || solutions.get(0).time - execResult.time() >= CServer.BEST_SOLUTION_TIME_DIFF;

		if (bestSolution)
			SQLInjector.inject(new Action_AddSolution(
				reqBody.getString(CField.SCROLL_ID),
				tokenPayload.getString(CField.UID),
				reqBody.getString(CField.SCRIPT),
				(int) execResult.time()
			));



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.ERROR, false)
			.put(CField.BEST_SOLUTION, bestSolution)
			.put(CField.OUTPUT, execResult.message())
			.put(CField.TIME, execResult.time())
			.toString());

	}
}
