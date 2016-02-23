package com.choicetrax.client.data.cache;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.LoadDJChartsAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.DJChartDetail;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.Genre;


public class DJChartsCache 
	extends AbstractCache 
{
	
	private LinkedHashMap<Integer,DJChartsCachePage> cachePages 
		= new LinkedHashMap<Integer,DJChartsCachePage>();
	
	
	@SuppressWarnings("unused")
	private DJChartsCache() {
		super();
	}
	
	
	public DJChartsCache( int type ) {
		super.setCacheType( type );
		cachePages.put( new Integer( 1 ), new DJChartsCachePage() );
	}
	
	
	
	public DJChartsCache( int[] chartIDs, int type )
	{
		super.setCacheType( type );
		
		if ( chartIDs.length == 0 )
		{
			cachePages.put( new Integer( 1 ), new DJChartsCachePage() );
		}
		else
		{
			DJChartsCachePage cachePage = new DJChartsCachePage();
			int pageNum = 1;
			int cachePageIndex = 0; // counts 0 to 15
			int x = 0;
			
			for ( x=0; x < chartIDs.length; x++ )
			{
				if ( cachePageIndex >= Constants.NUM_CHARTS_PER_PAGE )
				{
					cachePages.put( new Integer( pageNum ), cachePage );
					
					cachePage = new DJChartsCachePage();
					pageNum++;
					cachePageIndex = 0;
				}
				
				cachePage.addChartID( chartIDs[ x ] );
				cachePageIndex++;
			}
			
			if ( cachePageIndex != 0 )
				cachePages.put( new Integer( pageNum ), cachePage );
		}
	}
		
		
	public void addChart( DJChartDetail chart )
	{
		if ( chart != null )
		{
			Integer lastPageNum = new Integer( cachePages.size() );
			DJChartsCachePage lastPage = (DJChartsCachePage) cachePages.get( lastPageNum );
			
			if ( lastPage.size() < Constants.NUM_CHARTS_PER_PAGE )
			{
				// add Chart to existing cache page
				lastPage.addChart( chart );
			}
			else
			{
				// add Chart to new cache page
				DJChartsCachePage newCachePage = new DJChartsCachePage();
				newCachePage.addChart( chart );
				
				cachePages.put( new Integer( cachePages.size() + 1 ),
								newCachePage );
			}
		}
	}
	
	
	public void addCharts( DJCharts chartsList , int pageNum )
	{
		if ( chartsList != null )
		{
			DJChartsCachePage cachePage = (DJChartsCachePage) cachePages.get( new Integer( pageNum ) );
			cachePage.addCharts( chartsList );
		}
	}
	
	
	public void addCacheObjectListToPage( CacheObjectList list, int pageNum ) {
		addCharts( (DJCharts) list, pageNum );
	}
	
	
	protected void clearOldPages() 
	{
		int currentPage = super.getCurrentPage();
		
		if ( currentPage > Constants.NUM_PAGES_TO_CACHE ) 
		{
			int removePage = currentPage - Constants.NUM_PAGES_TO_CACHE;

			DJChartsCachePage cachePage = cachePages.get( new Integer( removePage ) );
			cachePage.clearPage();
		}
	}
			
	
	public DJCharts getChartsForPage( int pageNum )
	{
		DJChartsCachePage cachePage = (DJChartsCachePage) cachePages.get( new Integer( pageNum ) );
		
		if ( cachePage != null )
			return cachePage.getCharts();
		else 
			return null;
	}
	
		
	public int[] getIDsForPage( int pageNum )
	{
		DJChartsCachePage cachePage = (DJChartsCachePage) cachePages.get( new Integer( pageNum ) );
		
		if ( cachePage != null )
			return cachePage.getChartIDs();
		else
			return new int[ 0 ];
	}
	
	
	
	public boolean isCached( int chartID )
	{
		Iterator<DJChartsCachePage> i = cachePages.values().iterator();
		while ( i.hasNext() )
		{
			DJChartsCachePage cachePage = i.next();
			
			if ( cachePage == null ) 
				return false;
			else if ( cachePage.getChart( chartID ) != null )
				return true;
		}
		
		return false;
	}
	
	
	public boolean isPagePopulated( int pageNum ) 
	{
		DJCharts charts = getChartsForPage( pageNum );
		return super.isPagePopulated( charts, pageNum );
	}
	
	
	public LoaderAction getCacheLoaderAction( int pageNum )
	{
		int[] chartIDs = getIDsForPage( pageNum );
		return new LoadDJChartsAction( chartIDs );
	}
		
	
	/**
	 * @return the number of releases in the cache
	 */
	public int size()
	{
		int size = 0;
		
		Iterator<DJChartsCachePage> i = cachePages.values().iterator();
		while ( i.hasNext() )
		{
			DJChartsCachePage cachePage = i.next();
			size += cachePage.size();
		}
		
		return size;
	}
	
	
	public int getNumPages() {
		return cachePages.size();
	}



	@Override
	public List<Integer> getGenreIDsForPage( int pageNum ) 
	{
		HashMap<Integer,Integer> genreCount = new HashMap<Integer,Integer>(); 
		
		DJCharts charts = getChartsForPage( pageNum );
		Iterator<DJChartDetail> i = charts.getChartsList().iterator();
		while ( i.hasNext() ) 
		{
			DJChartDetail detail = i.next();
			Iterator<Genre> g = detail.getGenres().iterator();
			while ( g.hasNext() ) 
			{
				Genre genre = g.next();
				int genreID = genre.getGenreID();
				
				Integer count = genreCount.get( genreID );
				if ( count == null ) count = 0;
				
				genreCount.put( genreID, ++count );
			}
		}
		
		return Constants.sortHashValuesToOrderedIdList( genreCount );
	}

	
}
