package zer.http;



import java.net.Socket;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;



class SocketProcessor extends HTTPConfig implements Runnable
{
	private Socket socket;
	private DataInputStream inStream;
	private DataOutputStream outStream;
	private ArrayList<HTTPHandler> handlers;
  private ArrayList<HTTPMiddleware> middlewares; 
	private byte[] firstSegmentBuffer;
	private byte[] bodyBuffer;

	{
		firstSegmentBuffer = new byte[8000];
		bodyBuffer = null;
	}

	public SocketProcessor(Socket socket, ArrayList<HTTPHandler> handlers, ArrayList<HTTPMiddleware> middlewares) throws IOException
	{
		this.socket = socket;

		inStream = new DataInputStream(socket.getInputStream());
		outStream = new DataOutputStream(socket.getOutputStream());

		this.handlers = handlers;
    this.middlewares = middlewares;
	}

	public HTTPResponse process() throws IOException, SQLException
	{
		HTTPResponse res = new HTTPResponse();



		/*
		 * receiving first segment
		 */

		int firstSegmentSize = inStream.read(firstSegmentBuffer);
		if (firstSegmentSize == -1)
			return null;
		


		/*
		 * parse headers
		 */

		int headersSize = Tools.getHeadersSize(firstSegmentBuffer, firstSegmentSize);



		HTTPRequest req = new HTTPRequest();
		if (!req.parseHeaders(new String(firstSegmentBuffer, 0, headersSize, StandardCharsets.UTF_8)))
			return null;



		/*
		 * if content-length exists, we need to fill bodyBuffer
		 */
		
		if (req.headers().get("Content-Length") != null)
		{
			/*
			 * validate bodySize
			 */

			int bodySize = Integer.parseInt(req.headers().get("Content-Length"));
			if (bodySize > 265000)
				return res.status(HTTPStatus.PAYLOAD_TOO_LARGE);

			bodyBuffer = new byte[bodySize];



			/*
			 * copying body bytes from firstSegmentBuffer to bodyBuffer
			 */
			
			int bodyBytesLengthInFirstSegment = firstSegmentSize - (headersSize + 2);
			for (int i = 0; i < bodyBytesLengthInFirstSegment && i < bodyBuffer.length; ++i)
					bodyBuffer[i] = firstSegmentBuffer[i + (headersSize + 2)];



			/*
			 * receiving remaining segments
			 */

			int offset = bodyBytesLengthInFirstSegment;
			int segmentSize = -1;

			while (offset < bodySize && (segmentSize = inStream.read(bodyBuffer, offset, bodyBuffer.length - offset)) > 0)
				offset += segmentSize;
			
			req.body(bodyBuffer);
		}



		/*
		 * handlers pipeline
		 */

		for (HTTPHandler handler : handlers)
		{
			Class<?> clazz = handler.getClass();
			if (clazz.isAnnotationPresent(HTTPRoute.class))
			{
				HTTPRoute ann = clazz.getAnnotation(HTTPRoute.class);
				if
				(
					req.path().matches("^" + apiPrefix + ann.pattern() + "$") &&
					ann.type().toLowerCase().equals(req.type().toLowerCase())
				)
				{
					/*
					 * middlewares pipeline
					 */

					for (HTTPMiddleware middleware : middlewares)
						if (!middleware.process(req, res, ann))
							return res;
						
					handler.handle(req, res);
				}
			}
			else
				Tools.log(LogMsg.NO_ANNOTATION, new String[] {clazz.getName()});
		}

		if (res.body().length == 0)
			res.status(HTTPStatus.NOT_FOUND);
		
		return res;
	}

  @Override
	public void run()
	{
		try
		{
			HTTPResponse res = process();
			if (res == null)
			{
				Tools.log(LogMsg.BAD_REQUEST);
				return;
			}

			outStream.write(res.make());
			outStream.flush();

			socket.close();
		}
		catch (IOException e)
		{
			Tools.log(LogMsg.CONNECTION_CLOSED);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
