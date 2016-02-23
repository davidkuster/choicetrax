package com.choicetrax.server.db.handlers;

import java.net.URLEncoder;
import java.sql.ResultSet;

import com.choicetrax.client.actions.EmailLinkHandlerAction;
import com.choicetrax.client.actions.responses.ActionHandlerMessage;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.ActionHandler;
import com.choicetrax.server.util.bcrypt.BCrypt;
import com.choicetrax.server.util.email.ChoicetraxEmail;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public abstract class AbstractEmailLinkHandler implements ActionHandler
{
	
	private EmailLinkHandlerAction action = null;
	

	public AbstractEmailLinkHandler( EmailLinkHandlerAction action ) {
		super();
		this.action = action;
	}
	

	@Override
	public ActionResponse handleAction() 
		throws ChoicetraxException 
	{
		String emailAddress = ServerConstants.escapeChars( action.getEmailAddress() );
		
		int responseCode = -1;
		String responseText = null;
		
		try
		{
			String timestamp = verifyEmailAndLoadTimestamp( emailAddress );
			
			if ( timestamp == null ) 
			{
				responseCode = Constants.ACTION_RESPONSE_ERROR;
				responseText = getErrorResponseText();
			}
			else
			{
				timestamp = URLEncoder.encode( timestamp, "UTF-8" );
				
				sendEmailLink( emailAddress, timestamp );
				
				responseCode = Constants.ACTION_RESPONSE_NORMAL;
				responseText = getNormalResponseText();
			}
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Unhandled exception: " + t.toString(),
											this.getClass().getName() + ".handleAction()" );
		}
		
		ActionHandlerMessage response = new ActionHandlerMessage();
		response.setResponseCode( responseCode );
		response.setResponseText( responseText );
		
		return response;
	}
	
	
	
	
	protected String getErrorResponseText() 
	{
		return "No account is registered to email address "
				+ "[" + action.getEmailAddress() + "]. \n" 
				+ "Please check your entry and try again.";
	}
	
	
	protected abstract String getNormalResponseText();
	
	
	/**
	 * Must select these fields in the SQL:
	 * - SessionID
	 * - Timestamp1
	 * - Timestamp2
	 * - NumLogins
	 * 
	 * @param emailAddress
	 * @return
	 */
	protected abstract String createTimestampSQL( String emailAddress );
	
	
	protected abstract String createEmailLinkURL( String emailAddr, String timestamp );
	
	
	protected abstract String getEmailSubjectLine();
	
	
	protected abstract String createEmailHTML( String emailLinkURL );
	
	
	protected abstract String createEmailText( String emailLinkURL );
	
	
	
	private String verifyEmailAndLoadTimestamp( String emailAddr )
		throws ChoicetraxException
	{
		String timestamp = null;
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			String sql = createTimestampSQL( emailAddr );
			
			String sessionID = null;
			long timestamp1 = 0;
			long timestamp2 = 0;
			int numLogins = 0;
			
			ResultSet rs = dbHandle.executeQuery( sql );
			if ( rs.next() ) 
			{
				sessionID 	= rs.getString( "SessionID" );
				String ts1	= rs.getString( "Timestamp1" );
				String ts2	= rs.getString( "Timestamp2" );
				numLogins	= rs.getInt( "NumLogins" );
				
				// some of these values may be null in the database
				if ( ts1 != null ) timestamp1 = Long.parseLong( ts1 );
				if ( ts2 != null ) timestamp2 = Long.parseLong( ts2 );
			}
			rs.close();
			
			if ( sessionID != null )
			{
				String encryptedID = BCrypt.hashpw( sessionID, BCrypt.gensalt() );
				long sum = timestamp1 + timestamp2 + numLogins;
				
				timestamp = encryptedID + "+" + sum;
			}
		}
		catch( Exception e )
		{
			throw new ChoicetraxException( "Unable to read email from database: " + e,
							this.getClass().getName() + ".verifyEmailAndLoadTimestamp()" );
		}
		finally
		{
			if ( dbHandle != null ) 
				dbHandle.close();
		}
		
		return timestamp;
	}
	
	
	
	private void sendEmailLink( String emailAddr, String timestamp )
		throws ChoicetraxException
	{
		String emailLinkURL = createEmailLinkURL( emailAddr, timestamp );
		
		ChoicetraxEmail email = new ChoicetraxEmail();
		
		email.setSenderAddress( "dave@choicetrax.com" );
		email.setRecipientAddress( emailAddr );
		
		email.setSubject( getEmailSubjectLine() );
		
		email.setBodyPlainText( createEmailText( emailLinkURL ) );
		email.setBodyHTML( createEmailHTML( emailLinkURL ) );
		
		email.send();
		
		/*catch ( Exception e )
		{
			throw new ChoicetraxException( 
					"Unable to send email for "
						+ "email [" + emailAddr + "] and timestamp [" + timestamp + "]: " + e,
					this.getClass().getName() + ".sendEmailLink()" );
		}*/
	}
		

}
