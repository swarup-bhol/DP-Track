package com.dptrack.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

import com.dptrack.exceptions.EmailNotSentException;



@Component
public class EmailService {
	
	
	public boolean sentMail(String email,String code,String sub, String msgs) {
	      String recipientAddress = email;
	      final  String senderAddress = "swarup.bip@outlook.com";
	      final String password= "Swarup@123";
	      boolean result = false;
	      String host = "smtp.office365.com";

	      // Get system properties
	      Properties props = System.getProperties();
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.port", "587");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.auth", "true");

	      Authenticator authenticator = new Authenticator() {
	          @Override
	          protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(senderAddress, password);
	          }
	      };
	      
	      Session session = Session.getInstance(props, authenticator);

	      try {
	             MimeMessage msg = new MimeMessage(session);
	             msg.addHeader("Content-type", "text/HTML; charset=UTF-8"); //"Content-type"
	             msg.addHeader("format", "flowed");
	             msg.addHeader("Content-Transfer-Encoding", "8bit");
	             msg.setFrom(new InternetAddress(senderAddress));
	             msg.setSentDate(new Date());
	             msg.setSubject(sub);  //"Reset Password"
	             msg.setText(msgs + "  "+ code);  //"Hi , Your OTP for Resetting the Password is  : "
	             msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress, false));
	             System.out.println("Message is ready"); //"Message is ready"
	             Transport.send(msg);
	             result = true;
	      } catch (MessagingException mex) {
	            throw new EmailNotSentException(Constants.EMAIL_NOT_SENT);
	      }
	   return result;   
	}

}