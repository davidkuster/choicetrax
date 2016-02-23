package com.choicetrax.client.input;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.*;
import com.choicetrax.client.data.Artist;
import com.choicetrax.client.data.RecordLabel;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.TrackComponent;
import com.choicetrax.client.display.panels.decorators.PopupDecoratorPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.constants.Constants;


public class TrackHyperlink extends Label 
{
	protected ChoicetraxViewManager viewManager = null;
	protected TrackComponent trackComponent = null;
	private PopupPanel popup = null;
		
	
	public TrackHyperlink() {
		super();
	}
	
	public TrackHyperlink( String name ) {
		super( name );
	}

	public TrackHyperlink( TrackComponent tc, ChoicetraxViewManager manager )
	{
		super( tc.getName() );

		this.setWordWrap( true );
		
		this.trackComponent = tc;
		this.viewManager = manager;
		this.addClickHandler( new LinkListener() );
		this.setStyleName( "labelLink" );
		
		// put this in a deferred command to increase UI responsiveness, 
		// especially as user's favorites size increases 
		final TrackComponent component = tc;
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				if ( viewManager.getUserManager().isFavorite( component ) )
					TrackHyperlink.this.addStyleName( "favoritesLabel" );
			}
		});
	}
	

	
	private class LinkListener 
		implements ClickHandler 
	{
		public void onClick( ClickEvent event ) 
		{
			Label searchLabel = new Label( "Search for " + trackComponent.getName() );
			searchLabel.addClickHandler( new SearchListener() );
			searchLabel.setStyleName( "labelLink" );
						
			Label favoriteLabel = new Label( "Add " + trackComponent.getName() + " to Favorites" );
			favoriteLabel.addClickHandler( new AddFavoriteListener() );
			favoriteLabel.setStyleName( "labelLink" );
			
			VerticalPanel vp = new VerticalPanel();
			vp.setSpacing( 3 );
			
			vp.add( searchLabel );
			vp.add( favoriteLabel );
			
			Widget sender = (Widget) event.getSource();
			
			// show popup
			popup = new PopupPanel( true );
			popup.setAnimationEnabled( true );
			popup.add( new PopupDecoratorPanel( vp ) );
			popup.show();
			popup.setPopupPosition( sender.getAbsoluteLeft(), sender.getAbsoluteTop() + sender.getOffsetHeight() );
			//popup.setStyleName( "popup" );
		}
	}
	
		
	private class AddFavoriteListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			if ( popup != null ) 
				popup.hide();
			
			TrackHyperlink.this.addStyleName( "favoritesLabel" );
			
			DeferredCommand.addCommand( new Command() {
				public void execute() {
					viewManager.getUserManager().addFavorite( trackComponent );
				}
			});
		}
	}
	
	
	protected class SearchListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			if ( popup != null ) 
				popup.hide();
			
			ReleasesSearchAdvancedAction action = new ReleasesSearchAdvancedAction();

			if ( trackComponent instanceof Artist )
				action.setArtistName( trackComponent.getName() );
			else if ( trackComponent instanceof RecordLabel )
				action.setLabelName( trackComponent.getName() );
			else if ( trackComponent instanceof Genre )
				action.setGenreIDs( new int[] { trackComponent.getID() } );
			
			if ( viewManager.getCurrentUser() != null )
				action.setUserID( viewManager.getCurrentUser().getUserID() );
			
			final String actionString = action.getActionString();
			
			DeferredCommand.addCommand( new Command() {
				public void execute() {
					viewManager.updateHistoryDisplay( Constants.HISTORY_SEARCH + actionString );
				}
			});
		}
	}
	
}
