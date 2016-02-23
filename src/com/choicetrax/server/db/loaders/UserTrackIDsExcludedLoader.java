package com.choicetrax.server.db.loaders;


/**
 * Loads all the TrackIDs associated with a userID.
 * Purchase History, Virtual Carts, Wishlist, Rated Tracks, etc.
 * 
 * @author David
 *
 */
public class UserTrackIDsExcludedLoader 
	extends BaseReleasesLoader
{
	
	private int userID = -1;
	private int maxTrackRating = -1;
	
	
	public UserTrackIDsExcludedLoader( int userID ) {
		super();
		this.userID = userID;
	}

	
	public void setMaxTrackRating( int rating ) {
		this.maxTrackRating = rating;
	}
	
	
	
	protected String createTrackIdSQL() 
	{
		return "select TrackID from UserRecommendationsRemoved where userid = " + userID + " "
				+ "union select TrackID from UserRatedTracks "
				+	"where userid = " + userID + " and rating < " + maxTrackRating + " ";
	}

}
