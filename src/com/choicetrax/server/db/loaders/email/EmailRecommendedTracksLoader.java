package com.choicetrax.server.db.loaders.email;

import org.apache.log4j.Logger;

import com.choicetrax.client.actions.loaderactions.RecommendedTracksAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.loaders.RecommendedTracksLoader;


public class EmailRecommendedTracksLoader
{
	
	private Logger logger = Logger.getLogger( EmailRecommendedTracksLoader.class );
	
	private int userID = 0;
	
	private int totalRecommendedTracks = 0;
	
	
	public EmailRecommendedTracksLoader( int userID ) {
		super();
		this.userID = userID;
	}
	
	
	public Releases getReleases( int numReleasesToLoad, String sortType ) 
	{
		try
		{
			RecommendedTracksAction action = new RecommendedTracksAction();
			action.setUserID( userID );
			action.setSortBy( sortType );
			action.setSortOrder( Constants.SORT_ORDER_DESCENDING );
			
			RecommendedTracksLoader loader = new RecommendedTracksLoader();
			LoaderResponse response = loader.loadData( action );
			ReleasesCache cache = (ReleasesCache) response;
			
			this.totalRecommendedTracks = cache.size();
			
			return cache.getReleasesForPage( 1 );
		}
		catch ( ChoicetraxException ce ) 
		{
			logger.error( "Error loading recommended tracks for userID [" + userID + "]", ce );
			return new Releases();
		}
	}
	
	
	public int getTotalRecommendedTracksNum() {
		return totalRecommendedTracks;
	}

}
