

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class addEvent {
	public static void main(String args[]) {
		addEvent runEvent = new addEvent();
		runEvent.GamilSender("philipzheng@gmail.com", "20110217");
	}
	
	public boolean GamilSender(String email, String title) {
		boolean result = false;
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		final String username = "G_ID";
		final String password = "G_PASSWD";
		Session session = Session.getDefaultInstance(props,
				new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		// -- Create a new message --
		Message msg = new MimeMessage(session);

		// -- Set the FROM and TO fields --
		try {
			msg.setFrom(new InternetAddress("youraccount@gmail.com"));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(
					email, false));
			msg.setSubject(MimeUtility.encodeText(title, "UTF-8", "B"));
			msg.setText("Rµo«H´ú¸Õ");
			msg.setSentDate(new Date());
			//Transport.send(msg);
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", 465, username, password);
			transport.sendMessage(msg,msg.getAllRecipients());
			transport.close();
			result = true;
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
