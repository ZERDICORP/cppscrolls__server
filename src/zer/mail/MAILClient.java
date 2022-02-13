package zer.mail;



import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class MAILClient extends MAILConfig
{
	public static void send(String receiver, String title, String content)
	{
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(sender, password); }
		});

		try
		{
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(sender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
			message.setSubject(title);
			message.setContent(content, "text/html");
			
			Transport.send(message);
		}
		catch (MessagingException e) { throw new RuntimeException(e); }
	}
}
