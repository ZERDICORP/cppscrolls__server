package zer.http;



import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import constants.Server;



public class HTTPRequest
{
	private HashMap<String, String> fields = new HashMap<>();
	private ArrayList<String> path;
	private byte[] body = new byte[0];

	{
		fields.put("Type", "GET");
		fields.put("Path", "/");
	}
	
	public String path(int index) { return path.get(index + Server.API_PREFIX.split("/").length); }
	public String get(String key) { return fields.get(key); }
	public HTTPRequest set(String key, String sValue) { fields.put(key, sValue); return this; }
	
	public void setBody(String b) { body = b.getBytes(); }
	public void setBody(byte[] b) { body = b; }
	
	public boolean bodyIsEmpty() { return body.length == 0; }
	public boolean parse(String request) throws Exception
	{
		String[] parts = request.split("\r\n\r\n");

		if (parts.length == 0)
			return false;

		ArrayList<String> lines = new ArrayList<>(Arrays.asList(parts[0].split("\r\n")));
		
		String[] statusLine = lines.remove(0).split(" ");

		if (statusLine.length != 3)
			return false;

		fields.put("Type", statusLine[0]);
		fields.put("Path", statusLine[1]);

		for (String line : lines)
		{
			String[] temp = line.split(": ");
			fields.put(temp[0], temp[1]);
		}

		if (parts.length > 1)
			fields.put("Body", parts[1]);

		path = new ArrayList<>(Arrays.asList(fields.get("Path").split("/")));

		return true;
	}

	public byte[] make() throws IOException
	{
		String requestString = new String();
		requestString += fields.get("Type") + " " + fields.get("Path") + " HTTP/1.1\r\n";
		if (body.length != 0)
		{
			requestString += "Connection-Type: " + fields.get("Content-Type") + "\r\n";
			requestString += "Content-Length: " + body.length;
		}
		requestString += "\r\n\r\n";

		ByteArrayOutputStream requestStream = new ByteArrayOutputStream();
		requestStream.write(requestString.getBytes());
		requestStream.write(body);

		return requestStream.toByteArray();
	}
}
