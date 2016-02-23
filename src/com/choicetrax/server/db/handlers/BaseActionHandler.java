package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.responses.ActionHandlerMessage;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.db.ActionHandler;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public abstract class BaseActionHandler 
	implements ActionHandler 
{
	
	protected HandlerAction action = null;
	protected ActionHandlerMessage response = null;
	
	
	public BaseActionHandler( HandlerAction handlerAction )
	{
		this.action = handlerAction;
		this.response = new ActionHandlerMessage();
	}
	
	
	protected abstract String createAddSQL();
	
	protected abstract String createRemoveSQL();
	

	public ActionResponse handleAction()
		throws ChoicetraxException
	{
		String methodName = "handleAction()";

		String actionType = action.getActionType();
		
		try
		{
			if ( Constants.ACTION_ADD.equals( actionType ) )
			{
				// what if user is re-adding a favorite?
				// then need to just update table & set DateRemoved = null !!!
				
				return doUpdate( createAddSQL() );
			}
			else if ( Constants.ACTION_REMOVE.equals( actionType ) )
			{
				return doUpdate( createRemoveSQL() );
			}
			else
				throw new ChoicetraxException( "Empty or unknown actionType [" + actionType + "]",
												this.getClass().getName() + "." + methodName );
			
			// beef up error handling ??? 
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
	
	
	protected ActionHandlerMessage doUpdate( String sql )
		throws ChoicetraxException
	{
		DBResource dbHandle = null;
		
		//int rowCount = 0;
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			// rowCount = 
			dbHandle.executeUpdate( sql );
		}
		catch( Exception e )
		{
			response.setResponseCode( Constants.ACTION_RESPONSE_ERROR );
			response.setResponseText( "Action handler executeUpdate() error: " + e );
		}
		finally
		{
			if ( dbHandle != null ) 
				dbHandle.close();
		}
				
		if ( response.getResponseText() == null )
		{
			response.setResponseCode( Constants.ACTION_RESPONSE_NORMAL );
			response.setResponseText( "Success" );
		}
		
		return response;
	}
	

}
