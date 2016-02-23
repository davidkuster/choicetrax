package com.choicetrax.server.db.handlers;

import java.sql.ResultSet;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.BetaUserLoginAction;
import com.choicetrax.client.actions.responses.ActionHandlerMessage;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;
import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.ActionHandler;


public class BetaUserLoginActionHandler 
	implements ActionHandler 
{
	
	private BetaUserLoginAction action = null;
	
	
	public BetaUserLoginActionHandler( HandlerAction handlerAction ) {
		this.action = (BetaUserLoginAction) handlerAction;
	}
	
	

	public ActionResponse handleAction()
		throws ChoicetraxException
	{
		String methodName = "handleAction()";
	
		
		try
		{
			return loginBetaUser();
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Unhandled exception: " + t.toString(),
											this.getClass().getName() + "." + methodName );
		}
	}
	
	
	protected ActionHandlerMessage loginBetaUser()
		throws ChoicetraxException
	{
		DBResource dbHandle = null;
		
		ActionHandlerMessage response = new ActionHandlerMessage();
		
		String username = ServerConstants.escapeChars( action.getUsername() );
		String password = ServerConstants.escapeChars( action.getPassword() );
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			String sql = "select BetaUserID "
						+ "from AdminBetaUsers "
						+ "where BetaUserName = '" + username + "' "
						+	"and BetaPassword = '" + password + "' ";
			
			int betaUserID = -1;
			
			ResultSet rs = dbHandle.executeQuery( sql );
			if ( rs.next() ) {
				betaUserID = rs.getInt( 1 );
			}
			rs.close();
			
			if ( betaUserID >= 0 ) 
			{
				response.setResponseCode( Constants.ACTION_RESPONSE_NORMAL );
				response.setResponseText( "valid login" );
				
				String updateSQL = "update AdminBetaUsers "
									+ "set NumLogins = NumLogins + 1, "
									+	"LastLogin = sysdate() "
									+ "where BetaUserID = " + betaUserID + " ";
				
				dbHandle.executeUpdate( updateSQL );
			}
			else
			{
				response.setResponseCode( Constants.ACTION_RESPONSE_ERROR );
				response.setResponseText( "incorrect login" );
			}
		}
		catch( Exception e )
		{
			response.setResponseCode( Constants.ACTION_RESPONSE_ERROR );
			response.setResponseText( "Beta User login error: " + e );
		}
		finally
		{
			if ( dbHandle != null ) 
				dbHandle.close();
		}
		
		return response;
	}
	
}
