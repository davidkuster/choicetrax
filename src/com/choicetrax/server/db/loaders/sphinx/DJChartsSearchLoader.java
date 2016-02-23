package com.choicetrax.server.db.loaders.sphinx;

import java.util.Calendar;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.DJChartsSearchAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.db.loaders.DJChartsLoader;


public class DJChartsSearchLoader 
	extends DJChartsLoader
	implements DataLoader
{
	
	private DJChartsSearchAction requestObj = null;
		
	
	public DJChartsSearchLoader() {
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
		this.requestObj = (DJChartsSearchAction) action;
		
		
		// 1. init cache with all trackIDs - createTrackIdSQL() 
		//    called by BaseReleasesLoader.initializeCache()
    	// 2. call BaseReleasesLoader.loadReleases() and
		//    populate ReleasesCache with 1st 15 results
		// 3. return ReleasesCache to client
		
		// initialize cache
		int[] chartIDs = getChartIDs();
		
		DJChartsCache cache = new DJChartsCache( chartIDs, Constants.CACHE_CHARTS_DJ );
		
		int requestedPage = requestObj.getRequestedPage();
		
		DJCharts firstPage = loadCharts( cache.getIDsForPage( requestedPage ) );
		cache.addCharts( firstPage, requestedPage );
		
		int secondPageNum = requestedPage + 1;
		if ( cache.getNumPages() >= secondPageNum ) {
			DJCharts secondPage = loadCharts( cache.getIDsForPage( secondPageNum ) );
			cache.addCharts( secondPage, secondPageNum );
		}
		
        return cache;
    }
	
	
    
    /**
     * creates SQL to do searching.  includes logic on how searches are done,
     * with like/% or = or whatever.
     */
    /*protected String createChartIdSQL()
    {
    	int[] genreIDs			= requestObj.getGenreIDs(); 
    	String recentDate		= requestObj.getRecentDate(); 
    	int[] partnerIDs		= requestObj.getPartnerIDs();
    	
    	
    	String subquerySelect 		= "select distinct c.ChartID ";
    	StringBuffer subqueryFrom 	= new StringBuffer();
    	StringBuffer subqueryWhere	= new StringBuffer();
    	
    	subqueryFrom.append( "from ChartsDJs c " );
    	subqueryWhere.append( "where 1=1 ");
    	
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
    			subqueryWhere.append( "and c.ChartDate >= '" + begin + "' " );
    		else
    		{
    			String end = ServerConstants.DATE_DB_FORMAT.format( endDate.getTime() );

	    		subqueryWhere.append( "and c.ChartDate between '" 
	    								+ begin + "' and '" + end + "' " );
    		}
    	}
    	
    	if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
    	{
    		subqueryFrom.append( ", ChartsDJsTracks ct, TrackGenres tg1 " );
    		subqueryWhere.append( "and c.ChartID = ct.ChartID "
    							+ "and ct.TrackID = tg1.TrackID " );
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
    		subqueryWhere.append( "and c.PartnerID in ( " + ServerConstants.convertIntArrayToString( partnerIDs ) + " ) " );
    	}
    	   	
    	// aggregate SQL and add limit    	
    	String searchSQL = subquerySelect 
    					+ subqueryFrom 
    					+ subqueryWhere
    					+ "order by c.ChartDate desc, "
    					+	"c.ChartName "
    					+ "limit " + Constants.MAX_TRACKIDS_PER_QUERY + " ";
    	
    	return searchSQL;
    }*/
    
    
    /*protected int[] getChartIDs()
		throws ChoicetraxException
	{
		String methodName = "getChartIDs()";
		
		LinkedList<Integer> idList = new LinkedList<Integer>();
		
		DBResource dbHandle = null;
		
		try 
		{
			dbHandle = ResourceManager.getDBConnection();
			
			ResultSet rs = dbHandle.executeQuery( createChartIdSQL() );
			while ( rs.next() )
			{
				int chartID = rs.getInt( "ChartID" );
				idList.add( new Integer( chartID ) );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte ) {
			throw cte;
		}
		catch ( Throwable t ) {
			throw new ChoicetraxException( "Error loading chartIDs: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally {
			dbHandle.close();
		}
		
		// isn't there a more efficient way of doing this without having to iterate
		// over the entire list again ???
		
		int[] chartIDs = new int[ idList.size() ];
		
		Iterator<Integer> i = idList.iterator();
		for ( int x=0; i.hasNext(); x++ )
		{
			chartIDs[ x ] = i.next().intValue();
		}
		
		return chartIDs;
	}*/
	    
    
    /**
     * overriding BaseReleasesLoader.getTrackIDs() to now do this searching using Sphinx
     * instead.  to go back to using only MySQL simply comment out this method.
     */
    protected int[] getChartIDs() throws ChoicetraxException
    {
    	SphinxLoader loader = new SphinxDJChartsLoader( Constants.SORT_BY_DEFAULT,
    													Constants.SORT_ORDER_DEFAULT );
		
		int[] chartIDs = loader.executeQuery( createSphinxQuery( loader ) );
		
		return chartIDs;		
    }
    
    
    private String createSphinxQuery( SphinxLoader cl )
    	throws ChoicetraxException
    {
    	String searchTerms		= requestObj.getSearchTerms();
    	int[] genreIDs			= requestObj.getGenreIDs(); 
    	String recentDate		= requestObj.getRecentDate(); 
    	int[] partnerIDs		= requestObj.getPartnerIDs();
    	
    	StringBuffer query = new StringBuffer();
   		
    	if ( recentDate != null )
    	{
    		Calendar beginDate = Calendar.getInstance();
    		Calendar endDate = Calendar.getInstance();
        	   		
    		if ( Constants.BROWSE_TODAY.equals( recentDate ) ) 
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

   			cl.setFilterRange( "ChartDate", begin, end );
    	}
    	
    	if ( ( searchTerms != null ) && ( ! "".equals( searchTerms.trim() ) ) )
    	{
    		query.append( " @(ChartArtist,ChartName,ChartArtists,ChartRemixers,ChartLabels) " 
    						+ searchTerms + " " );
       	}
    	
    	if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
    	{
    		cl.setFilter( "ChartGenreIDs", genreIDs );
    	}
    	   	
    	if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
    	{
    		cl.setFilter( "ChartPartnerID", partnerIDs );
    	}
    	    	
    	return query.toString();
    }
    
}