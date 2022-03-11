package zer.http;



import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;



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
	public void run() throws IOException
	{
		ServerSocket serverSocket = new ServerSocket(port);

		while (true)
		{
			Socket socket = serverSocket.accept();
			
			new Thread(new SocketProcessor(socket, handlers, middlewares)).start();
		}
	}
}
