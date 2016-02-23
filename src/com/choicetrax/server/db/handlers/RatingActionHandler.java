package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.RatingAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.db.handlers.BaseActionHandler;


public class RatingActionHandler 
	extends BaseActionHandler
{
	private RatingAction action = null;
	
	
	public RatingActionHandler( HandlerAction handlerAction )
	{
		super( handlerAction );
		
		this.action = (RatingAction) handlerAction;
	}
	
	
	/**
	 * need to override BaseActionHandler.handleAction() because
	 * a rating action has two steps - to add the rating (ratings
	 * cannot be removed) and then update the average rating on
	 * the Tracks table.
	 */
	@Override
	public ActionResponse handleAction() 
		throws ChoicetraxException 
	{
		String methodName = "handleAction()";
		
		try
		{
			ActionResponse response = doUpdate( createAddSQL() );
			
			// update rating on Tracks table
			doUpdate( createUpdateTrackRatingSQL() );
	
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
		return "insert into UserRatedTracks "
				+ "( UserID, TrackID, Rating, RatingTimestamp ) "
				+ "values ( "
				+	action.getUserID() + ", "
				+	action.getTrackID() + ", "
				+	action.getRating() + ", "
				+	"sysdate() ) "
				+ "on duplicate key update "
				+	"Rating = " + action.getRating() + ", "
				+	"RatingTimestamp = sysdate() ";
	}
	
	
	protected String createRemoveSQL() 
	{
		return null;
	}
	
	private String createUpdateTrackRatingSQL()
	{
		return "update Tracks t, "
				+ "( select TrackID, "
				+	"count( UserID ) as NumRatings, "
				+	"sum( Rating ) / count( UserID ) as AvgRating "
				+	"from UserRatedTracks "
				+	"where TrackID = " + action.getTrackID() + " "
				+ 	"group by TrackID ) "
				+	"as Rated "
				+ "set t.TrackRatingOverall = Rated.AvgRating, "
				+	"t.TrackNumRatings = Rated.NumRatings "
				+ "where t.TrackID = Rated.TrackID";
	}
}