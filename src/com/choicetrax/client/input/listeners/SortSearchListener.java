package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.loaderactions.DJChartsSearchAction;
import com.choicetrax.client.actions.loaderactions.RecommendedTracksAction;
import com.choicetrax.client.actions.loaderactions.ReleasesBrowseAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchQuickAction;
import com.choicetrax.client.actions.loaderactions.SearchDJChartsFavoritesAction;
import com.choicetrax.client.actions.loaderactions.SearchTrackFavoritesAction;
import com.choicetrax.client.actions.loaderactions.ViewDJChartAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class SortSearchListener implements ClickHandler
{
	private ChoicetraxViewManager viewManager = null;
	
	private String sortByOption	= null;
	private String sortType		= null;
	
	
	public SortSearchListener( ChoicetraxViewManager manager, String sortBy, String type ) {
		this.viewManager = manager;
		this.sortByOption = sortBy;
		this.sortType = type;
	}
	
	public void onClick( ClickEvent event )
	{
		Cache cache = viewManager.getCurrentSearchCache();
		
		LoaderHistoryAction action = createLoaderHistoryAction( cache );
		action.setSortBy( sortByOption );
		action.setSortOrder( sortType );

		//String actionString = action.getActionString();
			//cache.getActionString()
			//	+ LoaderHistoryAction.PREFIX_SORT_BY + sortByOption + LoaderHistoryAction.DELIMITER
			//	+ LoaderHistoryAction.PREFIX_ORDER + sortType + LoaderHistoryAction.DELIMITER;
		
		String historyToken = Constants.createHistoryToken( cache.getCacheType(), 
															action.getActionString(),
															1 );
							
		viewManager.updateHistoryDisplay( historyToken );
	}
	
	
	/*
	public static final int CACHE_RELEASES_QUICKSEARCH		= 0;
	public static final int CACHE_RELEASES_SEARCH			= 1;
	public static final int CACHE_RELEASES_BROWSE			= 2;
	public static final int CACHE_RELEASES_WISHLIST 		= 4;
	public static final int CACHE_RELEASES_PURCHASE_HISTORY	= 5;
	public static final int CACHE_RELEASES_VIRTUAL_CART		= 6;
	public static final int CACHE_CHARTS_DJ					= 7;
	public static final int CACHE_CHARTS_SALES				= 8;
	public static final int CACHE_RELEASES_DJ_TRACKS		= 9;
	public static final int CACHE_RELEASES_SALES_TRACKS		= 10;
	public static final int CACHE_RELEASES_FAVORITES		= 11;
	public static final int CACHE_CHARTS_FAVORITES			= 12;
	 */
	private LoaderHistoryAction createLoaderHistoryAction( Cache cache ) 
	{
		LoaderHistoryAction action = null;
		int cacheType = cache.getCacheType();
		String actionString = cache.getActionString();
		
		if ( cacheType == Constants.CACHE_RELEASES_QUICKSEARCH ) 
			action = new ReleasesSearchQuickAction( actionString );
		else if ( cacheType == Constants.CACHE_RELEASES_SEARCH )
			action = new ReleasesSearchAdvancedAction( actionString );
		else if ( cacheType == Constants.CACHE_RELEASES_BROWSE )
			action = new ReleasesBrowseAction( actionString );
		//else if ( cacheType == Constants.CACHE_RELEASES_WISHLIST )
		//	action = new 
		else if ( cacheType == Constants.CACHE_RELEASES_FAVORITES )
			action = new SearchTrackFavoritesAction( viewManager.getCurrentUser(), actionString );
		else if ( cacheType == Constants.CACHE_CHARTS_FAVORITES )
			action = new SearchDJChartsFavoritesAction( viewManager.getCurrentUser(), actionString );
		else if ( cacheType == Constants.CACHE_CHARTS_DJ  )
			action = new DJChartsSearchAction( actionString );
		else if ( cacheType == Constants.CACHE_RELEASES_DJ_TRACKS )
			action = new ViewDJChartAction( actionString );
		else if ( cacheType == Constants.CACHE_RELEASES_RECOMMENDED )
			action = new RecommendedTracksAction( actionString );
		
		return action;
	}
}
