package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.RecommendationAction;


public class RecommendationActionHandler extends BaseActionHandler
{

	private int trackID = -1;
	private int userID = -1;
	
	
	public RecommendationActionHandler( HandlerAction handlerAction ) 
	{
		super( handlerAction );
		
		RecommendationAction action = (RecommendationAction) handlerAction;		
		this.trackID = action.getTrackID();
		this.userID = action.getUserID();
	}
	
	
	protected String createAddSQL()
	{
		return null;
	}
	
	/**
	 * This is currently somewhat counter-intuitive.  We're using action type "REMOVE"
	 * to add a row to a table, indicating that the recommendation has been removed.
	 * TODO: examine the architecture of this, recommendations are getting weirder
	 * all the time.
	 */
	protected String createRemoveSQL()
	{
		return "insert into UserRecommendationsRemoved "
				+ "set "
				+	"UserID = " + userID + ", "
				+	"TrackID = " + trackID + " "
				+ "on duplicate key update "
				+	"UserID = UserID, "
				+	"TrackID = TrackID";
		
		/*return "update UserRecommendations "
				+ "set "
				+	"DateRemoved = sysdate() "
				+ "where UserID = " + userID + " "
				+	"and TrackID = " + trackID + " ";*/
	}

}
