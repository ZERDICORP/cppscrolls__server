package zer.exec;



import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;



public class EXECResult
{
	long time;
	private String message;
	private EXECResultCode code;

	{
		message = new String();
		code = EXECResultCode.OK;
	}

	public long time() { return time; }
	public String message() { return message; }
	public EXECResultCode code() { return code; }

	public EXECResult(EXECResultCode code) { this.code = code; }

	public EXECResult(EXECResultCode code, String message, long time) { this(code, message); this.time = time; }
	public EXECResult(EXECResultCode code, String message) { this(code); this.message = message; }

	public EXECResult(EXECResultCode code, InputStream result, long time) { this(code, result); this.time = time; }
	public EXECResult(EXECResultCode code, InputStream result)
	{
		this(code);

		try
		{
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(result));
		
			String line;
			while ((line = bufferReader.readLine()) != null)
				message += line + "\n";
		}
		catch (IOException e) { e.printStackTrace(); }
	
		if (message.length() > 0)
			/*
			 * remove last new line character
			 */
			message = message.substring(0, message.length() - 1);
	}
}
