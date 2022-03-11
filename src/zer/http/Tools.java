package zer.http;



import java.net.Socket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;



class Tools
{
	public static void log(String msg) { System.out.println("[log]: " + msg); }
	public static void log(String msg, String[] args)
	{
		for (String arg : args)
			msg = msg.replaceFirst("\\?", arg);
		log(msg);
	}

	public static String getSocketIpAddress(Socket socket)
	{
		InetSocketAddress sockaddr = (InetSocketAddress)socket.getRemoteSocketAddress();
		InetAddress inaddr = sockaddr.getAddress();
		Inet4Address in4addr = (Inet4Address)inaddr;

		return in4addr.toString();
	}

	public static int getHeadersSize(byte[] b, int length)
	{
		for (int i = 3; i < length; ++i)
			if (b[i] == '\n' && b[i - 1] == '\r' && b[i - 2] == b[i] && b[i - 3] == b[i - 1])
				return i - 1;
		return b.length;
	}
}
