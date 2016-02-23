package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;


public class SearchResultsController 
	implements ChoicetraxLoaderResponseCallback 
{
	protected ChoicetraxViewManager viewManager = null;
	protected String actionString = null;
	protected int pageNum = 1;
	
	protected static final int searchPanel 		= Constants.PANEL_SEARCH;
	protected static final int releasesPanel 	= Constants.PANEL_RELEASES;
	protected int redirectPanel 				= Constants.PANEL_RELEASES;
	
	
	public SearchResultsController( ChoicetraxViewManager manager, 
									 String actionString, 
									 int pageNum )
	{
		this.viewManager = manager;
		this.actionString = actionString;
		this.pageNum = pageNum;
		
		viewManager.setWaitingState( searchPanel, true );
		viewManager.setWaitingState( releasesPanel, true );
	}
	
	public void onSuccess( LoaderResponse result ) 
	{ 
		viewManager.setWaitingState( searchPanel, false );
		viewManager.setWaitingState( releasesPanel, false );
		
		Cache cache = (Cache) result;
		cache.setActionString( actionString );
		cache.setCurrentPage( pageNum );
		
		viewManager.setCurrentSearchCache( cache );
		viewManager.updateDisplay( redirectPanel, cache );
	}
	
	public void onFailure( Throwable t ) 
	{
		viewManager.setWaitingState( searchPanel, false );
		viewManager.setWaitingState( releasesPanel, false );
		
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}
	
	
	public void setRedirectPanel( int panel ) {
		this.redirectPanel = panel;
	}

}
