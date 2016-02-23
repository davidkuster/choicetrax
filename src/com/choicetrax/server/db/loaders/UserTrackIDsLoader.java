package com.choicetrax.server.db.loaders;


/**
 * Loads all the TrackIDs associated with a userID.
 * Purchase History, Virtual Carts, Wishlist, Rated Tracks, etc.
 * 
 * @author David
 *
 */
public class UserTrackIDsLoader 
	extends BaseReleasesLoader
{
	
	private int userID = -1;
	private int minTrackRating = -1;
	
	
	public UserTrackIDsLoader( int userID ) {
		super();
		this.userID = userID;
	}

	
	public void setMinTrackRating( int rating ) {
		this.minTrackRating = rating;
	}
	
	
	
	protected String createTrackIdSQL() 
	{
		return "select TrackID from UserPurchaseHistory where userid = " + userID + " "
				+ "union select TrackID from UserWishlist where userid = " + userID + " "
				+ "union select TrackID from UserVirtualCarts where userid = " + userID + " "
				+ "union select TrackID from UserRatedTracks "
				+	"where userid = " + userID + " and rating >= " + minTrackRating + " ";
	}

}
