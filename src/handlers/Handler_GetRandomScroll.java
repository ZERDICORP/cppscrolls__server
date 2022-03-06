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
    res.headers().put("Content-Type", FType.JSON.mime());
   
    JSONObject resBody = new JSONObject();



		/*\
		 * We will not check if the scrolls
		 * array is empty, as there will always
		 * be at least 1 scroll in the database.
		 */

    ArrayList<Model_Scroll> scrolls = SQLInjector.<Model_Scroll>inject(Model_Scroll.class, new Action_GetRandomScroll(Integer.parseInt(req.path(0))));



    res.body(resBody
      .put(CField.STATUS, CStatus.OK.ordinal())
      .put(CField.SCROLL_ID, scrolls.get(0).id)
      .toString());
  }
}
