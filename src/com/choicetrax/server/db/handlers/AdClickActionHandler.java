package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.AdClickAction;

import com.choicetrax.server.constants.ServerConstants;


public class AdClickActionHandler 
	extends BaseActionHandler 
{
	
	private AdClickAction action = null;
	
	
	public AdClickActionHandler( HandlerAction handlerAction )
	{
		super( handlerAction );
		
		this.action = (AdClickAction) handlerAction;
	}
	
	
	protected String createAddSQL()
	{
		// if userID is not set, use IP address instead
		String userID = ServerConstants.readUserIdOrIpForMySQL( action );
		
		String updateSQL = "insert into AdsClickedByUsers "
							+ " ( AdID, UserID, ClickDate, NumClicks ) "
							+ "values "
							+ " ( " 
							+ 	action.getAdID() + ", "
							+ 	userID + ", "
							+	"( current_date() + 0 ), "
							+ 	" 1 " 
							+ " ) "
							+ "on duplicate key update "
							+	" NumClicks = NumClicks + 1 ";
						
		return updateSQL;
	}
	
	protected String createRemoveSQL()
	{
		return null;
	}

}
