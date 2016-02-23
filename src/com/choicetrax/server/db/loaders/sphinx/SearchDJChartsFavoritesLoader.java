package com.choicetrax.server.db.loaders.sphinx;

import java.sql.ResultSet;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.SearchDJChartsFavoritesAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.db.loaders.DJChartsLoader;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class SearchDJChartsFavoritesLoader 
	extends DJChartsLoader
	implements DataLoader
{
	
	//private boolean artists = false;
	//private boolean labels = false;
	
	private int[] artistIDs = null;
	//private int[] labelIDs = null;
	
	
	public SearchDJChartsFavoritesLoader() {
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
		SearchDJChartsFavoritesAction action = (SearchDJChartsFavoritesAction) loaderAction;
		//this.artists 	= action.getArtists();
		//this.labels 	= action.getLabels();
		this.artistIDs 	= action.getArtistIDs();
		//this.labelIDs 	= action.getLabelIDs();
		
		// initialize cache
		int[] chartIDs = getChartIDs();
		
		DJChartsCache cache = new DJChartsCache( chartIDs, Constants.CACHE_CHARTS_FAVORITES );
		
		DJCharts firstPage = loadCharts( cache.getIDsForPage( 1 ) );
		cache.addCharts( firstPage, 1 );
		
		if ( cache.getNumPages() >= 2 ) {
			DJCharts secondPage = loadCharts( cache.getIDsForPage( 2 ) );
			cache.addCharts( secondPage, 2 );
		}
		
        return cache;
	}

	
 
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

    	String artistNames = null;
    	String labelNames = null;
    	
    	if ( ( artistIDs != null ) && ( artistIDs.length > 0 ) )
    		artistNames = loadNames( artistIDs );
    	
    	//if ( ( labelIDs != null ) && ( labelIDs.length > 0 ) )
    	//	labelNames = loadNames( labelIDs );
    	
    	
    	StringBuffer query = new StringBuffer();
   	
    	if ( ( artistNames != null ) && ( artistNames.length() > 0 ) )
    	{
    		query.append( " @(ChartArtist,ChartName) " 
    						+ artistNames + " " );
       	}
    	    	
    	if ( ( labelNames != null ) && ( labelNames.length() > 0 ) )
    	{
    		if ( query.length() > 0 ) query.append( "| " );
    		query.append( " @ChartLabels " + labelNames + " " );
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
    	//else if ( ids == labelIDs )
    	//	sql.append( "select LabelName as Name from Labels where LabelID in (" );
    	
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