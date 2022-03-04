package zer.http;



import java.io.IOException;
import zer.file.FTool;



class Tools
{
	public static void log(String msg, String[] args)
	{
		for (String arg : args)
			msg = msg.replaceFirst("\\?", arg);

		System.out.println("[log]: " + msg);
	}

	public static int getHeadersSize(byte[] b, int length)
	{
		for (int i = 3; i < length; ++i)
			if (b[i] == '\n' && b[i - 1] == '\r' && b[i - 2] == b[i] && b[i - 3] == b[i - 1])
				return i - 2;
		return b.length;
	}

	public static boolean matchExtensions(String[] extensions, String path) throws IOException
	{
		if (extensions.length == 0)
			return true;
		
		for (String extension : extensions)
			if (FTool.type(path).equals(FTool.type("." + extension)))
				return true;
		return false;
	}
}
