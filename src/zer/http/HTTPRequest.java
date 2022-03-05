package zer.http;



import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;



public class HTTPRequest extends HTTPMessage
{
	private ArrayList<String> splitPath;
	
	public String bodyAsString() { return new String(body, StandardCharsets.UTF_8); }
	public String type() { return startLine[0]; }
	public String path() { return startLine[1]; }
	public String path(int index) { return splitPath.get(index); }

	public boolean parseHeaders(String plain)
	{
		ArrayList<String> lines = new ArrayList<>(Arrays.asList(plain.split("\r\n")));
  


    /*
     * parse start line
     */
     
		startLine(lines.get(0));
		if (startLine.length != 3)
      return false;

		splitPath = new ArrayList<>(Arrays.asList(path().split("/")));



    /*
     * parse headers
     */

		for (int i = 1; i < lines.size(); ++i)
		{
			String[] header = lines.get(i).split(": ");
			if (header.length != 2)
				continue;

			headers.put(header[0], header[1]);
		}

		return true;
	}
}
