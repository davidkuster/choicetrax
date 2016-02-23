package com.choicetrax.server.util.email;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.choicetrax.client.util.exception.ChoicetraxEmailException;

public class ChoicetraxEmail 
{
	
	private String fromAddress = null;
	private String toAddress = null;
	private String subject = null;
	private String bodyPlainText = null;
	private String bodyHTML = null; 
	private String mailHost = "localhost";
	
	
	public ChoicetraxEmail() {
		super();
	}
	
	
	
	public void send() throws ChoicetraxEmailException
	{
		Properties props = new Properties();
		
		props.put( "mail.smtp.host", mailHost );
		//props.put( "mail.debug", "true" );
			
		InternetAddress from = null;
		InternetAddress to = null;
		
		try {
			from = new InternetAddress( fromAddress );
		} catch ( AddressException ae ) {
			throw new ChoicetraxEmailException( 
					"Incorrectly formatted sender email address [" + fromAddress + "]",
					this.getClass().getName() + ".send()" );
		}
		
		try {
			to = new InternetAddress( toAddress );
		} catch ( AddressException ae ) {
			throw new ChoicetraxEmailException(
					"Incorrectly formatted receipient email address [" + toAddress + "]",
					this.getClass().getName() + ".send()" );
		}
			
		Session session = Session.getInstance( props );
			
		try
		{
			MimeMessage msg = new MimeMessage( session );
			msg.setFrom( from );
			msg.addRecipient( Message.RecipientType.TO, to );
			msg.setSubject( subject );
			  
			// Create an "Alternative" Multipart message
			Multipart mp = new MimeMultipart( "alternative" );
			      
			// Read text file, load it into a BodyPart, and add it to the
			// message.
			BodyPart bp1 = new MimeBodyPart();
	        bp1.setDataHandler( new DataHandler( bodyPlainText, "text/plain" ) );
			mp.addBodyPart(bp1);
			      
			// Do the same with the HTML part
			BodyPart bp2 = new MimeBodyPart();
	        bp2.setDataHandler( new DataHandler( bodyHTML, "text/html" ) );
			mp.addBodyPart( bp2 );
			      
			// Set the content for the message and transmit
			msg.setContent( mp );
			Transport.send( msg );
		}
		catch ( MessagingException me ) 
		{
			throw new ChoicetraxEmailException( 
					"Unable to send email: " + me,
					this.getClass().getName() + ".send()" );
		}
	}

 
	
	public String getSenderAddress() {
		return this.fromAddress;
	}
	public void setSenderAddress( String senderEmailAddress ) {
		this.fromAddress = senderEmailAddress;
	}
	
	public String getRecipientAddress() {
		return this.toAddress;
	}
	public void setRecipientAddress( String recipientEmailAddress ) {
		this.toAddress = recipientEmailAddress;
	}
	
	public String getSubject() {
		return this.subject;
	}
	public void setSubject( String subject ) {
		this.subject = subject;
	}
	
	public String getBodyPlainText() {
		return this.bodyPlainText;
	}
	public void setBodyPlainText( String bodyPlainText ) {
		this.bodyPlainText = bodyPlainText;
	}
	
	public String getBodyHTML() {
		return this.bodyHTML;
	}
	public void setBodyHTML( String bodyHTML ) {
		this.bodyHTML = bodyHTML;
	}
	
	public void setMailHost( String host ) {
		this.mailHost = host;
	}

}