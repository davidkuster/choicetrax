package com.choicetrax.client.data.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.LoadReleasesAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.Releases;


public class ReleasesCache 
	extends AbstractCache
{
		
	private LinkedHashMap<Integer,ReleasesCachePage> cachePages 
		= new LinkedHashMap<Integer,ReleasesCachePage>();
	
	
	@SuppressWarnings("unused")
	private ReleasesCache() { }
	
	
	public ReleasesCache( int type ) 
	{
		super.setCacheType( type );
		cachePages.put( new Integer( 1 ), new ReleasesCachePage() );
	}
	
	
	public ReleasesCache( int[] trackIDs, int type )
	{
		super.setCacheType( type );
		
		if ( trackIDs.length == 0 )
		{
			cachePages.put( new Integer( 1 ), new ReleasesCachePage() );
		}
		else
		{
			ReleasesCachePage cachePage = new ReleasesCachePage();
			int pageNum = 1;
			int cachePageIndex = 0; // counts 0 to 15
			int x = 0;
			
			for ( x=0; x < trackIDs.length; x++ )
			{
				if ( cachePageIndex >= Constants.NUM_RELEASES_PER_PAGE )
				{
					cachePages.put( new Integer( pageNum ), cachePage );
					
					cachePage = new ReleasesCachePage();
					pageNum++;
					cachePageIndex = 0;
				}
				
				cachePage.addTrackID( trackIDs[ x ] );
				cachePageIndex++;
			}
			
			if ( cachePageIndex != 0 )
				cachePages.put( new Integer( pageNum ), cachePage );
		}
	}
		
	
	
	
	public void addReleaseDetail( ReleaseDetail rd )
	{
		if ( rd != null )
		{
			Integer lastPageNum = new Integer( cachePages.size() );
			ReleasesCachePage lastPage = (ReleasesCachePage) cachePages.get( lastPageNum );
			
			if ( ( lastPage != null ) 
				&& ( lastPage.size() < Constants.NUM_RELEASES_PER_PAGE ) )
			{
				// add ReleaseDetail to existing cache page
				lastPage.addReleaseDetail( rd );
			}
			else
			{
				// add ReleaseDetail to new cache page
				ReleasesCachePage newCachePage = new ReleasesCachePage();
				newCachePage.addReleaseDetail( rd );
				
				cachePages.put( new Integer( cachePages.size() + 1 ),
								newCachePage );
			}
		}
	}
	
	
	public void removeReleaseDetail( ReleaseDetail rd )
	{
		// need to remove release detail and then re-order
		// ReleasesCachePage objs
		
		// this will be used by User.Wishlist and PurchaseHistory
		int trackID = rd.getTrackID();
		
		Iterator<ReleasesCachePage> i = cachePages.values().iterator();
		for ( int pageNum=1; i.hasNext(); pageNum++ )
		{
			ReleasesCachePage cachePage = i.next();
			
			if ( cachePage.containsTrackID( trackID ) )
			{
				cachePage.remove( trackID );
				reorderCachePagesFrom( pageNum );
				break;
			}
		}
	}
	
	private void reorderCachePagesFrom( int pageNum )
	{
		int numPages = this.getNumPages();
		for ( int x=pageNum; x <= numPages; x++ )
		{
			ReleasesCachePage thisPage = (ReleasesCachePage) cachePages.get( new Integer( x ) ); 
			ReleasesCachePage nextPage = (ReleasesCachePage) cachePages.get( new Integer( x + 1 ) );
			
			if ( nextPage != null )
			{
				List<ReleaseDetail> releasesList = nextPage.getReleases().getReleasesList();
				
				// if releaseDetail is populated in cache, remove & add that
				if ( releasesList.size() > 0 ) 
				{
					ReleaseDetail firstReleaseFromNextPage = releasesList.get( 0 );
					nextPage.remove( firstReleaseFromNextPage.getTrackID() );
					thisPage.addReleaseDetail( firstReleaseFromNextPage );
				}
				// otherwise if cache is not populated, just use the trackID
				else 
				{
					int firstReleaseFromNextPageTrackID = (nextPage.getTrackIDs())[ 0 ];
					nextPage.remove( firstReleaseFromNextPageTrackID );
					thisPage.addTrackID( firstReleaseFromNextPageTrackID );
				}
				
				if ( nextPage.size() == 0 )
					removePage( x + 1 );
			}
			else if ( thisPage != null )
			{
				if ( thisPage.size() == 0 )
					removePage( x );
			}
		}
	}
	
	private void removePage( int pageNum )
	{
		Integer pageNumber = new Integer( pageNum );
		
		cachePages.remove( pageNumber );
		Set<Integer> keys = cachePages.keySet();
		keys.remove( pageNumber );
		
		if ( super.getCurrentPage() == pageNum )
			super.setCurrentPage( pageNum - 1 );
	}	
	
	
	protected void clearOldPages() 
	{
		int currentPage = super.getCurrentPage();
		int cacheType = super.getCacheType();
		
		if ( ( currentPage > Constants.NUM_PAGES_TO_CACHE ) 
			&& ( ( cacheType == Constants.CACHE_RELEASES_SEARCH )
				|| ( cacheType == Constants.CACHE_RELEASES_QUICKSEARCH )
				|| ( cacheType == Constants.CACHE_RELEASES_BROWSE ) 
				|| ( cacheType == Constants.CACHE_RELEASES_FAVORITES ) ) )
		{
			int removePage = currentPage - Constants.NUM_PAGES_TO_CACHE;

			ReleasesCachePage cachePage = cachePages.get( new Integer( removePage ) );
			cachePage.clearReleases();
		}
	}
	
	
	public void addReleases( Releases releases, int pageNum )
	{
		if ( releases != null )
		{
			ReleasesCachePage cachePage = (ReleasesCachePage) cachePages.get( new Integer( pageNum ) );
			cachePage.addReleases( releases );
		}
	}
	
	
	public void addCacheObjectListToPage( CacheObjectList list, int pageNum ) {
		addReleases( (Releases) list, pageNum );
	}
		
	
	public Releases getReleasesForPage( int pageNum )
	{
		ReleasesCachePage cachePage = (ReleasesCachePage) cachePages.get( new Integer( pageNum ) );
		
		if ( cachePage != null )
			return cachePage.getReleases();
		else 
			return null;
	}
	
		
	public int[] getIDsForPage( int pageNum )
	{
		ReleasesCachePage cachePage = (ReleasesCachePage) cachePages.get( new Integer( pageNum ) );
		
		if ( cachePage != null )
			return cachePage.getTrackIDs();
		else
			return new int[ 0 ];
	}
	
	
	public int[] getTrackIDsArray()
	{
		int[] trackIdArray = new int[ this.size() ];
		
		int x = 0;
		int numPages = getNumPages();
		for ( int pageNum = 1; pageNum <= numPages; pageNum++ ) 
		{
			int[] ids = getIDsForPage( pageNum );
			
			for ( int i=0; i < ids.length; i++ ) 
			{
				int trackID = ids[ i ];
				
				if ( trackID == 0 ) break;
				else trackIdArray[ x++ ] = trackID;
			}
		}
		
		return trackIdArray;
	}
	
	
	public ArrayList<String> getTrackIDsList() 
	{
		ArrayList<String> trackIdList = new ArrayList<String>();
		
		int numPages = getNumPages();
		for ( int pageNum = 1; pageNum <= numPages; pageNum++ ) {
			int[] ids = getIDsForPage( pageNum );
			
			for ( int i=0; i < ids.length; i++ ) 
				trackIdList.add( ids[ i ] + "" );
		}
		
		return trackIdList;
	}
	
	
	public Iterator<ReleaseDetail> getReleaseDetailIterator()
	{
		List<ReleaseDetail> releaseList = new ArrayList<ReleaseDetail>();
			
		int numPages = getNumPages();
		
		for ( int pageNum = 1; pageNum <= numPages; pageNum++ )
		{
			Releases releases = getReleasesForPage( pageNum );
			List<ReleaseDetail> releasesList = releases.getReleasesList();
			Iterator<ReleaseDetail> i = releasesList.iterator();
			
			while ( i.hasNext() ) {
				releaseList.add( i.next() );
			}
		}
		
		return releaseList.iterator();
	}
	
	
	public boolean isCached( int trackID )
	{
		Iterator<ReleasesCachePage> i = cachePages.values().iterator();
		while ( i.hasNext() )
		{
			ReleasesCachePage cachePage = i.next();
			
			if ( cachePage == null ) 
				return false;
			else if ( cachePage.getReleaseDetail( trackID ) != null )
				return true;
		}
		
		return false;
	}
	
	
	public boolean isPagePopulated( int pageNum ) 
	{
		Releases releases = getReleasesForPage( pageNum );
		return super.isPagePopulated( releases, pageNum );
	}
	
	
	
	public LoaderAction getCacheLoaderAction( int pageNum )
	{
		int[] trackIDs = getIDsForPage( pageNum );
		return new LoadReleasesAction( trackIDs );
	}
	
	
	public boolean containsTrackID( int trackID )
	{
		Iterator<ReleasesCachePage> i = cachePages.values().iterator();
		while ( i.hasNext() )
		{
			ReleasesCachePage cachePage = i.next();

			if ( cachePage == null )
				return false;
			else if ( cachePage.containsTrackID( trackID ) )
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * @return the number of releases in the cache
	 */
	public int size()
	{
		int size = 0;
		
		Iterator<ReleasesCachePage> i = cachePages.values().iterator();
		while ( i.hasNext() )
		{
			ReleasesCachePage cachePage = i.next();
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
		
		Releases releases = getReleasesForPage( pageNum );
		Iterator<ReleaseDetail> i = releases.getReleasesList().iterator();
		while ( i.hasNext() ) 
		{
			ReleaseDetail detail = i.next();
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
