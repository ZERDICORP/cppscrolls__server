package handlers;
  
  
  
import java.util.ArrayList;
  
import org.json.JSONObject;
  
import zer.http.HTTPHandler;
import zer.http.HTTPRoute;
import zer.http.HTTPRequest;
import zer.http.HTTPResponse;
import zer.sql.SQLInjector;
import zer.file.FType;
 
import constants.Status;
import constants.Field;
 
import actions.Action_GetRandomScroll;
 
import models.Model_Scroll;
 
 
 
@HTTPRoute
(
  pattern = "/scroll",
  type = "GET",
  withAuthToken = true
)
public class Handler_GetRandomScroll extends HTTPHandler
{
  @Override
  public void handle(HTTPRequest req, HTTPResponse res)
  {   
    res.set("Content-Type", FType.JSON.mime());
   
    JSONObject resBody = new JSONObject();
 	
		/*\
		 * We will not check if the scrolls
		 * array is empty, as there will always
		 * be at least 1 scroll in the base.
		 */

    ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetRandomScroll());

    JSONObject scroll = new JSONObject(scrolls.get(0), new String[] {"id", "title", "description", "script_func"});

    res.setBody(resBody
      .put(Field.STATUS, Status.OK.ordinal())
      .put(Field.SCROLL, scroll)
      .toString());
  }
}
