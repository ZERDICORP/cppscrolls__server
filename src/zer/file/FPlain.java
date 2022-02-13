package zer.file;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FPlain
{
	public static String read(String sPath)
	{
		String sResult = new String();
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sPath)));
			String sLine;
			while ((sLine = br.readLine()) != null)
				sResult += sLine + "\n";
		}
		catch (IOException e) {e.printStackTrace();}

		if (sResult.length() > 0)
			sResult = sResult.substring(0, sResult.length() - 1);
		
		return sResult;
	}
	
	public static void write(String sPath, String sContent)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(sPath));
			writer.write(sContent);
			writer.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
}