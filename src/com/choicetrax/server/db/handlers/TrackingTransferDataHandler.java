package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.PartnerTransferAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;


public class TrackingTransferDataHandler extends BaseActionHandler
{
	
	private PartnerTransferAction action = null;
	

	public TrackingTransferDataHandler( HandlerAction handlerAction ) {
		super( handlerAction );
		this.action = (PartnerTransferAction) handlerAction;
	}

	
	@Override
	public ActionResponse handleAction() 
		throws ChoicetraxException 
	{
		String methodName = "handleAction()";
		
		try
		{
			ActionResponse response = doUpdate( createAddSQL() );
				
			return response;
			
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
	
	
	protected String createAddSQL()
	{
		int[] trackIDs = action.getTrackIDs();
		
		StringBuffer sb = new StringBuffer();
		
		if ( trackIDs != null )
		{
			for ( int x=0; x < trackIDs.length; x++ )
			{
				if ( sb.length() > 0 )
					sb.append( ", " );
				
				String userID = ServerConstants.readUserIdOrIpForMySQL( action );
				
				sb.append( "( "
							+ userID + ", "
							+ action.getPartnerID() + ", "
							+ trackIDs[ x ] + ", "
							+ "sysdate() "
							+ ")" );
			}
		}
		
		return "insert into TransferTracking "
				+ "( UserID, PartnerID, TrackID, TransferTimestamp ) "
				+ "values "
				+ sb.toString();
	}
	
	
	protected String createRemoveSQL() {
		return null;
	}

}
