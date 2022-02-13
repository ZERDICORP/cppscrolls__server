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



class SocketProcessor implements Runnable
{
	private Socket socket;
	private DataInputStream inputStream = null;
	private DataOutputStream outputStream = null;
	private ArrayList<HTTPHandler> handlers;
  private ArrayList<HTTPMiddleware> middlewares; 
	private byte[] readData = new byte[100000];

	public SocketProcessor(Socket socket, ArrayList<HTTPHandler> handlers, ArrayList<HTTPMiddleware> middlewares) throws IOException
	{
		this.socket = socket;
		this.inputStream = new DataInputStream(this.socket.getInputStream());
		this.outputStream = new DataOutputStream(this.socket.getOutputStream());
		this.handlers = handlers;
    this.middlewares = middlewares;
	}
	
  public void writeOutput(byte[] data) throws IOException
  {
    this.outputStream.write(data);
	  this.outputStream.flush();
  }

  @Override
	public void run()
	{
		try
		{
			int readDataLength = this.inputStream.read(this.readData);
			if (readDataLength == -1)
				return;

			HTTPRequest req = new HTTPRequest();
			
      boolean isValid = req.parse(new String(this.readData, 0, readDataLength, StandardCharsets.UTF_8));
			if (!isValid)
				return;

			HTTPResponse res = new HTTPResponse();
			for (HTTPHandler handler : this.handlers)
			{
				Class<?> clazz = handler.getClass();
				if (clazz.isAnnotationPresent(HTTPRoute.class))
				{
					HTTPRoute ann = clazz.getAnnotation(HTTPRoute.class);
					if
					(
						req.get("Path").matches(ann.pattern()) &&
						ann.type().toLowerCase().equals(req.get("Type").toLowerCase()) &&
						HTTPTool.matchExtensions(ann.extensions(), req.get("Path"))
					)
          {
            for (HTTPMiddleware middleware : this.middlewares)
              if (!middleware.process(req, res, ann))
              {
                this.writeOutput(res.make());
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
			
			this.writeOutput(res.make());
		}
		catch (Exception e) { e.printStackTrace(); }
	}
}

public class HTTPServer extends HTTPConfig
{
	private ArrayList<HTTPHandler> handlers = new ArrayList<>();
  private ArrayList<HTTPMiddleware> middlewares = new ArrayList<>();

	public void addHandler(HTTPHandler h) { this.handlers.add(h); }
  public void addMiddleware(HTTPMiddleware m) { this.middlewares.add(m); }
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
				new Thread(new SocketProcessor(socket, this.handlers, this.middlewares)).start();
			}
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
