package com.choicetrax.server.db.loaders;

import com.choicetrax.client.actions.*;
import com.choicetrax.client.actions.loaderactions.LoadReleasesAction;
import com.choicetrax.client.actions.responses.*;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;


public class RetrieveReleasesLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private LoadReleasesAction requestObj = null;
	
	
	public RetrieveReleasesLoader() {
		super();
	}
	
	
	/**
	 * performs the search operation
	 * 
	 * @param requestObj
	 * @return
	 */
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		this.requestObj = (LoadReleasesAction) action;
		
		setUserID( requestObj.getUserID() );
		setOrdering( requestObj.getOrdering() );
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( requestObj ) );
    	
        return loadReleases( requestObj.getTrackIDs() );
    }
    
    
    protected String createTrackIdSQL()
    {
    	// no need to load track IDs since they are passed in 
    	// action obj
    	
    	return null;
    }

}