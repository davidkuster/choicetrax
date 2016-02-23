package com.choicetrax.client.display;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.actions.loaderactions.LoadDJChartsAction;
import com.choicetrax.client.actions.loaderactions.ViewDJChartAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Artist;
import com.choicetrax.client.data.DJChartDetail;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.display.panels.CachePageNumbersPanel;
import com.choicetrax.client.display.panels.GenresPanel;
import com.choicetrax.client.input.HyperlinkImage;
import com.choicetrax.client.input.TrackHyperlink;
import com.choicetrax.client.input.listeners.DJChartImageLoadListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.CachePrefetchController;


public abstract class AbstractDJChartSelectComposite 
	extends Composite 
	implements ChoicetraxViewComponent
{
	
	private ChoicetraxViewManager viewManager = null;
	
	private VerticalPanel vp = null;
	
	protected int panelCode = -1;
	
	
	
	public AbstractDJChartSelectComposite( ChoicetraxViewManager manager, int panelCode ) {
		super();
		
		this.viewManager = manager;
		this.panelCode = panelCode;
		
		vp = new VerticalPanel();	
		vp.setWidth( "100%" );
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		
		initWidget( vp );
	}
	
	
	

	public void clearDisplay() {
		vp.clear();
	}

	public void setWaitingState( boolean waiting ) {
	}
	
	
	
	protected abstract Widget createHeaderWidget();
	
	
	

	public void updateDisplay( LoaderResponse responseObj ) 
	{
		clearDisplay();
		
		FlexTable table = new FlexTable();
		table.setWidth( "100%" );
		
		final DJChartsCache cache = (DJChartsCache) responseObj;
		DJCharts charts = cache.getChartsForPage( cache.getCurrentPage() );
		LinkedList<DJChartDetail> chartList = charts.getChartsList();
		if ( chartList == null ) return;
		
		table.setWidget( 0, 0, new CachePageNumbersPanel( viewManager, cache ) );
		table.getFlexCellFormatter().setColSpan( 0, 0, 2 );
		
		table.setWidget( 1, 0, new HTML( "&nbsp;" ) );
		
		int row = 2;
		int col = 0;
		
		if ( chartList.size() > 0 )
		{
			Iterator<DJChartDetail> i = chartList.iterator();
			while ( i.hasNext() ) 
			{
				DJChartDetail chartDetail = i.next();
				Widget chartDisplay = createChartDisplay( chartDetail );
				
				table.setWidget( row, col, chartDisplay );
				table.getFlexCellFormatter().setHorizontalAlignment( row, col, 
												HasHorizontalAlignment.ALIGN_LEFT );
				
				col++;			
				if ( col == 2 ) {
					col = 0;
					row++;
				}
			}
		}
		else
		{
			// if no results returned, show help text 
			table.setWidget( row, 0, createHelpInfo() );
			table.getFlexCellFormatter().setColSpan( row, 0, 2 );
		}
		
		table.setWidget( ++row, 0, new HTML( "&nbsp;" ) );
		table.setWidget( ++row, 0, new CachePageNumbersPanel( viewManager, cache ) );
		table.getFlexCellFormatter().setColSpan( row, 0, 2 );
		
		Widget header = createHeaderWidget();
		
		vp.add( header );
		vp.setCellHorizontalAlignment( header, HasHorizontalAlignment.ALIGN_CENTER );
		vp.add( table );
		
		
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				// show appropriate composite in the center panel 
				viewManager.showCenterPanel( panelCode );
			}
		});
		
		// prefetch next page
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				//prefetchNextPage( cache );
				prefetchPage( cache, cache.getCurrentPage() + 1 );
				prefetchPage( cache, cache.getCurrentPage() - 1 );
			}
		});
	}
		
	
	private Widget createChartDisplay( DJChartDetail chartDetail )
	{
		Artist artistObj = chartDetail.getArtist();
		String artistName = chartDetail.getArtistName();
		String chartName = chartDetail.getChartName();
		String chartDate = chartDetail.getChartDate();
		String imageURL = chartDetail.getImageURL();
		int partnerID = chartDetail.getPartnerID();
		String partnerName = viewManager.getConfigData().lookupPartnerName( partnerID );
		List<Genre> genres = chartDetail.getGenres();
		
		Label artistLabel = null;
		if ( artistObj != null ) 
			artistLabel = new TrackHyperlink( artistObj, viewManager );
		else if ( artistName != null )
			artistLabel = new Label( artistName );
		
		HyperlinkImage img = new HyperlinkImage( imageURL,
									"View Chart",
									new DJChartSelectListener( chartDetail.getChartID() ) );
		img.setSize( "100px", "100px" );
		
		DJChartImageLoadListener handler = new DJChartImageLoadListener();
		img.addLoadHandler( handler );
		img.addErrorHandler( handler );
		
		Label partnerLabel = new Label( partnerName + " chart" );
		partnerLabel.setStylePrimaryName( "boldText" );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing( 2 );
		
		if ( artistLabel != null )
			vp.add( artistLabel );
		
		vp.add( new HTML( chartName ) );
		vp.add( new Label( chartDate ) );
		vp.add( new GenresPanel( viewManager, genres ) );
		
		FlexTable table = new FlexTable();
		
		table.setWidget( 0, 0, partnerLabel );
		table.getFlexCellFormatter().setColSpan( 0, 0, 2 );
		table.setWidget( 1, 0, img );
		table.setWidget( 1, 1, vp );
		table.getFlexCellFormatter().setVerticalAlignment( 1, 1, 
										HasVerticalAlignment.ALIGN_TOP );
		//table.setWidget( 2, 0, new HTML( "&nbsp;" ) );
		
		SimplePanel sp = new SimplePanel();
		sp.setStylePrimaryName( "chartInfo" );
		sp.add( table );
		
		return sp;
	}
	
	
	
	protected Widget createHelpInfo()
	{
		String text1 = "<p><br>"
					+ "<center><b>Your search returned no results.</b></center>"
					+ "<p>"
					+ "It's possible you have a typo, or that the tracks you're looking for "
					+ "aren't currently available digitally or are not available at our "
					+ "partner stores. "
					+ "<p>";
		
		
		String text2 = "<p><br><p>"
					+ "<b>Tips on searching:</b>"
					+ "<p>"
					+ "Don't use accented characters."
					+ "<p><br>"
					+ "You can use the - operator for exclusion. "
					+ "<p>"
					+ "Example: <i>search for this -\"not this\"</i>"
					+ "<p><br>"
					+ "Use quotes when the artist name includes a hyphen. "
					+ "<p>"
					+ "Example: <i>\"Hipp-E\"</i>"
					+ "<p><br>"
					+ "You can use the | operator for OR searches. "
					+ "<p>"
					+ "Example: <i>artist | track | label</i>"
					+ "<p><br>";
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( new HTML( text1 ) );
		
		vp.add( new HTML( text2 ) );
		
		return vp;
	}
	
	/*private void prefetchNextPage( Cache cache )
	{
		int nextPage = cache.getCurrentPage() + 1;
		
		// don't try and prefetch page outside of cache
		if ( nextPage <= cache.getNumPages() )
		{
			// check if next page is loaded, and if not, prefetch it
			if ( ! cache.isPagePopulated( nextPage ) )
			{
				// load charts from server, but don't update display
				int[] chartIDs = cache.getIDsForPage( nextPage );
				
				LoadDJChartsAction action = new LoadDJChartsAction( chartIDs );
				if ( viewManager.getCurrentUser() != null )
					action.setUserID( viewManager.getCurrentUser().getUserID() );
				
				viewManager.deferAction( 
								action, 
								new CachePrefetchController( viewManager, cache, nextPage ) );
			}
		}
	}*/
	
	private void prefetchPage( Cache cache, int pageNumToPrefetch )
	{
		// don't try and prefetch page outside of cache
		if ( ( pageNumToPrefetch > 0 ) && ( pageNumToPrefetch <= cache.getNumPages() ) )
		{
			// check if next page is loaded, and if not, prefetch it
			if ( ! cache.isPagePopulated( pageNumToPrefetch ) )
			{
				// load charts from server, but don't update display
				int[] chartIDs = cache.getIDsForPage( pageNumToPrefetch );
				
				LoadDJChartsAction action = new LoadDJChartsAction( chartIDs );
				if ( viewManager.getCurrentUser() != null )
					action.setUserID( viewManager.getCurrentUser().getUserID() );
				
				viewManager.deferAction( action, new CachePrefetchController( 
														viewManager, cache, pageNumToPrefetch ) );
			}
		}
	}
	
	
	private class DJChartSelectListener implements ClickHandler
	{
		private int chartID = 0;
		
		public DJChartSelectListener( int chartID ) {
			this.chartID = chartID;
		}
		
		public void onClick( ClickEvent event ) 
		{
			ViewDJChartAction action = new ViewDJChartAction();
			action.setChartID( chartID );
						
			viewManager.updateHistoryDisplay( Constants.HISTORY_CHARTS_DJ_VIEW
												+ action.getActionString() );
		}
	}
	
}
