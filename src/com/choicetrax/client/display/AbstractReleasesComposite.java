package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.*;

import com.choicetrax.client.data.*;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.display.panels.CachePageNumbersPanel;
import com.choicetrax.client.display.panels.GenresPanel;
import com.choicetrax.client.display.panels.ListenBuyPanel;
import com.choicetrax.client.display.panels.ReleaseRatingPanel;
import com.choicetrax.client.display.panels.ReleaseMixNamePanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.CachePrefetchController;
import com.choicetrax.client.input.*;
import com.choicetrax.client.input.listeners.*;
import com.choicetrax.client.actions.loaderactions.LoadReleasesAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;


public abstract class AbstractReleasesComposite 
	extends Composite 
	implements ChoicetraxViewComponent
{
	
	protected ChoicetraxViewManager viewManager = null;
	
	private VerticalPanel vp = null;
	
	// releases cache object
	private ReleasesCache cache = null;
		
	// date time formatters
	private static DateTimeFormat monthDayFormatter	= DateTimeFormat.getFormat( "MMM dd" );
	private static DateTimeFormat yearFormatter		= DateTimeFormat.getFormat( "yyyy" );
	
	// panel code
	protected int panelCode = -1;
	
	// sort data types
	protected static final int SORT_DATATYPE_ALPHA 	= 0;
	protected static final int SORT_DATATYPE_DATE 	= 1;
	protected static final int SORT_DATATYPE_DIGIT	= 2;
	
	private static final String[] SORT_ASC_TEXT = { "A-Z", "old to new", "low to high" };
	private static final String[] SORT_DESC_TEXT = { "Z-A", "new to old", "high to low" };
	
	
		
	public AbstractReleasesComposite( ChoicetraxViewManager manager, int panelCode )
	{
		this.viewManager = manager;
		this.panelCode = panelCode;
		
		vp = new VerticalPanel();
		initWidget( vp );
	}
	
	
	protected ReleasesCache getCache() {
		return this.cache;
	}
	
	
	
	protected Widget createTrackNamePanel( ReleaseDetail rd )
	{
		String trackName = rd.getTrackName();
		String albumName = rd.getAlbumName();
		
		FlowPanel fp = new FlowPanel();
		
		// if album name is not the same as the track name, include it in the display 
		if ( ( albumName != null ) 
				&& ( ! albumName.equals( "" ) )
				&& ( ! albumName.equalsIgnoreCase( trackName ) ) ) 
		{
			Label album = new Label( albumName );
			album.setStylePrimaryName( "italicText" );
			
			fp.add( album );
			fp.add( new Label( " / " ) );
		}
		
		fp.add( new Label( trackName ) );
		
		return fp;
	}
	
	
	protected abstract Widget createActionHeader();
	
	
	protected abstract ListenBuyPanel createListenBuyPanel( ReleaseDetail rd );
	
	
	protected Widget createHeaderWidget( int cacheType )
	{
		String headerText = "Unknown Cache Type";
		
		if ( ( Constants.CACHE_RELEASES_SEARCH == cacheType )
				|| ( Constants.CACHE_RELEASES_QUICKSEARCH == cacheType )
				|| ( Constants.CACHE_RELEASES_FAVORITES == cacheType ) )
			headerText = "Search Results";
		else if ( Constants.CACHE_RELEASES_BROWSE == cacheType )
			headerText = "Browse Results";
		else if ( Constants.CACHE_RELEASES_WISHLIST == cacheType )
			headerText = "Wishlist";
		else if ( Constants.CACHE_RELEASES_PURCHASE_HISTORY == cacheType )
			headerText = "Purchase History";
		else if ( Constants.CACHE_RELEASES_RECOMMENDED == cacheType )
			headerText = "Recommended Results";
		else if ( Constants.CACHE_RELEASES_VIRTUAL_CART == cacheType )
			headerText = "Virtual Cart Trax";
		
		Label headerLabel = new Label( headerText );
		headerLabel.setStylePrimaryName( "mainPanelHeaderLabel" );
		
		return headerLabel;
	}
	
	
	
	public void updateDisplay( LoaderResponse response )
	{
		clearDisplay();
		
		FlexTable table = new FlexTable();
		table.setWidth( "100%" );
		table.setCellPadding( 2 );
		table.setCellSpacing( 0 );
		
		HTMLTable.CellFormatter cellFormatter = table.getCellFormatter();
		
		cache = (ReleasesCache) response;
		Releases releases = cache.getReleasesForPage( cache.getCurrentPage() );
		
		// add column headers
		createTableHeaders( table, cache.getCacheType() );
			
		int row = 3;
		if ( ( releases != null ) && ( releases.size() > 0 ) ) 
		{
			Iterator<ReleaseDetail> i = releases.getReleasesList().iterator();
			while ( i.hasNext() )
			{
			//DeferredCommand.addCommand( new IncrementalCommand() {
			//	private int row = 3;
			//	public boolean execute() 
			//	{
			//		if ( ! i.hasNext() )
			//			return false;
			//		else
			//		{
				int y = 0;
				ReleaseDetail rd = i.next();
				
				// add blue bg to every other row
				if ( row % 2 == 0 ) table.getRowFormatter().addStyleName( row, "altRow" );
				
				// EP
				table.setWidget( row, y++, createEPLink( rd ) );
				
				// artist name
				table.setWidget( row, y++, createArtistNamePanel( rd ) );
				
				// track name
				table.setWidget( row, y++, createTrackNamePanel( rd ) );
				
				// (mix name) [remixer]
				table.setWidget( row, y++, new ReleaseMixNamePanel( viewManager, rd ) );
				
				// time
				//table.setWidget( row, y, createTimePanel( rd ) );
										
				// label name
				table.setWidget( row, y++, createLabelNamePanel( rd ) );
				
				// genres
				table.setWidget( row, y++, new GenresPanel( viewManager, rd.getGenres() ) );
				
				// release date
				table.setWidget( row, y++, createReleaseDatePanel( rd.getReleaseDate() ) );
											
				ListenBuyPanel listenBuy = createListenBuyPanel( rd );
				// listen / buy
				table.setWidget( row, y++, listenBuy.getListenBuyPanel() );
				
				// wishlist
				table.setWidget( row, y, listenBuy.getActionPanel() );
				cellFormatter.setHorizontalAlignment( row, y++, 
								HasHorizontalAlignment.ALIGN_CENTER );
				
				// rating
				table.setWidget( row, y, createRatingPanel( rd ) );
				cellFormatter.setHorizontalAlignment( row, y++, 
								HasHorizontalAlignment.ALIGN_CENTER );
				
				row++;
		
			}
			//		return true;
			//	}
			//});
		}
		else
		{
			// if no results returned, show help text 
			table.setWidget( row, 2, createHelpInfo() );
			table.getFlexCellFormatter().setColSpan( row, 2, 6 );
		}
			
		// create releases header
		Widget header = createHeaderWidget( cache.getCacheType() );
		if ( header != null ) {
			vp.add( header );
			vp.setCellHorizontalAlignment( header, HasHorizontalAlignment.ALIGN_CENTER );
		}
				
		// create page number display
		vp.add( new CachePageNumbersPanel( viewManager, cache ) );
	
		vp.add( new HTML( "&nbsp;" ) );	// spacer
		
		vp.add( table );
		
		vp.add( new HTML( "&nbsp;" ) ); // spacer

		vp.add( new CachePageNumbersPanel( viewManager, cache ) );
		
				
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				// show appropriate composite in the center panel 
				viewManager.showCenterPanel( panelCode );
			}
		});
				
		// prefetch next page, and previous page (if not populated already)
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				prefetchPage( cache, cache.getCurrentPage() + 1 );
				prefetchPage( cache, cache.getCurrentPage() - 1 );
			}
		});
	}
	
	
	public void clearDisplay() 
	{ 
		vp.clear();
	}
	
	
	public void setWaitingState( boolean waiting ) { }
	
	
	
	
	protected void createTableHeaders( FlexTable table, int cacheType )
	{
		int column = 0;
		
		boolean sort = false;
		if ( ( cacheType == Constants.CACHE_RELEASES_BROWSE )
				|| ( cacheType == Constants.CACHE_RELEASES_FAVORITES )
				|| ( cacheType == Constants.CACHE_RELEASES_QUICKSEARCH )
				|| ( cacheType == Constants.CACHE_RELEASES_SEARCH ) )
				//|| ( cacheType == Constants.CACHE_RELEASES_RECOMMENDED ) )
			sort = true;
		
		table.setWidget( 1, column, new Label( "EP" ) );
		
		table.setWidget( 1, ++column, new Label( "Artist" ) );
		
		if ( sort )
			table.setWidget( 2, column, createHeaderSorts( "Artist Name", 
															Constants.RELEASES_SORT_BY_ARTIST,
															SORT_DATATYPE_ALPHA ) );
		
		table.setWidget( 1, ++column, new Label( "Album / Track" ) );
		
		if ( sort )
			table.setWidget( 2, column, createHeaderSorts( "Track Name", 
															Constants.RELEASES_SORT_BY_TRACK,
															SORT_DATATYPE_ALPHA ) );
		
		table.setWidget( 1, ++column, new Label( "Mix / Remixer" ) );
		
		if ( sort )
			table.setWidget( 2, column, createHeaderSorts( "Mix Name", 
															Constants.RELEASES_SORT_BY_MIX,
															SORT_DATATYPE_ALPHA ) );
		
		/*table.setWidget( 1, ++column, new Label( "Time" ) );*/
		
		table.setWidget( 1, ++column, new Label( "Label" ) );
		
		if ( sort )
			table.setWidget( 2, column, createHeaderSorts( "Label Name", 
															Constants.RELEASES_SORT_BY_LABEL,
															SORT_DATATYPE_ALPHA ) );
		
		table.setWidget( 1, ++column, new Label( "Genres" ) );
		
		table.setWidget( 1, ++column, new Label( "Date" ) );
		
		if ( sort )
			table.setWidget( 2, column, createHeaderSorts( "Release Date", 
															Constants.RELEASES_SORT_BY_DATE,
															SORT_DATATYPE_DATE ) );
		
		Image playOptions = new ClickableImage( "img/layout/iconListen_sm.gif" );
		playOptions.addClickHandler( new PlayOptionsListener( viewManager, cache ) );
		playOptions.setTitle( "play audio options" );
		
		if ( sort ) {
			table.setWidget( 1, ++column, new Label( "Buy / Listen" ) );
			table.setWidget( 2, column, playOptions );
		} else {
			FlowPanel buyListenPanel = new FlowPanel();
			buyListenPanel.add( new Label( "Buy / Listen " ) );
			buyListenPanel.add( playOptions );
			
			table.setWidget( 1, ++column, buyListenPanel );
		}
		
		table.setWidget( 1, ++column, createActionHeader() );
		
		table.setWidget( 1, ++column, new Label( "Rating" ) );
		
		if ( sort )
			table.setWidget( 2, column, createHeaderSorts( "Rating", 
															Constants.RELEASES_SORT_BY_RATING,
															SORT_DATATYPE_DIGIT ) );
		
		table.getRowFormatter().setStyleName( 1, "headerRow" );
		
		if ( sort ) {
			table.getRowFormatter().setStyleName( 2, "headerRow" );		
			table.getRowFormatter().setVerticalAlign( 1, HasVerticalAlignment.ALIGN_BOTTOM );
			table.getRowFormatter().setVerticalAlign( 2, HasVerticalAlignment.ALIGN_TOP );
		} else  {
			table.getRowFormatter().setVerticalAlign( 1, HasVerticalAlignment.ALIGN_MIDDLE );
		}
	}
	
	
	protected Widget createHeaderSorts( String title, String sortByType, int dataType )
	{
		Image sortAsc = new ClickableImage( "img/layout/iconSortAsc.gif" );
		sortAsc.addClickHandler( new SortSearchListener( viewManager,
															sortByType, 
															Constants.SORT_ORDER_ASCENDING ) );
		sortAsc.setTitle( "sort by " + title + " " + SORT_ASC_TEXT[ dataType ] );
		
		Image sortDesc = new ClickableImage( "img/layout/iconSortDesc.gif" );
		sortDesc.addClickHandler( new SortSearchListener( viewManager,
															sortByType,
															Constants.SORT_ORDER_DESCENDING ) );
		sortDesc.setTitle( "sort by " + title + " " + SORT_DESC_TEXT[ dataType ] );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing( 2 );
		hp.add( sortAsc );
		hp.add( sortDesc );
		
		return hp;
	}
	
	
	
	
	private void prefetchPage( Cache cache, int pageNumToPrefetch )
	{
		// don't try and prefetch page outside of cache
		if ( ( pageNumToPrefetch > 0 ) && ( pageNumToPrefetch <= cache.getNumPages() ) )
		{
			// check if next page is loaded, and if not, prefetch it
			if ( ! cache.isPagePopulated( pageNumToPrefetch ) )
			{
				// load releases from server, but don't update display
				int[] trackIDs = cache.getIDsForPage( pageNumToPrefetch );
				
				LoadReleasesAction action = new LoadReleasesAction( trackIDs );
				if ( viewManager.getCurrentUser() != null )
					action.setUserID( viewManager.getCurrentUser().getUserID() );
				
				viewManager.deferAction( action, new CachePrefetchController( 
													viewManager, cache, pageNumToPrefetch ) );
			}
		}
	}
	
	
	
	private Widget createEPLink( ReleaseDetail rd )
	{
		if ( rd.getCoverArtURL() != null ) 	{
			return new HyperlinkIconCoverArt( viewManager, rd );
		}
		else {
			EPListener epListener = new EPListener( viewManager, rd.getTrackID() );
			return new HyperlinkLabel( "EP", epListener );
		}
	}
	
	
	private Widget createArtistNamePanel( ReleaseDetail rd )
	{
		Artist artist = rd.getArtist();
		Artist vocalist = rd.getVocalist();
		
		Label artistLabel = new TrackHyperlink( artist, viewManager );
		
		FlowPanel artistPanel = new FlowPanel();
		artistPanel.add( artistLabel );
		
		if ( ( vocalist != null ) && ( vocalist.getArtistID() > 0 ) )
		{
			Label vocalistLabel = null;
			// don't enable hyperlinks for non-legit artist names
			if ( "Y".equals( vocalist.getLegitName() ) )
				vocalistLabel = new TrackHyperlink( vocalist, viewManager );
			else
				vocalistLabel = new Label( vocalist.getArtistName() );
			
			artistPanel.add( new Label( " feat. " ) );
			artistPanel.add( vocalistLabel );
		}
		return artistPanel;
	}
	
	
	/*
	private Widget createTimePanel( ReleaseDetail rd )
	{
		String time = rd.getTimeLength();
		if ( ( time != null ) && ( ! time.trim().equals("") )
			&& ( ! time.equals( "null" ) ) )
		{
			Label timeLabel = new Label( time );
			timeLabel.setStylePrimaryName( "smallerItalicText" );
			return timeLabel;
		}
		else
			return new HTML( "&nbsp;" );
	}*/
	
	
	
	private Widget createLabelNamePanel( ReleaseDetail rd )
	{
		RecordLabel label = rd.getLabel();
		
		Label labelLabel = new TrackHyperlink( label, viewManager );
			
		return labelLabel;
	}
	
	
	private Panel createReleaseDatePanel( Date date )
	{
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.setWidth( "100%" );
		
		if ( date != null )
		{
			Label monthDay = new Label( monthDayFormatter.format( date ) );
			monthDay.setWordWrap( false );
			
			Label year = new Label( yearFormatter.format( date ) );
			
			monthDay.setStylePrimaryName( "smallerItalicText" );
			year.setStylePrimaryName( "smallerItalicText" );
			
			vp.add( monthDay );
			vp.add( year );
		}
		else
		{
			Label tbd = new Label( "TBD" );
			tbd.setStylePrimaryName( "smallerItalicText" );
			
			vp.add( tbd );
		}
		
		return vp;
	}
	
	
	
	private Composite createRatingPanel( ReleaseDetail rd )
	{
		ReleaseRatingPanel ratingPanel = new ReleaseRatingPanel( viewManager, rd );
		
		return ratingPanel;
	}
	
	
	
	protected Widget createHelpInfo()
	{
		final String actionString = cache.getActionString();
		
		String text1 = "<p><br>"
					+ "<center><b>Your search returned no results.</b></center>"
					+ "<p>"
					+ "It's possible you have a typo, or that the tracks you're looking for "
					+ "aren't currently available digitally or are not available at our "
					+ "partner stores. "
					+ "<p>";
		
		Label search = null;
		if ( ( actionString != null ) && ( actionString.indexOf( ' ' ) != -1 ) )
		{
			search = new HyperlinkLabel( 
						"Click here to try a broader search using the above terms.",
						new ClickHandler() {
							public void onClick( ClickEvent event ) 
							{
								 String as = Constants.createHistoryToken( 
										 		cache.getCacheType(),
										 		actionString.replaceAll( " ", " | " ), 
										 		cache.getCurrentPage() );
								 viewManager.updateHistoryDisplay( as );
							}
						});
		}
		
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
		
		if ( search != null )
			vp.add( search );
		
		vp.add( new HTML( text2 ) );
		
		return vp;
	}
	
	
}
