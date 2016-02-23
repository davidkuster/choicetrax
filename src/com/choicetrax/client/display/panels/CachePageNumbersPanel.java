package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class CachePageNumbersPanel extends Composite
{
	
	private ChoicetraxViewManager viewManager = null;
	private Cache cache = null;
	
	// number of page numbers to display
	private static final int NUM_PAGES = 10;
	
	private String itemTerm = "trax";

	
	public CachePageNumbersPanel( ChoicetraxViewManager manager, Cache cache ) {
		super();
		this.viewManager = manager;
		this.cache = cache;
		
		int cacheType = cache.getCacheType();
		if ( ( cacheType == Constants.CACHE_CHARTS_DJ ) 
			|| ( cacheType == Constants.CACHE_CHARTS_SALES )
			|| ( cacheType == Constants.CACHE_CHARTS_DJ_RECOMMENDED ) )
		{
			itemTerm = "charts";
		}
		
		initWidget( createPageNumbersPanel() );
	}
	
	
	private Panel createPageNumbersPanel()
	{
		int currentPage = cache.getCurrentPage();
		int numPages = cache.getNumPages();
		
		FlowPanel pageNumPanel = new FlowPanel();
		pageNumPanel.add( new Label( "Page: " ) );
		
		// prev page
		if ( currentPage > 1 ) {
			Label prev = new HyperlinkLabel( "< prev", new PageListener( cache, currentPage - 1 ) );
			prev.setWordWrap( false );
			
			pageNumPanel.add( prev );
			pageNumPanel.add( new Label( " " ) );
		}
		
		// page numbers
		int startPage = currentPage - ( currentPage % NUM_PAGES );
		int endPage = startPage + NUM_PAGES;
		
		if ( startPage == 0 ) 
			startPage++;
		else {
			Label firstPage = new HyperlinkLabel( "[1]", new PageListener( cache, 1 ) );
			pageNumPanel.add( firstPage );
			pageNumPanel.add( new Label( " " ) );
		}
			
		if ( endPage > numPages ) endPage = numPages;
		
		// iterate through page numbers to display
		int x = startPage;
		for ( ; x <= endPage; x++ )
		{
			String pageNum = x + "";
			Label pageNumLabel = null;
			
			if ( x == currentPage )	
				pageNumLabel = new Label( pageNum );
			else
				pageNumLabel = new HyperlinkLabel( pageNum, new PageListener( cache, x ) );
			
			pageNumPanel.add( pageNumLabel );
			pageNumPanel.add( new Label( " " ) );
		}
		
		if ( x < numPages ) {
			Label lastPage = new HyperlinkLabel( "[" + numPages + "]",
												new PageListener( cache, numPages ) );
			pageNumPanel.add( lastPage );
			pageNumPanel.add( new Label( " " ) );
		}
		
		// next page
		if ( cache.getNumPages() > currentPage ) {
			Label next = new HyperlinkLabel( "next >", new PageListener( cache, currentPage + 1 ) );
			next.setWordWrap( false );
			
			pageNumPanel.add( next );
		}
		
		String numReleasesText = " (" + cache.size() + " " + itemTerm + ", " + numPages + " page";
		if ( numPages > 1 ) numReleasesText += "s";
		if ( cache.size() == Constants.MAX_TRACKIDS_PER_QUERY )
			numReleasesText += " - max results/search";
		numReleasesText += ")";
		
		Label numReleasesLabel = new Label( numReleasesText );
		numReleasesLabel.setStylePrimaryName( "smallerItalicText" );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth( "100%" );
		hp.add( pageNumPanel );
		hp.add( numReleasesLabel );
		hp.setCellHorizontalAlignment( pageNumPanel, HasHorizontalAlignment.ALIGN_LEFT );
		hp.setCellHorizontalAlignment( numReleasesLabel, HasHorizontalAlignment.ALIGN_RIGHT );
		
		return hp;
	}

	
	
	private class PageListener implements ClickHandler
	{
		private int pageNum = 0;
		private Cache cache = null;
		
		public PageListener( Cache cache, int pageNum ) {
			this.pageNum = pageNum;
			this.cache = cache;
		}
		
		public void onClick( ClickEvent event ) 
		{
			cache.setCurrentPage( pageNum );
			
			viewManager.updateSearchDisplay( cache );
		}		
	}

}
