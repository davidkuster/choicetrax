package com.choicetrax.client.data.cache;

import java.util.List;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.LoaderResponse;


public interface Cache
	extends LoaderResponse
{
	
	public String getActionString();
	
	public void setActionString( String actionString );
	
	
	public int getCurrentPage();
	
	public void setCurrentPage( int pageNum );
	
	public int getNumPages();
	
	public int size();
	
	
	public int getCacheType();
	
	public void setCacheType( int cacheType );

	
	public int[] getIDsForPage( int pageNum );
	
	public boolean isPagePopulated( int pageNum );
	
	public LoaderAction getCacheLoaderAction( int pageNum );
	
	public void addCacheObjectListToPage( CacheObjectList list, int pageNum );

	public void setPagePopulated( int currentPage );
	
	
	public List<Integer> getGenreIDsForPage( int pageNum );
	
	public List<Integer> getGenreIDsForCurrentPage();
	
}
