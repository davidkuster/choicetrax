package com.choicetrax.server.db.loaders;

import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.User;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.UserAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.server.db.DataLoader;


public class UserWishlistLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private User user = null;
	
	
	public LoaderResponse loadData( LoaderAction loaderAction ) 
		throws ChoicetraxException
	{
		UserAction userAction = (UserAction) loaderAction;
		this.user = userAction.getUserObj();
		
		setUserID( user.getUserID() );
	
		//return loadReleases( getTrackIDs() );
		
		int[] trackIDs = getTrackIDs();
		ReleasesCache cache = new ReleasesCache( trackIDs, Constants.CACHE_RELEASES_WISHLIST );
		
		Releases firstPage = loadReleases( cache.getIDsForPage( 1 ) );
		cache.addReleases( firstPage, 1 );
		
		return cache;
	}
	
	
	/*
	 * creates SQL to do searching.  includes logic on how searches are done,
	 * with like/% or = or whatever.
	 */
	protected String createTrackIdSQL()
	{
		String subquery = "select distinct uw.TrackID "
						+ "from UserWishlist uw, PartnerInventory pi "
						+ "where uw.UserID = " + user.getUserID() + " "
						+	"and uw.DateRemoved is null "
						+	"and uw.TrackID = pi.TrackID "
						+ "order by uw.DateAdded desc, uw.TrackID ";

		return subquery;
	}

}
