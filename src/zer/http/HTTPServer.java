package zer.http;



import java.net.Socket;
import java.net.ServerSocket;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import constants.Server;



class SocketProcessor implements Runnable
{
	private Socket socket;
	private DataInputStream inputStream = null;
	private DataOutputStream outputStream = null;
	private ArrayList<HTTPHandler> handlers;
  private ArrayList<HTTPMiddleware> middlewares; 
	private byte[] buffer = new byte[1000000];

	public SocketProcessor(Socket socket, ArrayList<HTTPHandler> handlers, ArrayList<HTTPMiddleware> middlewares) throws IOException
	{
		this.socket = socket;
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());

		this.handlers = handlers;
    this.middlewares = middlewares;
	}
	
  public void writeOutput(byte[] data) throws IOException
  {
    outputStream.write(data);
	  outputStream.flush();
  }

  @Override
	public void run()
	{
		try
		{
			int bytesRead = inputStream.read(buffer);
			if (bytesRead == -1)
				return;
			
			HTTPRequest req = new HTTPRequest();
			req.parse(new String(buffer, 0, bytesRead, StandardCharsets.ISO_8859_1));
			System.out.println(new String(buffer, 0, bytesRead, StandardCharsets.ISO_8859_1));
			if (!req.bodyIsEmpty())
			{
				int reqLength = Integer.parseInt(req.get("Content-Length"));
				int bufferStart = 0;
				int chunkSize = 0;
				
				while (chunkSize <= reqLength && (bytesRead = inputStream.read(buffer, bufferStart + bytesRead, 4096)) > 0)
					bufferStart += chunkSize;

				System.out.println(reqLength);

				req = new HTTPRequest();
				if (!req.parse(new String(buffer, 0, reqLength + bytesRead, StandardCharsets.UTF_8)))
					return;
			}

			HTTPResponse res = new HTTPResponse();
			for (HTTPHandler handler : handlers)
			{
				Class<?> clazz = handler.getClass();
				if (clazz.isAnnotationPresent(HTTPRoute.class))
				{
					HTTPRoute ann = clazz.getAnnotation(HTTPRoute.class);
					if
					(
						req.get("Path").matches("^" + Server.API_PREFIX + ann.pattern() + "$") &&
						ann.type().toLowerCase().equals(req.get("Type").toLowerCase()) &&
						HTTPTool.matchExtensions(ann.extensions(), req.get("Path"))
					)
          {
            for (HTTPMiddleware middleware : middlewares)
              if (!middleware.process(req, res, ann))
              {
                writeOutput(res.make());
                return;
              }
              
						handler.handle(req, res);
          }
				}
				else
					System.out.println("[warn]: handler \"" + clazz.getName() + "\" have no \"zer.http.HTTPRoute\" annotation.. ignore");
			}

			if (res.bodyIsEmpty())
        res
          .set("Code", "404")
          .set("Word", "NOT_FOUND");
			
			writeOutput(res.make());
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}

public class HTTPServer extends HTTPConfig
{
	private ArrayList<HTTPHandler> handlers = new ArrayList<>();
  private ArrayList<HTTPMiddleware> middlewares = new ArrayList<>();

	public void addHandler(HTTPHandler h) { handlers.add(h); }
  public void addMiddleware(HTTPMiddleware m) { middlewares.add(m); }
	public void run()
	{
		try
		{
			/* create server socket */
			ServerSocket serverSocket = new ServerSocket(port);

			/* start listening */
			while (true)
			{
				Socket socket = serverSocket.accept();
				new Thread(new SocketProcessor(socket, handlers, middlewares)).start();
			}
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
