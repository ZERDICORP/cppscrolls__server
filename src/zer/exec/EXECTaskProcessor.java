package zer.exec;



import java.io.IOException;



class EXECTaskProcessor extends Thread
{
	private Process process;
	private int timeOut;
	private String command;

	public EXECTaskProcessor(String command, int timeOut) { this(command); this.timeOut = timeOut; }
	public EXECTaskProcessor(String command) { this.command = command; }

	@Override
	public void run()
	{
		try
		{
			process = Runtime.getRuntime().exec(command);
			process.waitFor();
		}
		catch (InterruptedException | IOException e) { e.printStackTrace(); }
	}

	public Process exec()
	{
		try
		{
			start();

			if (timeOut == -1)
				join();
			else
				join(timeOut);
		}
		catch (InterruptedException e) { e.printStackTrace(); }

		if (process.isAlive())
		{
			process.destroy();
			return null;
		}

		return process;
	}
}
