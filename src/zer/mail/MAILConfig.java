package zer.mail;



import java.util.Properties;



public class MAILConfig
{
  protected static Properties props;

  static
  {
    props = new Properties();
    props.setProperty("mail.smtp.starttls.enable", "true");
    props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
    props.setProperty("mail.smtp.auth", "true");
    props.setProperty("mail.smtp.host", "smtp.gmail.com");
    props.setProperty("mail.smtp.port", "587");
  }

	protected static String sender;
	protected static String password; 

	public static void setSender(String s) { sender = s; }
	public static void setPassword(String p) { password = p; }
}
