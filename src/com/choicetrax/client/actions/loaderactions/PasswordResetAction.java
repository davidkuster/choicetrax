package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.EmailLinkLoaderAction;
import com.choicetrax.client.actions.LoaderHistoryAction;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class PasswordResetAction 
	extends LoaderHistoryAction
	implements EmailLinkLoaderAction
{
	
    private String emailAddress = null;
    private String timestamp = null;
    private int userID = 0;
         
    
    private static final String PREFIX_EMAIL		= "email(";
    private static final String PREFIX_TIMESTAMP	= "ts(";

    
    
    public PasswordResetAction() {
    	super();
    }
    
    
    
    public PasswordResetAction( String actionString )
    {
		super( actionString );
		
		if ( actionString == null ) return;
		
		emailAddress	= readEncodedValue( actionString, PREFIX_EMAIL );
		timestamp		= readEncodedValue( actionString, PREFIX_TIMESTAMP );
	}
    
    
    
    public String getLogString() 
	{
		StringBuffer log = new StringBuffer();
		if ( emailAddress != null )
			log.append( "email [" + emailAddress + "] " );
		if ( timestamp != null )
			log.append( "ts [" + timestamp + "] " );
		if ( userID > 0 )
			log.append( "userID [" + userID + "] " );
		
		return "PasswordResetAction: " + log.toString();
	}

    

	public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( emailAddress != null )
			sb.append( PREFIX_EMAIL + emailAddress + DELIMITER );
		
		if ( timestamp != null )
			sb.append( PREFIX_TIMESTAMP + timestamp + DELIMITER );
				
		return sb.toString();
	}



	
	
	
	public String getEmailAddress() {
		return this.emailAddress;
	}
	public void setEmailAddress( String emailAddress ) {
		this.emailAddress = emailAddress;
	}

	public String getTimestamp() {
		return this.timestamp;
	}	
	public void setTimestamp( String timestamp ) {
		this.timestamp = timestamp;
	}
		
	public int getUserID() {
		return userID;
	}
	public void setUserID( int userID ) {
		this.userID = userID;
	}




	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( emailAddress != null )
			sb.append( "email [" + emailAddress + "] " );

		if ( timestamp != null )
			sb.append( "ts [" + timestamp + "] " );
		
		if ( userID > 0 )
			sb.append( "userID [" + userID + "] " );
		
		return sb.toString();
	}

}