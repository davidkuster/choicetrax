package com.choicetrax.server.db.loaders;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.AlbumEPSearchAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;


public class AlbumEPReleasesLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private int trackID = -1;
	
	
	public AlbumEPReleasesLoader() {
		super();
	}
	
	
	/**
	 * performs the search operation
	 * 
	 * @param requestObj
	 * @return
	 */
	public LoaderResponse loadData( LoaderAction loaderAction )
		throws ChoicetraxException
	{
		AlbumEPSearchAction action = (AlbumEPSearchAction) loaderAction;
		this.trackID = action.getTrackID();
		
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( action ) );
		
		return loadReleases( getTrackIDs() );
	}

	
    /*
     * creates SQL to do searching. 
     */
    protected String createTrackIdSQL()
    {
    	String subquery = "select t1.TrackID "
    					+ "from Tracks t1, Tracks t2 "
    					+ "where t1.AlbumID = t2.AlbumID "
    					+	"and t2.TrackID = " + this.trackID + " ";
    	
    	return subquery;
    }

}