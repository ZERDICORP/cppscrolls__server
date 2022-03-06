package zer.http;



import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;



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
	
	public HTTPResponse process() throws IOException
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
		req.parseHeaders(new String(firstSegmentBuffer, 0, headersSize - 1, StandardCharsets.UTF_8));



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
			
			int bodyBytesLengthInFirstSegment = firstSegmentSize - (headersSize + 3);
			for (int i = 0; i < bodyBytesLengthInFirstSegment; ++i)
					bodyBuffer[i] = firstSegmentBuffer[i + (headersSize + 3)];



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
				return;

			outStream.write(res.make());
			outStream.flush();
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}



public class HTTPServer extends HTTPConfig
{
	private ArrayList<HTTPHandler> handlers;
  private ArrayList<HTTPMiddleware> middlewares;

	{
		handlers = new ArrayList<>();
		middlewares = new ArrayList<>();
	}

	public void addHandler(HTTPHandler h) { handlers.add(h); }
  public void addMiddleware(HTTPMiddleware m) { middlewares.add(m); }
	public void run()
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(port);

			while (true)
			{
				Socket socket = serverSocket.accept();
				new Thread(new SocketProcessor(socket, handlers, middlewares)).start();
			}
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
