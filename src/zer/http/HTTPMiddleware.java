package zer.http;



import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.http.HTTPRoute;



public abstract class HTTPMiddleware
{
  public abstract boolean process(HTTPRequest req, HTTPResponse res, HTTPRoute ann); 
}
