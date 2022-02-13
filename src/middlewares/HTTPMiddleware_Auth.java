package middlewares;



import org.json.JSONObject;

import zer.http.HTTPMiddleware;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.http.HTTPRoute;

import constants.Status;
import constants.Field;
import constants.Server;

import tools.Token;



public class HTTPMiddleware_Auth extends HTTPMiddleware
{
  @Override
  public boolean process(HTTPRequest req, HTTPResponse res, HTTPRoute ann)
  {
    /*\
     * The handler that owns this annotation doesn't
     * need token authentication, so i'm not intereste
     * in that, i'm skipping
     */

    if (!ann.withAuthToken())
      return true;

    /*
     * checking for INVALID_TOKEN
     */

    String payload = Token.access(req.get("Authentication-Token"), Server.SECRET);
    if (payload == null)
    {   
      res.setBody(new JSONObject()
        .put(Field.STATUS, Status.INVALID_TOKEN.ordinal())
        .toString());
      return false;
    }   

    req.set("Authentication-Token-Payload", payload);

    return true;
  }
}
