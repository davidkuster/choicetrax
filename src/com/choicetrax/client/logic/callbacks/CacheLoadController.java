package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.data.cache.CacheObjectList;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class CacheLoadController 
	implements ChoicetraxLoaderResponseCallback 
{
	
	private ChoicetraxViewManager viewManager = null;
	private Cache cache = null;
	private int pageNum = 0;
	private int updatePanel = -1;
	
	
	
	public CacheLoadController( ChoicetraxViewManager manager, 
								Cache cache, 
								int page,
								int viewComposite )
	{
		this.viewManager = manager;
		this.cache = cache;
		this.pageNum = page;
		this.updatePanel = viewComposite;
		
		viewManager.setWaitingState( Constants.PANEL_RELEASES, true );
	}
	
		
	public void onFailure( Throwable caught ) 
	{
		viewManager.setWaitingState( Constants.PANEL_RELEASES, false );
		viewManager.handleError( caught, this.getClass().getName() + ".onFailure()" );
	}


	public void onSuccess( LoaderResponse result ) 
	{
		viewManager.setWaitingState( Constants.PANEL_RELEASES, false );
		
		// save releases obj in cache obj
		cache.addCacheObjectListToPage( (CacheObjectList) result, pageNum );
		
		viewManager.updateDisplay( updatePanel, cache );
		//viewManager.updateSearchDisplay( cache );
	}

}
