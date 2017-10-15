package br.com.odontofacil.ws.controller;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RestController;

import br.com.odontofacil.pojo.Email;

@RestController
public class SendEmailController {
	
	private Logger logger = Logger.getLogger("cloud");
	private static String corpo;
	private static String host;
	private static Properties properties;
	
	
//	public SendEmailController(Email email) {
//		this.host = "smtp.gmail.com";
//		this.properties = System.getProperties();
//		this.mountMail(email);
//		this.sendMail(email);
//	}

	/**
	 * Send mail from contato@keepupload.com to contato@keepupload.com
	 */
	private void sendMail(Email email){
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.port", "587");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		Session session = Session.getInstance(properties,
				  new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(email.getFrom(), email.getPass());
					}
				  });
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email.getFrom()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
			message.setSubject("[Contato-Site]KeepUpload");
			message.setContent(this.corpo, "text/html");

			Transport.send(message);
			logger.info("Email eviado com sucesso!");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public void mountMail(Email email){
		this.corpo = email.getEmailFormatado();
	}
/*	public static void main(String[] args) {
		SendEmailController sem = new SendEmailController("MENSAGEMdcfsdfsdfsd");
		sem.sendMail();
	}*/
}