package zer.exec;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;



public class EXECTask
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
		long time = System.currentTimeMillis();

		Process process = new EXECTaskProcessor(command, timeOut).exec();
		
		long timeDiff = System.currentTimeMillis() - time;

		if (process == null)
			return new EXECResult(EXECResultCode.ERROR, "too long.. (more than " + timeOut + " ms)");

		if (process.exitValue() != 0)
			return new EXECResult(EXECResultCode.ERROR, process.getErrorStream());

		return new EXECResult(EXECResultCode.OK, process.getInputStream(), timeDiff);
	}
};
