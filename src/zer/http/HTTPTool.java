package zer.http;



import java.io.IOException;
import zer.file.FTool;



public class HTTPTool
{
	public static Boolean matchExtensions(String[] extensions, String path) throws IOException
	{
		if (extensions.length == 0)
			return true;
		
		for (String extension : extensions)
			if (FTool.type(path).equals(FTool.type("." + extension)))
				return true;
		return false;
	}
}
