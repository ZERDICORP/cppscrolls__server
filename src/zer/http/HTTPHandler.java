package zer.http;



import java.sql.SQLException;



public abstract class HTTPHandler
{
  public abstract void handle(HTTPRequest req, HTTPResponse res) throws SQLException;
}
