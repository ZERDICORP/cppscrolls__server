package middlewares;



import org.json.JSONObject;

import zer.http.HTTPMiddleware;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.http.HTTPRoute;

import constants.CStatus;
import constants.CField;
import constants.CServer;

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

    String payload = Token.access(req.headers().get("Authentication-Token"), CServer.SECRET);
    if (payload == null)
    {   
      res.body(new JSONObject()
        .put(CField.STATUS, CStatus.INVALID_TOKEN.ordinal())
        .toString());
      return false;
    }   

    req.headers().put("Authentication-Token-Payload", payload);

    return true;
  }
}
