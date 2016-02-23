package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.data.cache.CacheObjectList;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class CachePrefetchController 
	implements ChoicetraxLoaderResponseCallback 
{
	
	private ChoicetraxViewManager viewManager = null;
	private Cache cache = null;
	private int pageNum = 0;
	
	
	public CachePrefetchController() { }
	
	
	public CachePrefetchController( ChoicetraxViewManager manager,
									Cache cache,
									int page )
	{
		this.viewManager = manager;
		this.cache = cache;
		this.pageNum = page;
	}


	
	public void onFailure( Throwable caught ) 
	{
		viewManager.handleError( caught, this.getClass().getName() + ".onFailure()" );
	}


	public void onSuccess( LoaderResponse result ) 
	{
		cache.addCacheObjectListToPage( (CacheObjectList) result, pageNum );
	}

}
