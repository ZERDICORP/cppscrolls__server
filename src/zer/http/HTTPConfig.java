package zer.http;



public class HTTPConfig
{
	protected static int port = 80;
	protected static String ip = "127.0.0.1";

	public static void setPort(int p) { port = p; }
	public static void setIp(String i) { ip = i; }

	public static final int getPort() { return port; }
	public static final String getIp() { return ip; }
	public static final String getHost() { return ip + ":" + port; }
}
