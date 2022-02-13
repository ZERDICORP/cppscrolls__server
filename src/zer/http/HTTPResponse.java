package zer.http;



import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class HTTPResponse
{
	private HashMap<String, String> fields = new HashMap<>();
	private byte[] body = new byte[0];

	{
		fields.put("Code", "200");
		fields.put("Word", "OK");
	}

	public Boolean bodyIsEmpty() { return this.body.length == 0; }
	public String get(String key) { return this.fields.get(key); }
	public HTTPResponse set(String key, String sValue) { this.fields.put(key, sValue); return this; }
	
	public void setBody(String body) { this.body = body.getBytes(); }
	public void setBody(byte[] body) { this.body = body; }
	public void parse(String request) throws Exception
	{
		String[] parts = request.split("\r\n\r\n");

		ArrayList<String> lines = new ArrayList<>(Arrays.asList(parts[0].split("\r\n")));
		
		String[] statusLine = lines.remove(0).split(" ");

		this.fields.put("Code", statusLine[1]);
		this.fields.put("Word", statusLine[2]);

		for (String line : lines)
		{
			String[] temp = line.split(": ");
			this.fields.put(temp[0], temp[1]);
		}

		if (parts.length > 1)
			fields.put("Body", parts[1]);
	}

	public byte[] make() throws IOException
	{
		String responseString = new String();
		responseString += "HTTP/1.1 " + this.fields.get("Code") + " " + this.fields.get("Word") + "\r\n";
		if (this.body.length != 0)
		{
			responseString += "Connection-Type: " + this.fields.get("Content-Type") + "\r\n";
			responseString += "Content-Length: " + this.body.length;
		}
		responseString += "\r\n\r\n";

		ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
		responseStream.write(responseString.getBytes());
		responseStream.write(this.body);

		return responseStream.toByteArray();
	}
}
