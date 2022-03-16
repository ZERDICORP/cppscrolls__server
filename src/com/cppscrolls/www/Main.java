package com.cppscrolls.www;



import java.io.IOException;

import zer.mail.MAILConfig;
import zer.sql.SQLManager;
import zer.http.HTTPConfig;
import zer.http.HTTPServer;

import tools.ConfigLoader;

import configs.AppConfig;

import middlewares.Middleware_Auth;

import handlers.Handler_SignUp;
import handlers.Handler_Confirm;
import handlers.Handler_SignIn;
import handlers.Handler_UpdateNickname;
import handlers.Handler_UpdateBio;
import handlers.Handler_UpdatePassword;
import handlers.Handler_UpdateImage;
import handlers.Handler_GetUser;
import handlers.Handler_DeleteAccount;
import handlers.Handler_Rating;
import handlers.Handler_GetRandomScroll;
import handlers.Handler_CreateScroll;
import handlers.Handler_UpdateScroll;
import handlers.Handler_DeleteScroll;
import handlers.Handler_BadMark;
import handlers.Handler_GetScroll;
import handlers.Handler_GetTopics;
import handlers.Handler_GetMatchedTopics;
import handlers.Handler_GetScrollsByTopic;
import handlers.Handler_GetUserScrolls;
import handlers.Handler_GetHistory;
import handlers.Handler_SolveScroll;



public class Main
{
  public static void main(String[] args)
  {
		ConfigLoader.load(AppConfig.class, "resources/app.cfg");



    MAILConfig.setSender(AppConfig.EMAIL_SENDER);
    MAILConfig.setPassword(AppConfig.EMAIL_SENDER_PASSWORD);



		SQLManager.auth(AppConfig.DATABASE_USER, AppConfig.DATABASE_PASSWORD);
    SQLManager.connect(AppConfig.SQL_DRIVER, AppConfig.SQL_CONNECTION_STRING);



		HTTPConfig.apiPrefix(AppConfig.API_PREFIX);

    HTTPServer server = new HTTPServer();

    server.addMiddleware(new Middleware_Auth());

    server.addHandler(new Handler_SignUp());
    server.addHandler(new Handler_Confirm());
    server.addHandler(new Handler_SignIn());
		server.addHandler(new Handler_UpdateNickname());
		server.addHandler(new Handler_UpdateBio());
		server.addHandler(new Handler_UpdatePassword());
		server.addHandler(new Handler_UpdateImage());
		server.addHandler(new Handler_GetUser());
		server.addHandler(new Handler_DeleteAccount());
		server.addHandler(new Handler_Rating());
		server.addHandler(new Handler_GetRandomScroll());
		server.addHandler(new Handler_CreateScroll());
		server.addHandler(new Handler_UpdateScroll());
		server.addHandler(new Handler_DeleteScroll());
		server.addHandler(new Handler_BadMark());
		server.addHandler(new Handler_GetScroll());
		server.addHandler(new Handler_GetTopics());
		server.addHandler(new Handler_GetMatchedTopics());
		server.addHandler(new Handler_GetScrollsByTopic());
		server.addHandler(new Handler_GetUserScrolls());
		server.addHandler(new Handler_GetHistory());
		server.addHandler(new Handler_SolveScroll());
 		
		try
		{
			System.out.println("Server started listening on port " + HTTPConfig.port() + "..");
			server.run();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
  }
}
