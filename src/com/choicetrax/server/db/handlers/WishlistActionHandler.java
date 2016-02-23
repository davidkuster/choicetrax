package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.WishlistAction;
import com.choicetrax.server.db.handlers.BaseActionHandler;


public class WishlistActionHandler 
	extends BaseActionHandler
{
	private int trackID = -1;
	private int userID = -1;
	
	
	public WishlistActionHandler( HandlerAction handlerAction )
	{
		super( handlerAction );
		
		WishlistAction action = (WishlistAction) handlerAction;		
		this.trackID = action.getTrackID();
		this.userID = action.getUserID();
	}
	
	
	protected String createAddSQL()
	{
		return  "insert into UserWishlist "
				+ "( UserID, TrackID, DateAdded ) "
				+ "values ( " 
				+ 	userID + ", " + trackID + ", sysdate() ) "
				+ "on duplicate key update "
				+ 	"DateRemoved = null ";
	}
	
	protected String createRemoveSQL()
	{
		return "update UserWishlist "
				+ "set DateRemoved = sysdate() "
				+ "where UserID = " + userID + " "
				+	"and TrackID = " + trackID + " ";
	}
	
}