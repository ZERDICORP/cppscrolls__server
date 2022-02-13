package zer.file;

import java.io.File;



public class FTool
{
	public static void delete(String path) { new File(path).delete(); }

  public static Boolean exists(String path) { return new File(path).exists(); }
	
  public static int size(String path) { return (int) new File(path).length(); }
	
  public static FType type(String path)
	{
		if (path.contains("."))
		{
			String[] parts = path.split("\\.");
			return FType.valueOf(parts[parts.length - 1].toUpperCase());
		}
		return null;
	}
}
