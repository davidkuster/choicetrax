package com.choicetrax.client.display;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.loaderactions.SearchDJChartsFavoritesAction;
import com.choicetrax.client.actions.loaderactions.SearchTrackFavoritesAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.TrackComponent;
import com.choicetrax.client.data.User;
import com.choicetrax.client.display.panels.decorators.BoxTabPanel;
import com.choicetrax.client.input.ClickableImageOnOff;
import com.choicetrax.client.input.HyperlinkIconDelete;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.input.SearchHyperlink;
import com.choicetrax.client.input.UserSearchHyperlink;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class FavoritesComposite 
	extends Composite 
	implements ChoicetraxViewComponent
{
	
	private ChoicetraxViewManager viewManager = null;
	private VerticalPanel artistsPanel = null;
	private VerticalPanel labelsPanel = null;
	private VerticalPanel searchesPanel = null;
	
	private static final String LOGIN_TEXT = "Please login to load Favorites.";
	
	private static final int SEARCH_ALL_FAVORITES			= 0;
	private static final int SEARCH_ALL_FAV_ARTISTS 		= 1;
	private static final int SEARCH_ALL_FAV_LABELS			= 2;
	private static final int SEARCH_ALL_FAV_ARTISTS_CHARTS 	= 3;

	
	private BoxTabPanel tp = null;
	
	
	public FavoritesComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		VerticalPanel panel = new VerticalPanel();
		initWidget( panel );
		
		panel.add( createTabPanel() );
	}
	
	
	private Widget createTabPanel()
	{
		tp = new BoxTabPanel();
		tp.getDeckPanel().setAnimationEnabled( true );
		
		tp.add( createSearchesTab(), 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.favsearches_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.favsearches_off() ),
						ClickableImageOnOff.DEFAULT_ON ) );
		tp.add( createArtistsTab(), 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.favartist_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.favartist_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		tp.add( createLabelsTab(), 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.favlabels_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.favlabels_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		
		tp.selectTab( 0 );
		tp.setStyleName( "favoritesTabPanel" );
		
		return tp;
	}
	
	private Panel createArtistsTab()
	{
		artistsPanel = new VerticalPanel();
		artistsPanel.add( new Label( LOGIN_TEXT ) );
		
		ScrollPanel sp = new ScrollPanel( artistsPanel );
		sp.setStyleName( "favoritesScrollPanel" );
		
		// workaround necessary to get ScrollPanel to scroll
		VerticalPanel vp2 = new VerticalPanel();
		vp2.add( sp );
		
		return vp2;
	}
	
	private Panel createLabelsTab()
	{
		labelsPanel = new VerticalPanel();
		labelsPanel.add( new Label( LOGIN_TEXT ) );
		
		ScrollPanel sp = new ScrollPanel( labelsPanel );
		sp.setStyleName( "favoritesScrollPanel" );
		
		// workaround necessary to get ScrollPanel to scroll
		VerticalPanel vp2 = new VerticalPanel();
		vp2.add( sp );
		
		return vp2;
	}
	
	private Panel createSearchesTab()
	{
		searchesPanel = new VerticalPanel();
		searchesPanel.add( new Label( LOGIN_TEXT ) );
		
		ScrollPanel sp = new ScrollPanel( searchesPanel );
		sp.setStyleName( "favoritesScrollPanel" );
		
		// workaround necessary to get ScrollPanel to scroll
		VerticalPanel vp2 = new VerticalPanel();
		vp2.add( sp );
		
		return vp2;
	}
	
	
	public void selectTab( int tab )
	{
		if ( tp == null ) return;
		
		if ( tab == Constants.FAVORITES_TAB_SEARCHES )
			tp.selectTab( 0 );
		else if ( tab == Constants.FAVORITES_TAB_ARTISTS )
			tp.selectTab( 1 );
		else if ( tab == Constants.FAVORITES_TAB_LABELS )
			tp.selectTab( 2 );
	}
	
	
	public void clearDisplay() 
	{ 
		artistsPanel.clear();
		labelsPanel.clear();
		searchesPanel.clear();
		
		artistsPanel.add( new Label( LOGIN_TEXT ) );
		labelsPanel.add( new Label( LOGIN_TEXT ) );
		searchesPanel.add( new Label( LOGIN_TEXT ) );
	}
	
	
	public void setWaitingState( boolean waiting ) { }
	
	
	public void updateDisplay( LoaderResponse responseObj ) 
	{ 
		artistsPanel.clear();
		labelsPanel.clear();
		searchesPanel.clear();
		
		User user = (User) responseObj;
		
		if ( user != null )
		{
			artistsPanel.add( createFavoritesGrid( user.getFavoriteArtists() ) );
			labelsPanel.add( createFavoritesGrid( user.getFavoriteLabels() ) );
			searchesPanel.add( createFavoriteSearchesDisplay( user.getFavoriteSearchNames() ) );
		}
		else
		{
			artistsPanel.add( new Label( LOGIN_TEXT ) );
			labelsPanel.add( new Label( LOGIN_TEXT ) );
			searchesPanel.add( new Label( LOGIN_TEXT ) );
		}
	}
	
	
	private Grid createFavoritesGrid( List<TrackComponent> favList )
	{
		int artistSize = favList.size();
		if ( artistSize % 2 != 0 ) artistSize++;
		
		Grid favGrid = new Grid( artistSize / 2 , 4 );
		favGrid.setStylePrimaryName( "favoritesGrid" );
						
		favGrid.getColumnFormatter().setWidth( 0, "90px" );
		favGrid.getColumnFormatter().setWidth( 1, "15px" );
		favGrid.getColumnFormatter().setWidth( 2, "90px" );
		favGrid.getColumnFormatter().setWidth( 3, "15px" );
				
		int row = 0;
		int col = 0;
					
		Iterator<TrackComponent> artistsIt = favList.iterator();
		for ( int x=1; artistsIt.hasNext(); x++ )
		{
			TrackComponent favorite = artistsIt.next();
			
			favGrid.setWidget( row, col++, new SearchHyperlink( favorite, viewManager ) );
			favGrid.setWidget( row, col++, new HyperlinkIconDelete(  
												"Remove this Favorite", 
												new RemoveFavoriteListener( favorite ) ) );
							
			if ( x % 2 == 0 )	row++;
			if ( col >= 3 )		col = 0;
		}
		
		return favGrid;
	}
	
	
	private Grid createFavoriteSearchesDisplay( Set<String> searchNames )
	{
		Grid grid = new Grid( searchNames.size() + 4, 2 );
		grid.getColumnFormatter().setWidth( 0, "190px" );
		grid.getColumnFormatter().setWidth( 1, "15px" );
		
		// add user-defined searches
		int row = 0;
		Iterator<String> i = searchNames.iterator();
		for ( ; i.hasNext(); row++ )
		{
			String searchName = i.next();
			
			grid.setWidget( row, 0, new UserSearchHyperlink( searchName, viewManager ) );
			grid.setWidget( row, 1, new HyperlinkIconDelete(
										"Remove this search", 
										new RemoveFavoriteListener( searchName ) ) );
		}
		
		// add default searches
		HyperlinkLabel allSearch 	= new HyperlinkLabel( "All Fav Artists & Labels Search",
				new SearchFavoritesListener( SEARCH_ALL_FAVORITES ) );
		HyperlinkLabel artistSearch = new HyperlinkLabel( "All Fav Artists Track Search",
										new SearchFavoritesListener( SEARCH_ALL_FAV_ARTISTS ) );
		HyperlinkLabel labelSearch 	= new HyperlinkLabel( "All Fav Labels Track Search",
										new SearchFavoritesListener( SEARCH_ALL_FAV_LABELS ) );
		HyperlinkLabel chartSearch	= new HyperlinkLabel( "All Favorite Artists Chart Search",
										new SearchFavoritesListener( SEARCH_ALL_FAV_ARTISTS_CHARTS ) );
		
		allSearch.addStyleName( "italicText" );
		artistSearch.addStyleName( "italicText" );
		labelSearch.addStyleName( "italicText" );
		chartSearch.addStyleName( "italicText" );
		
		grid.setWidget( row++, 0, allSearch );
		grid.setWidget( row++, 0, artistSearch );
		grid.setWidget( row++, 0, labelSearch );
		grid.setWidget( row, 0, chartSearch );
		
		return grid;
	}
	
	
	
	private class RemoveFavoriteListener implements ClickHandler
	{
		private Object favObj = null;
		
		public RemoveFavoriteListener( Object obj )
		{
			this.favObj = obj;
		}
		
		public void onClick( ClickEvent event ) 
		{ 
			if ( favObj instanceof TrackComponent )
			{
				TrackComponent tc = (TrackComponent) favObj;
				String msg = "Do you want to delete " + tc.getName() + " as a Favorite?";
				
				if ( Window.confirm( msg ) )
					viewManager.getUserManager().removeFavorite( tc );
			}
			else if ( favObj instanceof String )
			{
				String searchName = (String) favObj;
				String msg = "Do you want to delete search \"" + searchName + "\"?";
				
				if ( Window.confirm( msg ) )
					viewManager.getUserManager().removeFavoriteSearch( searchName );
			}
		}
	}
	
	
	private class SearchFavoritesListener implements ClickHandler
	{
		private int searchType = -1;
		
		public SearchFavoritesListener( int type ) {
			super();
			this.searchType = type;
		}
		
		public void onClick( ClickEvent event ) 
		{
			if ( searchType == SEARCH_ALL_FAV_ARTISTS_CHARTS )
				searchChartFavorites();
			else
				searchTrackFavorites();
		}
		
		
		private void searchChartFavorites() 
		{ 
			SearchDJChartsFavoritesAction action = new SearchDJChartsFavoritesAction();
			
			String errorMsg = null;
			User user = viewManager.getCurrentUser();
			
			if ( user.getFavoriteArtists().size() > 0 )
				action.setArtists( true );
			else
				errorMsg = "You haven't picked out any favorite artists yet.";
			
			if ( action.getArtists() )
				viewManager.updateHistoryDisplay( Constants.HISTORY_FAVORITES_CHARTS
													+ action.getActionString() );
			else
				Window.alert( errorMsg
						+ "\n\n"
						+ "To add to favorites click on an artist or label name link and\n"
						+ "select 'Add [name] to Favorites' from the popup window."
						+ "\n\n"
						+ "Happy hunting!" );
		}
		
		
		private void searchTrackFavorites()
		{
			SearchTrackFavoritesAction action = new SearchTrackFavoritesAction();
			
			String errorMsg = null;
			User user = viewManager.getCurrentUser();
			
			if ( ( searchType == SEARCH_ALL_FAVORITES )
					|| ( searchType == SEARCH_ALL_FAV_ARTISTS ) )
			{
				if ( user.getFavoriteArtists().size() > 0 )
					action.setArtists( true );
				else
					errorMsg = "You haven't picked out any favorite artists yet.";
			}
			
			if ( ( searchType == SEARCH_ALL_FAVORITES )
					|| ( searchType == SEARCH_ALL_FAV_LABELS ) )
			{
				if ( user.getFavoriteLabels().size() > 0 )
					action.setLabels( true );
				else 
					errorMsg = "You haven't picked out any favorite labels yet.";
			}
			
			if ( searchType == SEARCH_ALL_FAV_ARTISTS_CHARTS )
			{
				//if ( user.getFavoriteArtists().size() > 0 )
				//	action
			}
			
			if ( action.getArtists() || action.getLabels() )
				viewManager.updateHistoryDisplay( Constants.HISTORY_FAVORITES_SEARCH
													+ action.getActionString() );
			else 
			{
				if ( ( searchType == SEARCH_ALL_FAVORITES ) 
						&& ( ! action.getArtists() ) && ( ! action.getLabels() ) )
					errorMsg = "You haven't picked out any favorite artists or labels yet.";
				
				Window.alert( errorMsg
						+ "\n\n"
						+ "To add to favorites click on an artist or label name link and\n"
						+ "select 'Add [name] to Favorites' from the popup window."
						+ "\n\n"
						+ "Happy hunting!" );
			}
		}
	}

}
