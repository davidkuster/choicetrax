package com.choicetrax.server.db.handlers;

import org.apache.log4j.Logger;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.FeedbackAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.util.exception.ChoicetraxEmailException;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.util.email.ChoicetraxEmail;


public class FeedbackActionHandler 
	extends BaseActionHandler
{
	
	private FeedbackAction action = null;
	
	private Logger logger = Logger.getLogger( FeedbackActionHandler.class );
	
	
		

	public FeedbackActionHandler( HandlerAction handlerAction ) {
		super( handlerAction );
		this.action = (FeedbackAction) handlerAction;
	}
	
	
	
	public ActionResponse handleAction()
		throws ChoicetraxException
	{
		
		String emailText = createEmailBody();
		
		try
		{
			// send email first, then let super.handleAction() insert into database table
			ChoicetraxEmail email = new ChoicetraxEmail();
			
			email.setSenderAddress( "feedback@choicetrax.com" );
			email.setRecipientAddress( "dave@choicetrax.com" );
			
			email.setSubject( "Choicetrax Feedback: " + action.getFeedbackSubject() );
						
			email.setBodyHTML( emailText );
			email.setBodyPlainText( emailText );
			
			email.send();
		}
		catch ( ChoicetraxEmailException e ) {
			logger.error( "Unable to send feedback email [" + emailText + "]", e );
		}
		
		return super.handleAction();
	}
	
	
	
	private String createEmailBody()
	{
		String text = "Choicetrax Feedback "
					+ "<p>"
					+ "UserID: " + action.getUserID() + " <br>"
					+ "IP Address: " + action.getIpAddress() + " <br>"
					+ "Name: " + action.getName() + " <br>"
					+ "Email: " + action.getEmailAddress() + " <br>"
					+ "Subject: " + action.getFeedbackSubject() + " <br>"
					+ "Feedback: " + action.getFeedbackText();
		
		return text;
	}
	
	

	@Override
	protected String createAddSQL() 
	{
		/*
		 * create table UserFeedback (
				FeedbackID INT(11) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
				UserID INT(11) UNSIGNED NOT NULL,
				FeedbackTimestamp TIMESTAMP NOT NULL,
				Name VARCHAR(60) NULL,
				Email VARCHAR(128) NULL,
				Subject VARCHAR(100) NOT NULL,
				FeedbackText VARCHAR(1024) NOT NULL
			);*/
		
		String userID 	= ServerConstants.readUserIdOrIpForMySQL( action );
		String name 	= ServerConstants.escapeChars( action.getName() );
		String email 	= ServerConstants.escapeChars( action.getEmailAddress() );
		String subject	= ServerConstants.escapeChars( action.getFeedbackSubject() );
		String text		= ServerConstants.escapeChars( action.getFeedbackText() );
		
		if ( subject.length() > 100 )
			subject = subject.substring( 0, 97 ) + "..";
		
		if ( text.length() > 1024 )
			text = text.substring( 0, 1021 ) + "..";
		
		String sql = "insert into UserFeedback "
					+ "( UserID, FeedbackTimestamp, Name, Email, Subject, FeedbackText ) "
					+ "values ( "
					+	userID + ", "
					+	"sysdate(), "
					+	"'" + name + "', "
					+	"'" + email + "', "
					+	"'" + subject + "', "
					+	"'" + text + "' "
					+ ") ";
		
		return sql;
	}



	@Override
	protected String createRemoveSQL() {
		return null;
	}

}
