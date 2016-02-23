package com.choicetrax.server.db.loaders.sphinx;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchQuickAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.db.loaders.BaseReleasesLoader;

import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;


public class SearchReleasesQuickLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private ReleasesSearchQuickAction requestObj = null;
	
	private String sortBy 		= Constants.SORT_BY_DEFAULT;
	private String sortOrder	= Constants.SORT_ORDER_DEFAULT;
		
	
	public SearchReleasesQuickLoader() {
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
		this.requestObj = (ReleasesSearchQuickAction) action;
		
		if ( requestObj.getSortBy() != null ) sortBy = requestObj.getSortBy();
		if ( requestObj.getSortOrder() != null ) sortOrder = requestObj.getSortOrder();
		
		setUserID( requestObj.getUserID() );
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( requestObj ) );
				
		// 1. init cache with all trackIDs - createTrackIdSQL() 
		//    called by BaseReleasesLoader.initializeCache()
    	// 2. call BaseReleasesLoader.loadReleases() and
		//    populate ReleasesCache with 1st 15 results
		// 3. return ReleasesCache to client
		
		// initialize cache
		int[] trackIDs = getTrackIDs();
		
		int requestedPage = requestObj.getRequestedPage();
		
		ReleasesCache cache = new ReleasesCache( trackIDs, Constants.CACHE_RELEASES_QUICKSEARCH );
		
		Releases firstPage = loadReleases( cache.getIDsForPage( requestedPage ) );
		cache.addReleases( firstPage, requestedPage );
		
		int secondPageNum = requestedPage + 1;
		if ( cache.getNumPages() >= secondPageNum ) {
			Releases secondPage = loadReleases( cache.getIDsForPage( secondPageNum ) );
			cache.addReleases( secondPage, secondPageNum );
		}
		
        return cache;
    }
	
	
    
    /**
     * creates SQL to do searching.  includes logic on how searches are done,
     * with like/% or = or whatever.
     */
    protected String createTrackIdSQL()
    {
    	return null;
    }
    
    
    
    /**
     * overriding BaseReleasesLoader.getTrackIDs() to now do this searching using Sphinx
     * instead.  to go back to using only MySQL simply comment out this method.
     */
    protected int[] getTrackIDs() throws ChoicetraxException
    {
    	SphinxLoader loader = new SphinxReleasesLoader( sortBy, sortOrder );
		
		int[] trackIDs = loader.executeQuery( createSphinxQuery( loader ) );
    	
     	return trackIDs;
    }
    
    
    private String createSphinxQuery( SphinxLoader loader )
    	throws ChoicetraxException
    {
    	String searchTerms	= ServerConstants.escapeSphinxChars( requestObj.getSearchTerms() ); 
    	int[] genreIDs		= requestObj.getGenreIDs(); 
    	int[] partnerIDs	= requestObj.getPartnerIDs();
    	
    	StringBuffer query = new StringBuffer();
   	
    	if ( ( searchTerms != null ) && ( ! "".equals( searchTerms.trim() ) ) )
    	{
    		query.append( " @(ArtistName,RemixerName,VocalistName,"
    						+ "TrackName,MixName,AlbumName,LabelName) " 
    						+ searchTerms + " " );
       	}  	
    	
    	if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
    	{
    		loader.setFilter( "TrackGenreIDs", genreIDs );
    	}
    	   	
    	if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
    	{
    		loader.setFilter( "PartnerIDs", partnerIDs );
    	}
    	    	
    	return query.toString();
    }

}