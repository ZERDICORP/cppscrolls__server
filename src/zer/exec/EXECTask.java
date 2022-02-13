package zer.exec;

import java.io.IOException;

class EXECTask extends Thread
{
	private Process process;
  private int timeOut;
	private String command;

  public EXECTask(String command, int t) { this(command); this.timeOut = t; }
	public EXECTask(String command) { this.command = command; }

	@Override
	public void run()
	{
		try
		{
			this.process = Runtime.getRuntime().exec(this.command);
			this.process.waitFor();
		}
		catch (InterruptedException | IOException e) { e.printStackTrace(); }
	}

	public Process exec()
	{
		try
		{
			this.start();

      if (this.timeOut == -1)
        this.join();
      else
			  this.join(this.timeOut);
		}
		catch (InterruptedException e) { e.printStackTrace(); }

		if (this.process.isAlive())
		{
			this.process.destroy();
			return null;
		}

		return this.process;
	}
}
