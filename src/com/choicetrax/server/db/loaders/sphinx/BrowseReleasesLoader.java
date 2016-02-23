package com.choicetrax.server.db.loaders.sphinx;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import org.sphx.api.SphinxResult;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.ReleasesBrowseAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.db.loaders.BaseReleasesLoader;


public class BrowseReleasesLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private ReleasesBrowseAction requestObj = null;
	
	private String sortBy 		= Constants.SORT_BY_DEFAULT;
	private String sortOrder	= Constants.SORT_ORDER_DEFAULT;
	
	
	public BrowseReleasesLoader() {
		super();
	}
	
	
	/**
	 * performs the browse operation
	 * 
	 * @param requestObj
	 * @return
	 */
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		this.requestObj = (ReleasesBrowseAction) action;
		
		setUserID( requestObj.getUserID() );
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( requestObj ) );
		
		if ( requestObj.getSortBy() != null ) sortBy = requestObj.getSortBy();
		if ( requestObj.getSortOrder() != null ) sortOrder = requestObj.getSortOrder();
				
		// 1. init cache with all trackIDs - createTrackIdSQL() 
		//    called by BaseReleasesLoader.initializeCache()
    	// 2. call BaseReleasesLoader.loadReleases() and
		//    populate ReleasesCache with 1st 15 results
		// 3. return ReleasesCache to client
		
		// initialize cache
		int[] trackIDs = getTrackIDs();
		
		ReleasesCache cache = new ReleasesCache( trackIDs, Constants.CACHE_RELEASES_BROWSE );
		
		int requestedPage = requestObj.getRequestedPage();
		
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
    	int[] genreIDs			= requestObj.getGenreIDs(); 
    	String recentDate		= requestObj.getRecentDate(); 
    	int[] partnerIDs		= requestObj.getPartnerIDs();
    	boolean onlyExclusive	= requestObj.isOnlyExclusive();
    	
    	
    	String subquerySelect 		= "select distinct t1.TrackID ";
    	StringBuffer subqueryFrom 	= new StringBuffer();
    	StringBuffer subqueryWhere	= new StringBuffer();
    	
    	subqueryFrom.append( "from Tracks t1, Artists a " );
    	subqueryWhere.append( "where t1.ArtistID = a.ArtistID ");
    	
    	if ( recentDate != null )
    	{
    		Calendar beginDate = Calendar.getInstance();
    		Calendar endDate = Calendar.getInstance();
        	
    		//endDate.add( Calendar.DATE, 1 );
    		
        	if ( Constants.BROWSE_FUTURE.equals( recentDate ) )
    			endDate = null;
        	//else if ( Constants.BROWSE_TODAY.equals( recentDate ) )
        	//	beginDate.add( Calendar.DATE, -1 );
    		else if ( Constants.BROWSE_LAST_7_DAYS.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -7 );
    		else if ( Constants.BROWSE_LAST_14_DAYS.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -14 );
    		else if ( Constants.BROWSE_LAST_21_DAYS.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -21 );
    		else if ( Constants.BROWSE_LAST_MONTH.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -30 );
    		else if ( Constants.BROWSE_LAST_QUARTER.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -90 );
    		
    		String begin = ServerConstants.DATE_DB_FORMAT.format( beginDate.getTime() );
    		
    		if ( endDate == null )
    			subqueryWhere.append( "and t1.ReleaseDate >= '" + begin + "' " );
    		else
    		{
    			String end = ServerConstants.DATE_DB_FORMAT.format( endDate.getTime() );

	    		subqueryWhere.append( "and t1.ReleaseDate between '" 
	    								+ begin + "' and '" + end + "' " );
    		}
    	}
    	
    	if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
    	{
    		subqueryFrom.append( ", TrackGenres tg1 " );
    		subqueryWhere.append( "and t1.TrackID = tg1.TrackID " );
    		subqueryWhere.append( "and tg1.GenreID in ( " );
    		
    		for ( int x=0; x < genreIDs.length; x++ )
    		{
    			if ( x > 0 ) subqueryWhere.append( ", " );
    			subqueryWhere.append( genreIDs[ x ] );
    		}
    			
    		subqueryWhere.append( " ) " );
    	}
    	   	
    	if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
    	{
    		subqueryFrom.append( ", PartnerInventory pi1 ");
    		subqueryWhere.append( "and t1.TrackID = pi1.TrackID " );
    		subqueryWhere.append( "and pi1.PartnerID in ( " 
    							+ ServerConstants.convertIntArrayToString( partnerIDs ) + " ) " );
    	}
    	
    	if ( onlyExclusive )
    	{
    		subqueryFrom.append( ", "
    							+ "( select t2.TrackID, count(pi2.PartnerID) "
	    						+ 	"from Tracks t2, PartnerInventory pi2 "
	    						+ 	"where t2.TrackID = pi2.TrackID "
	    						+ 	"group by t2.TrackID "
	    						+ 	"having count( pi2.PartnerID ) = 1 "
			  					+ ") as UniqueView " );
			subqueryWhere.append( "and t1.TrackID = UniqueView.TrackID " );
    	}
    	
    	// aggregate SQL and add limit    	
    	String searchSQL = subquerySelect 
    					+ subqueryFrom 
    					+ subqueryWhere
    					+ "order by "
    					//+	"t1.ReleaseDate desc, "
    					+	"t1.LoadDate desc, "
    					+	"t1.AlbumID "
    					//+	"a.ArtistName, "
    					//+ 	"t1.TrackName, "
    					//+ 	"t1.MixName ";
    					+ "limit " + Constants.MAX_TRACKIDS_PER_QUERY + " ";
    	
    	return searchSQL;
    }
    
    
    /**
     * overriding BaseReleasesLoader.getTrackIDs() to now do this searching using Sphinx
     * instead.  to go back to using only MySQL simply comment out this method.
     */
    protected int[] getTrackIDs() throws ChoicetraxException
    {
    	int[] trackIDs = null;
    	
    	SphinxLoader loader = new SphinxReleasesLoader( sortBy, sortOrder );
    	
		SphinxResult result = loader.getResult( createSphinxQuery( loader ) );
		
		int numMatches = result.matches.length;
		
		if ( requestObj.isOnlyExclusive() )
		{
			int partnerIdIndex = 0;
			int numAttrNames = result.attrNames.length;
			for ( int x=0; x < numAttrNames; x++ ) {
				if ( result.attrNames[ x ].equalsIgnoreCase( "PartnerIDs" ) ) {
					partnerIdIndex = x;
					break;
				}
			}
    		
			LinkedList<Integer> list = new LinkedList<Integer>();
			for ( int i=0; i < numMatches; i++ ) {
				long[] partnerIds = (long[]) result.matches[ i ].attrValues.get( partnerIdIndex );
				if ( partnerIds.length == 1 ) 
					list.add( (int) result.matches[ i ].docId );
			}
			
			trackIDs = new int[ list.size() ];
			Iterator<Integer> i = list.iterator();
			for ( int x=0; i.hasNext(); x++ ) {
				trackIDs[ x ] = i.next();
			}
		}
		else
		{
			trackIDs = new int[ numMatches ];
			
    		for ( int i=0; i < numMatches; i++ ) 
    			trackIDs[ i ] = (int) result.matches[ i ].docId;
		}
    	
    	return trackIDs;
    }
    
    
    private String createSphinxQuery( SphinxLoader cl )
    	throws ChoicetraxException
    {
    	int[] genreIDs			= requestObj.getGenreIDs(); 
    	String recentDate		= requestObj.getRecentDate(); 
    	int[] partnerIDs		= requestObj.getPartnerIDs();
    	boolean onlyExclusive	= requestObj.isOnlyExclusive();
    	int minimumRating		= requestObj.getMinRating();
    	
    	StringBuffer query = new StringBuffer();
   		
    	if ( recentDate != null )
    	{
    		Calendar beginDate = Calendar.getInstance();
    		Calendar endDate = Calendar.getInstance();
        	   		
        	if ( Constants.BROWSE_FUTURE.equals( recentDate ) )
    			endDate.add( Calendar.YEAR, 50 );
        	else if ( Constants.BROWSE_TODAY.equals( recentDate ) )
        		beginDate.add( Calendar.DATE, -1 );
    		else if ( Constants.BROWSE_LAST_7_DAYS.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -7 );
    		else if ( Constants.BROWSE_LAST_14_DAYS.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -14 );
    		else if ( Constants.BROWSE_LAST_21_DAYS.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -21 );
    		else if ( Constants.BROWSE_LAST_MONTH.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -30 );
    		else if ( Constants.BROWSE_LAST_QUARTER.equals( recentDate ) )
    			beginDate.add( Calendar.DATE, -90 );
    		
    		String beginString = ServerConstants.SPHINX_FORMATTER.format( beginDate.getTime() );   		
   			String endString = ServerConstants.SPHINX_FORMATTER.format( endDate.getTime() );
   			int begin = Integer.parseInt( beginString );
   			int end = Integer.parseInt( endString );

   			cl.setFilterRange( "LoadDate", begin, end );
    	}
    	
    	if ( minimumRating > 0 ) 
    	{
    		cl.setFilterRange( "TrackRatingOverall", minimumRating, 10 );    		
    	}
    	
    	if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
    	{
    		cl.setFilter( "TrackGenreIDs", genreIDs );
    	}
    	   	
    	if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
    	{
    		cl.setFilter( "PartnerIDs", partnerIDs );
    	}
    	
    	if ( onlyExclusive )
    	{
    		// going to post-filter these instead,
    		// doesn't seem to be a way to do it in Sphinx...
    		
    		/*query.append( "@PartnerIDSearch "
    					+ "1 -(2|3|4|5)"
    					+ "| 2 -(1|3|4|5) "
    					+ "| 3 -(1|2|4|5) "
    					+ "| 4 -(1|2|3|5) "
    					+ "| 5 -(1|2|3|4) " );*/
    	}
    	    	
    	return query.toString();
    }
    
}