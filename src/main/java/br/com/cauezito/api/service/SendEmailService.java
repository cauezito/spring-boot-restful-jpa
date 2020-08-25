package br.com.cauezito.api.service;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
	
	private String email = "developer.test9627@gmail.com";
	private String password = "";	

	public void send(String subject, String to, String msg) {		
		
		Properties properties = new Properties();
		properties.put("mail.smtp.ssl.trust", "*");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls", "true"); 
		properties.put("mail.smtp.host", "smtp.gmail.com"); 
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
		
		Address[] toUser;
		try {
			toUser = InternetAddress.parse(to);
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email)); 
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(subject);
			message.setText(msg);			
			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
