package com.choicetrax.client.data.cache;

import java.util.List;

import com.choicetrax.client.actions.LoaderAction;


public class SalesChartsCache 
	extends AbstractCache
{
	
	public SalesChartsCache() {
		super();
	}
	

	protected void clearOldPages() {
	}

	public int getNumPages() {
		return 0;
	}
	
	public int size() { 
		return 0;
	}
	
	public int[] getIDsForPage( int pageNum ) {
		return null;
	}
	
	public boolean isPagePopulated( int pageNum ) {
		return false;
	}
	
	public LoaderAction getCacheLoaderAction( int pageNum ) {
		return null;
	}
	
	public void addCacheObjectListToPage( CacheObjectList list, int pageNum ) {
		
	}
	
	@Override
	public List<Integer> getGenreIDsForPage( int pageNum ) 
	{
		/*Hashtable<Integer,Integer> genreCount = new Hashtable<Integer,Integer>(); 
		
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
				
				// TODO: will this throw an error if genreID not in hashtable?
				int count = genreCount.get( genreID );
				genreCount.put( genreID, ++count );
			}
		}
		
		return Constants.sortHashtableValuesToIdIntArray( genreCount );*/
		return null;
	}

}
