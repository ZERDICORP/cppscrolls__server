package handlers;



import java.util.UUID;
import java.util.ArrayList;
import java.sql.SQLException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

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

import configs.AppConfig;

import constants.CStatus;
import constants.CField;
import constants.Const;
import constants.CMark;

import validators.Validator_SolveScroll;

import actions.Action_GetScrollById;
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



		String solutionFolderPath = AppConfig.SOLUTIONS_FOLDER_PATH + preloadedUser.getString(CField.NICKNAME);

		File solutionFolder = new File(solutionFolderPath);
		solutionFolder.mkdir();

		try
		{
			OutputStream os = new FileOutputStream(solutionFolderPath + "/main.cpp");
			os.write((reqBody.getString(CField.SCRIPT) + "\n\n" + scroll.test_func).getBytes());
			os.close();
		}
		catch (IOException e) { e.printStackTrace(); }



		EXECResult execResult = EXECTask.exec(
			"docker run --rm --volume " + System.getProperty("user.dir") + "/" + solutionFolderPath + "/main.cpp:/workspace/main.cpp solution",
			Const.EXEC_TIMEOUT
		);

		FTool.deleteFolder(solutionFolderPath);

		if (execResult.code() != EXECResultCode.OK)
		{
			SQLInjector.inject(new Action_IncScrollUnsuccessfulAttempts(scroll.id));

			res.body(resBody
				.put(CField.STATUS, CStatus.OK.ordinal())
				.put(CField.ERROR, true)
				.put(CField.OUTPUT, execResult.message())
				.toString());
			return;
		}



		SQLInjector.inject(new Action_IncScrollSuccessfulAttempts(scroll.id));



		res.body(resBody
			.put(CField.STATUS, CStatus.OK.ordinal())
			.put(CField.ERROR, false)
			.put(CField.OUTPUT, execResult.message())
			.put(CField.TIME, execResult.time())
			.toString());

	}
}
