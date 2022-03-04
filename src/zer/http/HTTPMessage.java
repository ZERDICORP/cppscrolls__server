package zer.http;



import java.util.HashMap;



class HTTPMessage
{
	protected String[] startLine;
	protected HashMap<String, String> headers;
  protected byte[] body;

	{
		startLine = null;
		headers = new HashMap<>();
		body = new byte[0];
	}

	public HashMap<String, String> headers() { return headers; }
	public byte[] body() { return body; }

	public void body(byte[] body) { this.body = body; }
	public void startLine(String startLine) { this.startLine = startLine.split(" "); }
}
