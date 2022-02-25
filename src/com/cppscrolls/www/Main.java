package com.cppscrolls.www;



import zer.http.HTTPConfig;
import zer.http.HTTPServer;
import zer.sql.SQLConfig;
import zer.mail.MAILConfig;

import constants.Server;

import middlewares.HTTPMiddleware_Auth;

import handlers.Handler_SignUp;
import handlers.Handler_Confirm;
import handlers.Handler_SignIn;
import handlers.Handler_UpdateProfile;
/////////////////////////////////////////
import handlers.Handler_Rating;
import handlers.Handler_GetUser;
import handlers.Handler_GetRandomScroll;



public class Main
{
  public static void main(String[] args)
  {
    MAILConfig.setSender(Server.EMAIL_SENDER);
    MAILConfig.setPassword(Server.EMAIL_SENDER_PASSWORD);



		SQLConfig.auth(Server.DATABASE_USER, Server.DATABASE_PASSWORD);
    SQLConfig.connect(Server.SQL_DRIVER, Server.SQL_CONNECTION_STRING);



    HTTPConfig.setPort(Server.PORT);

    HTTPServer server = new HTTPServer();

    server.addMiddleware(new HTTPMiddleware_Auth());

    server.addHandler(new Handler_SignUp());
    server.addHandler(new Handler_Confirm());
    server.addHandler(new Handler_SignIn());
    server.addHandler(new Handler_Rating());
		server.addHandler(new Handler_GetUser());
		server.addHandler(new Handler_GetRandomScroll());

    System.out.println("Server started listening on port " + HTTPConfig.getPort() + "..");
  
    server.run();
  }
}
