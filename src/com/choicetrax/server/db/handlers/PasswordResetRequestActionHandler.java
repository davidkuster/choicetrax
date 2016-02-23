package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.EmailLinkHandlerAction;
import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.constants.Constants;


public class PasswordResetRequestActionHandler 
	extends AbstractEmailLinkHandler
{
	
	
	public PasswordResetRequestActionHandler( HandlerAction handlerAction ) {
		super( (EmailLinkHandlerAction) handlerAction );
	}
	
	

	@Override
	protected String createEmailHTML( String emailLinkURL ) 
	{
		return "<img src=http://www.choicetrax.com/img/layout/logoHeader.gif><p>"
		+ "Here is the link to reset your Choicetrax password: <p>"
		+ "<a href=" + emailLinkURL + ">" + emailLinkURL + "</a> <p>"
		+ "If you have any problems please email "
		+ "<a href=mailto:dave@choicetrax.com>dave@choicetrax.com</a>. "
		+ "Thanks, and good hunting!  :) <p>"
		+ "tall dave <br>"
		+ "dave@choicetrax.com";
	}


	@Override
	protected String createEmailLinkURL( String emailAddr, String timestamp ) 
	{
		return "http://www.choicetrax.com/#"
				+ Constants.HISTORY_PASSWORD_RESET
				+ "email(" + emailAddr + ")"
				+ "ts(" + timestamp + ")";
	}



	@Override
	protected String createEmailText( String passwordResetURL ) 
	{
		return "Here is the link to reset your Choicetrax password: \n\n"
		+ passwordResetURL + "\n\n"
		+ "If you have any problems please email dave@choicetrax.com. "
		+ "Thanks, and good hunting!  :) \n\n"
		+ "tall dave \n"
		+ "dave@choicetrax.com";
	}



	@Override
	protected String createTimestampSQL( String emailAddress ) 
	{
		return "select SessionID, "
				+ "( SIDTimestamp + 0 ) as Timestamp1, "
				+ "( LastLoginTimestamp + 0 ) as Timestamp2, NumLogins "
				+ "from Users "
				+ "where EmailAddress = '" + emailAddress + "' ";
	}



	@Override
	protected String getEmailSubjectLine() {
		return "Choicetrax password reset";
	}



	@Override
	protected String getNormalResponseText() {
		return "Sent! Please check your email for the password reset link.";
	}
		

}
