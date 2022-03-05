package com.cppscrolls.www;



import zer.http.HTTPConfig;
import zer.http.HTTPServer;
import zer.sql.SQLConfig;
import zer.mail.MAILConfig;

import constants.CServer;

import middlewares.Middleware_Auth;

import handlers.Handler_SignUp;
import handlers.Handler_Confirm;
import handlers.Handler_SignIn;
import handlers.Handler_UpdateNickname;
import handlers.Handler_UpdateBio;
import handlers.Handler_UpdatePassword;
import handlers.Handler_UpdateImage;



public class Main
{
  public static void main(String[] args)
  {
    MAILConfig.setSender(CServer.EMAIL_SENDER);
    MAILConfig.setPassword(CServer.EMAIL_SENDER_PASSWORD);



		SQLConfig.auth(CServer.DATABASE_USER, CServer.DATABASE_PASSWORD);
    SQLConfig.connect(CServer.SQL_DRIVER, CServer.SQL_CONNECTION_STRING);



    HTTPConfig.setPort(CServer.PORT);

    HTTPServer server = new HTTPServer();

    server.addMiddleware(new Middleware_Auth());

    server.addHandler(new Handler_SignUp());
    server.addHandler(new Handler_Confirm());
    server.addHandler(new Handler_SignIn());
		server.addHandler(new Handler_UpdateNickname());
		server.addHandler(new Handler_UpdateBio());
		server.addHandler(new Handler_UpdatePassword());
		server.addHandler(new Handler_UpdateImage());

    System.out.println("Server started listening on port " + HTTPConfig.getPort() + "..");
  
    server.run();
  }
}
