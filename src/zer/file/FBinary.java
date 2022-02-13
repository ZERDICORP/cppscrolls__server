package zer.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FBinary
{
	public static byte[] read(String sPath)
	{
		byte[] bytes = new byte[FTool.size(sPath)];
		try
		{
			new FileInputStream(sPath).read(bytes);
		}
		catch (IOException e) {e.printStackTrace();}

		return bytes;
	}

	public static void write(String sPath, byte[] bytes)
	{
		try
		{
			new FileOutputStream(sPath).write(bytes);
		}
		catch (IOException e) {e.printStackTrace();}
	}
}