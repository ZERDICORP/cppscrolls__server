package zer.http;



import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;



public class HTTPClient extends HTTPConfig
{
	public static HTTPResponse send(HTTPRequest req)
	{
		HTTPResponse res = new HTTPResponse();
		try
		{
			Socket socket = new Socket(host, port);
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

			outputStream.write(req.make());
			outputStream.flush();

			ByteArrayOutputStream result = new ByteArrayOutputStream();

			byte[] response = new byte[100000];
			int responseLength = inputStream.read(response);
			result.write(response, 0, responseLength);

			res.parse(new String(response, 0, responseLength, StandardCharsets.UTF_8));
			int trueLength = Integer.parseInt(res.get("Content-Length"));

			for (; result.size() < trueLength;)
			{
				responseLength = inputStream.read(response);
				result.write(response, 0, responseLength);
			}

			res.parse(result.toString("UTF-8"));
		}
		catch (Exception e) {e.printStackTrace();}

		return res;
	}
}
