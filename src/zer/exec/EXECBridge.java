package zer.exec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class EXECBridge
{
  private static int timeOut;

  static
  {
    timeOut = -1;
  }

  public static EXECResult exec(String command, int t)
  {
    timeOut = t;
    return exec(command);
  }

	public static EXECResult exec(String command)
	{
		Process process = new EXECTask(command, timeOut).exec();
		
		if (process == null)
			return new EXECResult(EXECResultCode.ERROR, "[error]: Too long.. (more than " + timeOut + " ms)");

		if (process.exitValue() != 0)
			return new EXECResult(EXECResultCode.ERROR, process.getErrorStream());
		return new EXECResult(EXECResultCode.OK, process.getInputStream());
	}
};
