package zer.exec;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class EXECResult
{
  public String message;
	public EXECResultCode code;

	{
    message = new String();
    code = EXECResultCode.OK;
  }

	public EXECResult(EXECResultCode code) { this.code = code; }
	public EXECResult(EXECResultCode code, String message) { this(code); this.message = message; }
	public EXECResult(EXECResultCode code, InputStream result)
	{
		this(code);

		try
		{
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(result));
		
			String line;
			while ((line = bufferReader.readLine()) != null)
				this.message += line + "\n";
		}
		catch (IOException e) { e.printStackTrace(); }
	}
}
