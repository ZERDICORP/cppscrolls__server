package zer.http;



public class HTTPConfig
{
	protected static int apiPrefixOffset;	
	protected static int port;
	protected static String ip;
	protected static String apiPrefix;

	static
	{
		port = 8080;
		ip = "127.0.0.1";
		apiPrefix = "";
		apiPrefixOffset = 0;
	}

	public static void port(int p) { port = p; }
	public static void ip(String i) { ip = i; }
	public static void apiPrefix(String ap)
	{
		apiPrefix = ap;
		apiPrefixOffset = apiPrefix.split("/").length;
	}
	
	public static final int apiPrefixOffset() { return apiPrefixOffset; }	
	public static final int port() { return port; }
	public static final String ip() { return ip; }
	public static final String host() { return ip + ":" + port; }
	public static final String apiPrefix() { return apiPrefix; }
}
