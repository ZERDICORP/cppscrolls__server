package handlers;
  
  
  
import java.util.ArrayList;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;
 
import constants.CStatus;
import constants.CField;
import constants.CRegex;
import constants.CMark;
 
import actions.Action_GetRandomScroll;
 
import models.Model_Scroll;
 
 
 
@HTTPRoute
(
  pattern = "/" + CRegex.SIDE + "/scroll",
  type = "GET",
  marks = { CMark.WITH_AUTH_TOKEN }
)
public class Handler_GetRandomScroll extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {
		JSONObject tokenPayload = new JSONObject(req.headers().get("Authentication-Token-Payload"));

    res.headers().put("Content-Type", FType.JSON.mime());
   
    JSONObject resBody = new JSONObject();



    ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetRandomScroll(
			tokenPayload.getString(CField.UID),
			req.pathInt(0)
		));

		if (scrolls.size() == 0)
    {   
      res.body(resBody
        .put(CField.STATUS, CStatus.NO_SCROLLS_ON_YOUR_SIDE.ordinal())
        .toString());
      return;
    }   
   
    Model_Scroll scroll = scrolls.get(0);



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.SCROLL_ID, scroll.id)
      .toString());
  }
}
