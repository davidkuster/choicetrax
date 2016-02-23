package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.display.panels.decorators.PopupDecoratorPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class PlayOptionsListener implements ClickHandler
{
	
	private ChoicetraxViewManager viewManager = null;
	private ReleasesCache cache = null;
	private PopupPanel popup = null;
	
	
	public PlayOptionsListener( ChoicetraxViewManager manager, ReleasesCache cache ) {
		this.viewManager = manager;
		this.cache = cache;
	}
	
	
	public void onClick( ClickEvent event )
	{
		Label playPageLabel = new Label( "Play Page" );
		playPageLabel.setStylePrimaryName( "labelLink" );
		playPageLabel.addClickHandler( new ClickHandler() {
			public void onClick( ClickEvent event ) {
				popup.hide();
				viewManager.getListenManager().playPage( cache, cache.getCurrentPage() );
			}
		});
		Label playPageHelp = new Label( "(listen to all tracks on this page)" );
		playPageHelp.setStylePrimaryName( "italicText" );
					
		Label playOptionLabel = null;
		Label playOptionHelp = null;
		if ( ( cache.getCacheType() == Constants.CACHE_RELEASES_SEARCH )
				|| ( cache.getCacheType() == Constants.CACHE_RELEASES_QUICKSEARCH )
				|| ( cache.getCacheType() == Constants.CACHE_RELEASES_BROWSE )
				|| ( cache.getCacheType() == Constants.CACHE_RELEASES_DJ_TRACKS ) 
				|| ( cache.getCacheType() == Constants.CACHE_RELEASES_RECOMMENDED )
				|| ( cache.getCacheType() == Constants.CACHE_RELEASES_FAVORITES ) ) 
		{
			playOptionLabel = new Label( "Play Random" );
			playOptionLabel.addClickHandler( new ClickHandler() {
				public void onClick( ClickEvent event ) {
					popup.hide();
					viewManager.getListenManager().playRandom( cache );
				}
			});
			playOptionHelp = new Label( "(listen to up to 100 clips in random order)" );
		} 
		else 
		{
			playOptionLabel = new Label( "Play All" );
			playOptionLabel.addClickHandler( new ClickHandler() {
				public void onClick( ClickEvent event ) {
					popup.hide();
					viewManager.getListenManager().playAll( cache );
				}
			});
			playOptionHelp = new Label( "(listen to all audio clips in "
										+ determineAllText() + ")" );
		}
		playOptionLabel.setStylePrimaryName( "labelLink" );
		playOptionHelp.setStylePrimaryName( "italicText" );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_RIGHT );
		vp.setSpacing( 3 );
		
		vp.add( playPageLabel );
		vp.add( playPageHelp );
		vp.add( playOptionLabel );
		vp.add( playOptionHelp );
		
		if ( ! viewManager.getListenManager().isQueued() ) 
		{
			HTML queueAlert = new HTML( "(Please note that these options may work best<br>"
										+ "when the audio player is in queue mode.)" );
			queueAlert.setStylePrimaryName( "alertText" );
			
			vp.add( queueAlert );
		}
		
		Widget sender = (Widget) event.getSource();
		final int senderLeft 	= sender.getAbsoluteLeft();
		final int senderWidth 	= sender.getOffsetWidth();
		final int senderTop 	= sender.getAbsoluteTop();
		final int senderHeight 	= sender.getOffsetHeight();
		
		// show popup
		popup = new PopupPanel( true );
		popup.setAnimationEnabled( true );
		popup.add( new PopupDecoratorPanel( vp ) );
		popup.show();
		popup.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) 
			{
				int left = senderLeft + senderWidth - offsetWidth;
				int top = senderTop + senderHeight;
				popup.setPopupPosition( left, top );
			}
		});
	}
	
	
	private String determineAllText() 
	{
		int cacheType = cache.getCacheType();
		
		if ( cacheType == Constants.CACHE_RELEASES_WISHLIST )
			return "wishlist";
		else if ( cacheType == Constants.CACHE_RELEASES_VIRTUAL_CART )
			return "this store's virtual cart";
		else if ( cacheType == Constants.CACHE_RELEASES_PURCHASE_HISTORY )
			return "this store's purchase history";
		else if ( cacheType == Constants.CACHE_RELEASES_DJ_TRACKS )
			return "this DJ chart";
		else 
			return "this page";
	}
	
	
}
