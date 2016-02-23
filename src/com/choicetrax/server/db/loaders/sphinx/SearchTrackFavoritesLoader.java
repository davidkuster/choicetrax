package com.choicetrax.server.db.loaders.sphinx;

import java.sql.ResultSet;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.SearchTrackFavoritesAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.db.loaders.BaseReleasesLoader;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class SearchTrackFavoritesLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private boolean artists = false;
	private boolean labels = false;
	
	private int[] artistIDs = null;
	private int[] labelIDs = null;
	
	private String sortBy 		= Constants.SORT_BY_DEFAULT;
	private String sortOrder	= Constants.SORT_ORDER_DEFAULT;

	
	
	public SearchTrackFavoritesLoader() {
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
		SearchTrackFavoritesAction action = (SearchTrackFavoritesAction) loaderAction;
		this.artists 	= action.getArtists();
		this.labels 	= action.getLabels();
		this.artistIDs 	= action.getArtistIDs();
		this.labelIDs 	= action.getLabelIDs();
		
		if ( action.getSortBy() != null ) sortBy = action.getSortBy();
		if ( action.getSortOrder() != null ) sortOrder = action.getSortOrder();
		
		setUserID( action.getUserID() );
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( action ) );
		
		int[] trackIDs = getTrackIDs();
		
		int requestedPage = action.getRequestedPage();
		
		ReleasesCache cache = new ReleasesCache( trackIDs, Constants.CACHE_RELEASES_FAVORITES );
		
		Releases firstPage = loadReleases( cache.getIDsForPage( requestedPage ) );
		cache.addReleases( firstPage, requestedPage );
		
		int secondPageNum = requestedPage + 1;
		if ( cache.getNumPages() >= secondPageNum ) {
			Releases secondPage = loadReleases( cache.getIDsForPage( secondPageNum ) );
			cache.addReleases( secondPage, secondPageNum );
		}
		
        return cache;
	}

	
    /*
     * creates SQL to do searching. 
     */
    protected String createTrackIdSQL()
    {
    	StringBuffer subquery = new StringBuffer();
    	
    	subquery.append( "select t1.TrackID "
    					+ "from Tracks t1 "
    					+ "where ( " );
    	   	 	
    	if ( artists ) 
    	{
    		String artistIdString = ServerConstants.convertIntArrayToString( artistIDs );
    		
    		subquery.append( "t1.ArtistID in ( " + artistIdString + " ) " 
    						+ "or t1.RemixerArtistID in ( " + artistIdString + " ) "
    						+ "or t1.VocalistArtistID in ( " + artistIdString + " ) " );
    	}
    	
    	if ( labels ) 
    	{
    		if ( artists ) subquery.append( "or " );
    		
    		subquery.append( "t1.LabelID in ( "
    						+ ServerConstants.convertIntArrayToString( labelIDs ) + " ) " );
    	}
    					
    	subquery.append( ") "
    					+ "and t1.TrackID in ( select distinct TrackID from PartnerInventory ) "
    					// above clause is necessary to make sure tracks are in stock somewhere
    					
    					+ "order by t1.LoadDate desc, "
    					+	"t1.AlbumID "
    					+ "limit " + Constants.MAX_TRACKIDS_PER_QUERY + " " );
    	
    	return subquery.toString();
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
    
    
    private String createSphinxQuery( SphinxLoader cl )
    	throws ChoicetraxException
    {

    	String artistNames = null;
    	String labelNames = null;
    	
    	if ( ( artistIDs != null ) && ( artistIDs.length > 0 ) )
    		artistNames = loadNames( artistIDs );
    	
    	if ( ( labelIDs != null ) && ( labelIDs.length > 0 ) )
    		labelNames = loadNames( labelIDs );
    	
    	
    	StringBuffer query = new StringBuffer();
   	
    	if ( ( artistNames != null ) && ( artistNames.length() > 0 ) )
    	{
    		query.append( " @(ArtistName,RemixerName,VocalistName,MixName) " 
    						+ artistNames + " " );
       	}
    	    	
    	if ( ( labelNames != null ) && ( labelNames.length() > 0 ) )
    	{
    		if ( query.length() > 0 ) query.append( "| " );
    		query.append( " @LabelName " + labelNames + " " );
    	}
    	    	
    	return query.toString();
    }
    
    
    private String loadNames( int[] ids ) throws ChoicetraxException
    {
    	StringBuffer names = new StringBuffer();
    	
    	DBResource dbHandle = null;
    	
    	StringBuffer sql = new StringBuffer();
    	if ( ids == artistIDs )
    		sql.append( "select ArtistName as Name from Artists where ArtistID in (" );
    	else if ( ids == labelIDs )
    		sql.append( "select LabelName as Name from Labels where LabelID in (" );
    	
    	for ( int x=0; x < ids.length; x++ ) {
			if ( x > 0 ) sql.append( ", " );
			sql.append( ids[ x ] );
		}    		
		sql.append( ")" );
		
    	
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( sql.toString() );
    		while ( rs.next() )
    		{
    			String name = rs.getString( "Name" );
    			
    			if ( names.length() > 0 ) names.append( "| " );
    			names.append( "\"" + name + "\" " );
    		}
    		rs.close();
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing artist/label load: " + t,
    										this.getClass().getName() + ".loadNames()" );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}
    	
    	return names.toString();
    }
    
}