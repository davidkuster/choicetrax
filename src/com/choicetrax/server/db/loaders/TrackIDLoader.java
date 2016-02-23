package com.choicetrax.server.db.loaders;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.LoadTrackIDAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;


public class TrackIDLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private LoadTrackIDAction requestObj = null;
	
	
	public TrackIDLoader() {
		super();
	}

	
	@Override
	public LoaderResponse loadData( LoaderAction action ) throws ChoicetraxException 
	{
		this.requestObj = (LoadTrackIDAction) action;
		
		setUserID( requestObj.getUserID() );
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( requestObj ) );
		
		int[] trackID = new int[] { requestObj.getTrackID() };
		ReleasesCache cache = new ReleasesCache( trackID, Constants.CACHE_RELEASES_SEARCH );
		
		Releases firstPage = loadReleases( trackID );
		cache.addReleases( firstPage, 1 );
		
		return cache;
	}
	

	@Override
	protected String createTrackIdSQL() {
		return null;
	}

}
