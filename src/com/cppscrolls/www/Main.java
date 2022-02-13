package com.cppscrolls.www;



import zer.http.HTTPConfig;
import zer.http.HTTPServer;
import zer.sql.SQLConfig;
import zer.mail.MAILConfig;

import constants.Server;

import middlewares.HTTPMiddleware_Auth;

import handlers.HTTPHandler_SignUp;
import handlers.HTTPHandler_Confirm;
import handlers.HTTPHandler_SignIn;
import handlers.HTTPHandler_Rating;



public class Main
{
  public static void main(String[] args)
  {
    MAILConfig.setSender(Server.EMAIL_SENDER);
    MAILConfig.setPassword(Server.EMAIL_SENDER_PASSWORD);



    SQLConfig.setDatabase(Server.DATABASE_NAME);
    SQLConfig.setPassword(Server.DATABASE_PASSWORD);
    SQLConfig.connect();



    HTTPConfig.setPort(Server.PORT);

    HTTPServer server = new HTTPServer();

    server.addMiddleware(new HTTPMiddleware_Auth());

    server.addHandler(new HTTPHandler_SignUp());
    server.addHandler(new HTTPHandler_Confirm());
    server.addHandler(new HTTPHandler_SignIn());
    server.addHandler(new HTTPHandler_Rating());

    System.out.println("Server started listening on port " + HTTPConfig.getPort() + "..");
  
    server.run();
  }
}
