package zer.http;



import java.io.IOException;



public abstract class HTTPHandler
{
  public abstract void handle(HTTPRequest req, HTTPResponse res);
}
