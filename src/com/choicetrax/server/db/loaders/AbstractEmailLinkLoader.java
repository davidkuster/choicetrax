package com.choicetrax.server.db.loaders;

import java.net.URLDecoder;
import java.sql.ResultSet;

import com.choicetrax.client.actions.EmailLinkLoaderAction;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.util.exception.ChoicetraxDatabaseException;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.util.bcrypt.BCrypt;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public abstract class AbstractEmailLinkLoader implements DataLoader
{
	
	private EmailLinkLoaderAction requestObj = null;
	
	private String sessionID = null;
	
	

	public AbstractEmailLinkLoader() {
		super();
	}
	
	
	
	protected String getSessionID() {
		return this.sessionID;
	}
	protected void setSessionID( String sessionID ) {
		this.sessionID = sessionID;
	}
	
	
	
	protected abstract String getIdNotFoundExceptionText();
	
	protected abstract DataLoader createResponseDataLoader();
	
	protected abstract LoaderAction createResponseAction( int id );	
	
	
	/**
	 * must select these fields in the SQL:
	 * - SessionID
	 * - Timestamp1
	 * - Timestamp2
	 * - NumLogins
	 * - ID
	 * 
	 * @param emailAddr
	 * @return
	 */
	protected abstract String createTimestampSQL( String emailAddr );
	
	
	/**
	 * Use this if anything needs to be done on the ID upon successful verification.
	 * 
	 * @param id
	 */
	protected abstract void doSuccessOperation( int id ) throws ChoicetraxException;
	
	

	@Override
	public LoaderResponse loadData( LoaderAction action ) 
		throws ChoicetraxException 
	{
		this.requestObj = (EmailLinkLoaderAction) action;
		
		String email = ServerConstants.escapeChars( requestObj.getEmailAddress() );
		String timestamp = ServerConstants.escapeChars( requestObj.getTimestamp() );
		
		try
		{
			timestamp = URLDecoder.decode( timestamp, "UTF-8" );
			
			int id = verifyEmailAndTimestampAndLoadID( email, timestamp ); 
				
			if ( id <= 0 ) 
			{
				throw new ChoicetraxException( getIdNotFoundExceptionText() );
			}
			else
			{
				doSuccessOperation( id );
				
				DataLoader loader = createResponseDataLoader();
				LoaderAction responseAction = createResponseAction( id );
				
				return loader.loadData( responseAction );
			}
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Unhandled exception: " + t.toString(),
											this.getClass().getName() + ".loadData()" );
		}
	}
	
	
	
	private int verifyEmailAndTimestampAndLoadID( String emailAddr, String timestamp ) 
		throws ChoicetraxException
	{
		int id = 0;
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			String sql = createTimestampSQL( emailAddr );
			
			long timestamp1 = 0;
			long timestamp2 = 0;
			int numLogins = 0;
			
			ResultSet rs = dbHandle.executeQuery( sql );
			if ( rs.next() ) 
			{
				this.sessionID 	= rs.getString( "SessionID" );
				String ts1		= rs.getString( "Timestamp1" );
				String ts2		= rs.getString( "Timestamp2" );
				numLogins		= rs.getInt( "NumLogins" );
				id				= rs.getInt( "ID" );
				
				// some of these values may be null in the database
				if ( ts1 != null ) timestamp1 = Long.parseLong( ts1 );
				if ( ts2 != null ) timestamp2 = Long.parseLong( ts2 );
			}
			rs.close();
			
			String[] split = timestamp.split( "\\+" );
			if ( split.length < 2 )
				split = timestamp.split( " " );
			
			String encryptedID = split[ 0 ];
			long sum = Long.parseLong( split[ 1 ] );
			
			if ( BCrypt.checkpw( sessionID, encryptedID )
				&& ( sum == timestamp1 + timestamp2 + numLogins ) )
			{
				return id;
			}
			else 
				return 0;
		}
		catch( Exception e )
		{
			throw new ChoicetraxDatabaseException( "Unable to load account from database: " + e,
					this.getClass().getName() + ".verifyEmailAndTimestampAndLoadID()" );
		}
		finally
		{
			if ( dbHandle != null ) 
				dbHandle.close();
		}
	}
	

}
