package com.choicetrax.server.db.loaders.sphinx;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.db.loaders.BaseReleasesLoader;
import com.choicetrax.server.util.jdbc.ResourceManager;

import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;


public class SearchReleasesAdvancedLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	protected ReleasesSearchAdvancedAction requestObj = null;
	
	protected String sortBy 	= Constants.SORT_BY_DEFAULT;
	protected String sortOrder	= Constants.SORT_ORDER_DEFAULT;
	
	
	public SearchReleasesAdvancedLoader() {
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
		this.requestObj = (ReleasesSearchAdvancedAction) action;
		
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
		
		ReleasesCache cache = new ReleasesCache( trackIDs, Constants.CACHE_RELEASES_SEARCH );
		
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
    	String artistName	= ServerConstants.escapeChars( requestObj.getArtistName() ); 
    	String trackName 	= ServerConstants.escapeChars( requestObj.getTrackName() ); 
    	String labelName 	= ServerConstants.escapeChars( requestObj.getLabelName() ); 
    	int[] genreIDs		= requestObj.getGenreIDs(); 
    	java.util.Date releaseDateBegin	= requestObj.getReleaseDateBegin(); 
    	java.util.Date releaseDateEnd	= requestObj.getReleaseDateEnd(); 
    	int[] partnerIDs	= requestObj.getPartnerIDs();
    	
    	String subquerySelect 		= "select distinct t1.TrackID ";
    	StringBuffer subqueryFrom 	= new StringBuffer();
    	StringBuffer subqueryWhere	= new StringBuffer();
    	
    	subqueryFrom.append( "from Tracks t1 " );
    	subqueryWhere.append( "where 1=1 ");
    	
   	
    	if ( ( artistName != null ) && ( ! "".equals( artistName.trim() ) ) )
    	{
    		subqueryFrom.append( "left outer join Artists a1 "
    								+ "on ( t1.ArtistID = a1.ArtistID ) " );
    		subqueryFrom.append( "left outer join Artists a2 "
									+ "on ( t1.RemixerArtistID = a2.ArtistID ) " );
    		subqueryFrom.append( "left outer join Artists a3 "
    								+ "on ( t1.VocalistArtistID = a3.ArtistID ) " );
    		subqueryWhere.append( "and ( a1.ArtistName like '" + artistName + "%' "
    								+ "or a2.ArtistName like '" + artistName + "%' "
    								+ "or a3.ArtistName like '" + artistName + "%' " );
    								
    		// only do double wildcard on longer artist names
    		if ( artistName.length() >= 5 )
    			subqueryWhere.append( "or t1.MixName like '%" + artistName + "%' " );
    		else
    			subqueryWhere.append( "or t1.MixName like '" + artistName + "%'" );
    								
    								// look for "The ..."
    								//+ "or a1.ArtistName like 'The " + artistName + "%' "
    								//+ "or a2.ArtistName like 'The " + artistName + "%' "
    								//+ "or a3.ArtistName like 'The " + artistName + "%' "
    								
    								// look for "DJ ..."
    								//+ "or a1.ArtistName like 'DJ " + artistName + "%' "
    								//+ "or a2.ArtistName like 'DJ " + artistName + "%' "
    								//+ "or a3.ArtistName like 'DJ " + artistName + "%' "
    								
    		subqueryWhere.append( " ) " );
       	}
    	
    	if ( ( trackName != null ) && ( ! "".equals( trackName.trim() ) ) )
    	{
    		subqueryFrom.append( "left outer join AlbumEPs ae "
    								+ "on ( t1.AlbumID = ae.AlbumID ) " );
    		subqueryWhere.append( "and ( t1.TrackName like '%" + trackName + "%' "
    								+ " or t1.MixName like '%" + trackName + "%' "
    								+ " or ae.AlbumName like '%" + trackName + "%' ) " );
    	}
    	
    	if ( ( labelName != null ) && ( ! "".equals( labelName.trim() ) ) )
    	{
    		subqueryFrom.append( ", Labels l1 " );
    		subqueryWhere.append( "and t1.LabelID = l1.LabelID " );
    		subqueryWhere.append( "and l1.LabelName like '" + labelName + "%' " );
    	}
    	
    	if ( releaseDateBegin != null )
    	{
    		subqueryWhere.append( "and t1.ReleaseDate >= '" 
    						+ ServerConstants.DATE_DB_FORMAT.format( releaseDateBegin ) + "' " ); 
    	}
    	
    	if ( releaseDateEnd != null )
    	{
    		subqueryWhere.append( "and t1.ReleaseDate <= '" 
    						+ ServerConstants.DATE_DB_FORMAT.format( releaseDateEnd ) + "' " );
    	}
    	
    	if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
    	{
    		subqueryFrom.append( ", TrackGenres tg1 " );
    		subqueryWhere.append( "and t1.TrackID = tg1.TrackID " );
    		subqueryWhere.append( "and tg1.GenreID in ( " 
    						+ ServerConstants.convertIntArrayToString( genreIDs ) + " ) " );
    	}
    	   	
    	if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
    	{
    		subqueryFrom.append( ", PartnerInventory pi1 ");
    		subqueryWhere.append( "and t1.TrackID = pi1.TrackID " );
    		subqueryWhere.append( "and pi1.PartnerID in ( " 
    						+ ServerConstants.convertIntArrayToString( partnerIDs ) + " ) " );
    	}
    	
    	// aggregate SQL and add limit    	
    	String searchSQL = subquerySelect 
    					+ subqueryFrom 
    					+ subqueryWhere
    					+ "order by "
    					//+	"if ( t1.ReleaseDate is null, 0, 1 ), " // null dates first
    					//+	"t1.ReleaseDate desc, "
    					+	"t1.LoadDate desc, "
    					+	"t1.AlbumID "
    					+ "limit " + Constants.MAX_TRACKIDS_PER_QUERY + " ";
    	
    	//if ( subqueryFrom.indexOf( "a1" ) != -1 )
    	//	searchSQL += "a1.ArtistName, ";
    	
    	//searchSQL += "t1.TrackName, t1.MixName ";

    	
    	return searchSQL;
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
    
    
    protected String createSphinxQuery( SphinxLoader cl )
    	throws ChoicetraxException
    {
    	String artistName	= ServerConstants.escapeSphinxChars( requestObj.getArtistName() ); 
    	String trackName 	= ServerConstants.escapeSphinxChars( requestObj.getTrackName() ); 
    	String labelName 	= ServerConstants.escapeSphinxChars( requestObj.getLabelName() ); 
    	int[] genreIDs		= requestObj.getGenreIDs(); 
    	int[] partnerIDs	= requestObj.getPartnerIDs();
    	java.util.Date releaseDateBegin	= requestObj.getReleaseDateBegin(); 
    	java.util.Date releaseDateEnd	= requestObj.getReleaseDateEnd(); 
    	
    	StringBuffer query = new StringBuffer();
   	
    	if ( ( artistName != null ) && ( ! "".equals( artistName.trim() ) ) )
    	{
    		query.append( " @(ArtistName,RemixerName,VocalistName,MixName) " 
    						+ artistName + " " );
       	}
    	
    	if ( ( trackName != null ) && ( ! "".equals( trackName.trim() ) ) )
    	{
    		query.append( " @(TrackName,MixName,AlbumName) "
    						+ trackName + " " );
    	}
    	
    	if ( ( labelName != null ) && ( ! "".equals( labelName.trim() ) ) )
    	{
    		query.append( " @LabelName " + labelName + " " );
    	}
    	
    	
    	int begin 	= ResourceManager.getSphinxStartDate();
    	int end 	= ResourceManager.getSphinxEndDate();
    	
    	if ( releaseDateBegin != null ) {
    		String date = ServerConstants.SPHINX_FORMATTER.format( releaseDateBegin );
    		begin = Integer.parseInt( date );
    	}
    	if ( releaseDateEnd != null ) {
    		String date = ServerConstants.SPHINX_FORMATTER.format( releaseDateEnd );
    		end = Integer.parseInt( date );
    	}
    	if ( begin != ResourceManager.getSphinxStartDate() 
    		|| end != ResourceManager.getSphinxEndDate() )
    	{
    		cl.setFilterRange( "LoadDate", begin, end );
    	}
    	
    	
    	if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
    	{
    		cl.setFilter( "TrackGenreIDs", genreIDs );
    	}
    	   	
    	if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
    	{
    		cl.setFilter( "PartnerIDs", partnerIDs );
    	}
    	    	
    	return query.toString();
    }

}