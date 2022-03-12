package zer.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;



public class FTool
{
	public static void delete(String path) { new File(path).delete(); }

  public static Boolean exists(String path) { return new File(path).exists(); }
	
  public static int size(String path) { return (int) new File(path).length(); }
	
	public static void deleteFolder(String path)
	{
		try
		{
			File file = new File(path);

			if (file.isDirectory())
			{
				File[] entries = file.listFiles();
				if (entries != null)
					for (File entry : entries)
						deleteFolder(entry.getPath());
			}
			if (!file.delete()) {
				throw new IOException("Failed to delete " + file);
			}
		}
		catch (IOException e) { e.printStackTrace(); }
	}	

  public static FType type(String path)
	{
		if (path.contains("."))
		{
			String[] parts = path.split("\\.");
			return FType.valueOf(parts[parts.length - 1].toUpperCase());
		}
		return null;
	}

	public static String readPlain(String path)
	{
		String result = new String();
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String line;
			while ((line = br.readLine()) != null)
				result += line + "\n";
		}
		catch (IOException e) { e.printStackTrace(); }
	
		if (result.length() > 0)
			/*
			 * remove last new line character
			 */
			result = result.substring(0, result.length() - 1);
		
		return result;
	}	
}
