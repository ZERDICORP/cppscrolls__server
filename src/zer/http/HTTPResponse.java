package zer.http;



import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;



public class HTTPResponse extends HTTPMessage
{
	{
		startLine(HTTPStatus.OK);
	}

	public void body(String s) { body = s.getBytes(); }
	public HTTPResponse status(String startLine) { this.startLine(startLine); return this; }

	public byte[] make() throws IOException
	{
		String res = new String();
		res += "HTTP/1.1 " + startLine[0] + " " + startLine[1] + "\r\n";
		if (body.length != 0)
		{
			res += "Content-Type: " + headers.get("Content-Type") + "\r\n";
			res += "Content-Length: " + body.length;
		}
		res += "\r\n\r\n";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(res.getBytes());
		baos.write(body);

		return baos.toByteArray();
	}
}
