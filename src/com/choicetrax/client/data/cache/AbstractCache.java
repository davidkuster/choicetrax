package com.choicetrax.client.data.cache;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractCache 
	implements Cache
{
	
	private int currentPage = 1;
	private int cacheType = -1;
	private String actionString = null;
	
	private ArrayList<Integer> populatedPages = new ArrayList<Integer>(); 
	
	
	public AbstractCache() {
		super();
	}


	public int getCurrentPage() { 
		return currentPage;
	}
	
	public void setCurrentPage( int pageNum ) {
		this.currentPage = pageNum;
		
		// only keep last 10 ReleasesCachePage pages cached in memory
		clearOldPages();
	}
	
	public String getActionString() {
		return this.actionString;
	}
	
	public void setActionString( String actionString ) {
		this.actionString = actionString;
	}

	
	public void setCacheType( int type )
	{
		this.cacheType = type;
	}
	
	public int getCacheType()
	{
		return this.cacheType;
	}
	
	
	protected boolean isPagePopulated( CacheObjectList objectList, int pageNum ) 
	{
		if ( populatedPages.contains( pageNum ) )
			return true;
		else if ( objectList == null )
			return false;
		else
		{
			// verify that this isn't an empty cache, vs. not populated
			int[] ids = getIDsForPage( pageNum );
			if ( ( ids != null ) 
				&& ( ( ids.length == 0 ) 
					|| ( ids.length == objectList.size() ) ) )
				// ids.length == 0 means cache is empty (no results), and therefore populated
				return true;
			else
				// if ids.length != listSize then this page is only partially populated
				return false;	
		}
		
		/*if ( ( objectList == null ) || ( objectList.size() == 0 ) ) 
		{
			// verify that this isn't an empty cache, vs. not populated
			int[] ids = getIDsForPage( pageNum );
			if ( ( ids != null ) 
				&& ( ( ids.length == 0 ) 
					|| ( ids.length == objectList.size() ) ) )
				// ids.length == 0 means cache is empty (no results), and therefore populated
				return true;
			else
				// if ids.length != listSize then this page is only partially populated
				return false;	
		}
		else
			return true;*/
	}
	
	public abstract boolean isPagePopulated( int pageNum );
	
	
	public void setPagePopulated( int pageNum ) 
	{
		populatedPages.add( pageNum );
	}
	
	protected abstract void clearOldPages();
	
	public abstract int getNumPages();
	
	
	
	public List<Integer> getGenreIDsForCurrentPage() {
		return getGenreIDsForPage( getCurrentPage() );
	}
	
	public abstract List<Integer> getGenreIDsForPage( int pageNum );
	
}
