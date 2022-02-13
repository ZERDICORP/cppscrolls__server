package zer.http;



import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class HTTPRequest
{
	private HashMap<String, String> fields = new HashMap<>();
	private byte[] body = new byte[0];

	{
		fields.put("Type", "GET");
		fields.put("Path", "/");
	}

	public String get(String key) { return this.fields.get(key); }
	public HTTPRequest set(String key, String sValue) { this.fields.put(key, sValue); return this; }
	
	public void setBody(String body) { this.body = body.getBytes(); }
	public void setBody(byte[] body) { this.body = body; }
	
	public boolean bodyIsEmpty() { return this.body.length == 0; }
	public boolean parse(String request) throws Exception
	{
		String[] parts = request.split("\r\n\r\n");

		if (parts.length == 0)
			return false;

		ArrayList<String> lines = new ArrayList<>(Arrays.asList(parts[0].split("\r\n")));
		
		String[] statusLine = lines.remove(0).split(" ");

		if (statusLine.length != 3)
			return false;

		this.fields.put("Type", statusLine[0]);
		this.fields.put("Path", statusLine[1]);

		for (String line : lines)
		{
			String[] temp = line.split(": ");
			this.fields.put(temp[0], temp[1]);
		}

		if (parts.length > 1)
			fields.put("Body", parts[1]);

		return true;
	}

	public byte[] make() throws IOException
	{
		String requestString = new String();
		requestString += this.fields.get("Type") + " " + this.fields.get("Path") + " HTTP/1.1\r\n";
		if (this.body.length != 0)
		{
			requestString += "Connection-Type: " + this.fields.get("Content-Type") + "\r\n";
			requestString += "Content-Length: " + this.body.length;
		}
		requestString += "\r\n\r\n";

		ByteArrayOutputStream requestStream = new ByteArrayOutputStream();
		requestStream.write(requestString.getBytes());
		requestStream.write(this.body);

		return requestStream.toByteArray();
	}
}
