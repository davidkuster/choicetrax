package com.choicetrax.client.display.panels;

import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.handleractions.PartnerTransferAction;
import com.choicetrax.client.actions.loaderactions.LoadFormatsAndPricesAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.User;
import com.choicetrax.client.display.panels.decorators.PopupDecoratorPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.DoNothingController;
import com.choicetrax.client.logic.callbacks.FormatsAndPricesLoadController;
import com.choicetrax.client.input.HyperlinkLabel;


public class BuyPopupPanel extends PopupPanel 
{
	
	private ChoicetraxViewManager viewManager = null;
	private ReleaseDetail rd = null;
	private PartnerInventory pi = null;
	private String partnerName = null;
	
	private Widget sender = null;
	
	private PopupPanel loadingPopup = null;
	
	private int senderLeft 	= 0;
	private int senderWidth = 0;
	private int senderTop 	= 0;
	private int senderHeight = 0;
	
	
	public BuyPopupPanel( ChoicetraxViewManager manager,
							ReleaseDetail rd, 
							PartnerInventory partnerInventory, 
							Widget sender )
	{
		super( true );
		this.setAnimationEnabled( true );
		
		this.viewManager = manager;
		this.rd = rd;
		this.pi = partnerInventory;
		this.partnerName = viewManager.getConfigData().lookupPartnerName( pi.getPartnerID() );
		
		this.sender 		= sender;
		this.senderLeft 	= sender.getAbsoluteLeft();
		this.senderWidth 	= sender.getOffsetWidth();
		this.senderTop 		= sender.getAbsoluteTop();
		this.senderHeight 	= sender.getOffsetHeight();
		
		if ( pi == null )
		{
			// error
			this.add( new PopupDecoratorPanel( new Label( "PartnerInventory obj is null" ) ) );
			
			this.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
				public void setPosition(int offsetWidth, int offsetHeight) 
				{
					int left = senderLeft + senderWidth - offsetWidth;
					int top = senderTop + senderHeight;
					BuyPopupPanel.this.setPopupPosition(left, top);
				}
			});
		}
		else if ( pi.getFormatAndPrices() != null )
		{
			// we've already populated the list, so don't
			// bother making another RPC roundtrip
			this.clear();
			
			VerticalPanel vp = new VerticalPanel();	
			vp.add( createFormatsAndPricesPanel( pi ) );
			vp.add( createSearchAndSharingPanel( pi ) );
			
			//this.add( new PopupDecoratorPanel( createFormatsAndPricesPanel( pi ) ) );
			this.add( vp );
			this.setStyleName( "popup" );
			
			this.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
				public void setPosition(int offsetWidth, int offsetHeight) 
				{
					int left = senderLeft + senderWidth - offsetWidth;
					int top = senderTop + senderHeight;
					BuyPopupPanel.this.setPopupPosition(left, top);
				}
			});
		}
		else
		{
			// if format list is null, 
			// retrieve partner inventory formats
			
			loadingPopup = new PopupPanel( true );
			//loadingPopup.setStyleName( "popup" );
			loadingPopup.add( new PopupDecoratorPanel( new Label( "Loading..." ) ) );
			loadingPopup.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
				public void setPosition( int offsetWidth, int offsetHeight )
				{
					int left = senderLeft + senderWidth - offsetWidth;
					int top = senderTop + senderHeight;
					loadingPopup.setPopupPosition( left, top );
				}
			});
			
			LoadFormatsAndPricesAction action = new LoadFormatsAndPricesAction();
			action.setPartnerID( pi.getPartnerID() );
			action.setPartnerTrackID( pi.getPartnerTrackID() );
			action.setTrackID( rd.getTrackID() );
			
			//viewManager.executeAction( action, new FormatsAndPricesLoadController( viewManager, this ) );
			viewManager.deferAction( action, new FormatsAndPricesLoadController( viewManager, this ) );
		}
	}
	
	public void populateFormats( LinkedList<Format> formatList )
	{
		this.pi.setFormatAndPrices( formatList );
		
		if ( loadingPopup != null ) loadingPopup.hide();
		
		VerticalPanel vp = new VerticalPanel();	
		vp.add( createFormatsAndPricesPanel( pi ) );
		vp.add( createSearchAndSharingPanel( pi ) );
		
		//this.add( new PopupDecoratorPanel( createFormatsAndPricesPanel( pi ) ) );
		this.add( vp );
		this.setStyleName( "popup" );

		this.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) 
			{
				int left = senderLeft + senderWidth - offsetWidth;
				int top = senderTop + senderHeight;
				BuyPopupPanel.this.setPopupPosition(left, top);
			}
		});
	}
		
	
	
	private Widget createSearchAndSharingPanel( PartnerInventory pi )
	{
		Label search = new HyperlinkLabel( "Search for this track at all stores",
							new SearchAllStoresListener( rd ) );
		//search.addStyleName( "smallItalicText" );
		
		HorizontalPanel hp1 = new HorizontalPanel();
		hp1.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
		hp1.add( new HTML( "<b>Search</b> &nbsp;" ) );
		hp1.add( new Label( "  " ) );
		hp1.add( search );
		
		SharingPanel sharing = new SharingPanel( viewManager, rd );
		
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
		hp2.add( new HTML( "<b>Share</b> &nbsp;" ) );
		hp2.add( sharing );
		
		String url 		= Constants.createDirectEpLink( partnerName, pi.getReleaseID() );
		String target 	= Constants.getPartnerWindowName( partnerName );
		
		Anchor directLink = new Anchor( "View release at " + partnerName, url, target );
		directLink.addClickHandler( new DirectLinkListener( pi ) );
		
		HorizontalPanel hp3 = new HorizontalPanel();
		hp3.add( new HTML( "<b>Direct Link</b> &nbsp;" ) );
		hp3.add( directLink );
				
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing( 2 );
		vp.add( hp1 );
		vp.add( hp2 );
		vp.add( hp3 );
		
		return vp;
	}
	
	
	
	private Widget createFormatsAndPricesPanel( PartnerInventory pi )
	{
		User currentUser = viewManager.getCurrentUser();
		
		String territoryRestrictions = pi.getTerritoryRestrictions();
		boolean isRestricted = viewManager.getUserManager().isRestricted( territoryRestrictions );
		
		FlexTable table = new FlexTable();
		
		table.setWidget( 0, 0, new HTML( "<b>Add to Virtual Cart for " + partnerName + "</b>" ) );
		table.getFlexCellFormatter().setColSpan( 0, 0, 4 );
			
		table.setWidget( 1, 0, new Label( "Format" ) );
		table.setWidget( 1, 1, new Label( "Bitrate" ) );
		table.setWidget( 1, 2, new Label( "Price" ) );
		
		int row = 2;
		Iterator<Format> i = pi.getFormatAndPrices().iterator();
		while ( i.hasNext() )
		{
			Format format = i.next();
			
			Label cartLabel = new HyperlinkLabel( "Add", new BuyListener( format ) );
			if ( ( currentUser == null ) 
				|| isRestricted )
			{
				cartLabel = new Label( "Add" );
				cartLabel.setStyleName( "disabledText" );
			}
			
			table.setWidget( row, 0, new Label( format.getFormatName() ) );
			table.setWidget( row, 1, new Label( format.getBitrate() ) );
			table.setWidget( row, 2, new HTML( format.getPrice() ) );
			table.setWidget( row, 3, cartLabel );
			
			row++;
		}
		
		if ( currentUser == null )
		{
			HTML loginLabel = new HTML( 
					"(you must be logged in to add tracks to your virtual carts.)" );
			loginLabel.setStyleName( "alertText" );
			
			table.setWidget( row, 0, loginLabel );
			table.getFlexCellFormatter().setColSpan( row++, 0, 4 );
			
			if ( isRestricted )
			{
				HTML territoryLabel = new HTML( "This track has territory restrictions.<br>"
											+ "Please login or create an account to view." );
				territoryLabel.setStyleName( "alertText" );
				
				table.setWidget( row, 0, territoryLabel );
				table.getFlexCellFormatter().setColSpan( row, 0, 4 );
				table.getFlexCellFormatter().setHorizontalAlignment( row++, 0, 
													HasHorizontalAlignment.ALIGN_CENTER );
			}
		}
		else if ( isRestricted ) 
		{
			// this user is affected by the territory restrictions - warn them
			HTML territoryLabel = new HTML( "This track is restricted from your country. :(" );
			territoryLabel.setStyleName( "alertText" );

			table.setWidget( row, 0, territoryLabel );
			table.getFlexCellFormatter().setColSpan( row++, 0, 4 );
		}
					
		SimplePanel sp = new SimplePanel();
		sp.setStylePrimaryName( "formatsAndPricesPanel" );
		sp.add( table );
		
		return sp;
	}
	
	
	
	private class BuyListener implements ClickHandler
	{
		private Format format = null;
		
		public BuyListener( Format f ) {
			this.format = f;
		}
		
		public void onClick( ClickEvent event ) 
		{
			BuyPopupPanel.this.sender.addStyleName( "clickedIcon" );
			
			hide();
			
			DeferredCommand.addCommand( new Command() {
				public void execute() {
					viewManager.getUserManager().addToVirtualCart( rd, pi, format );
				}
			});
		}
	}
	
	
	private class SearchAllStoresListener implements ClickHandler
	{
		private ReleaseDetail rd = null;
		
		public SearchAllStoresListener( ReleaseDetail rd ) {
			this.rd = rd;
		}
		
		public void onClick( ClickEvent event ) 
		{
			String artist = Constants.readFirstWord( rd.getArtist().getArtistName() );
			String track = Constants.readFirstWord( rd.getTrackName() );
			String mix = Constants.readFirstWord( rd.getMixName() );
			String label = Constants.readFirstWord( rd.getLabel().getLabelName() );
			
			if ( ( artist != null ) && ( artist.indexOf( '-' ) != -1 ) )
				artist = "\"" + artist + "\"";
			
			if ( ( mix != null ) && ( ! mix.equalsIgnoreCase( "original" ) ) )
				track = track + " " + mix;
			
			ReleasesSearchAdvancedAction action = new ReleasesSearchAdvancedAction();
			action.setArtistName( artist );
			action.setTrackName( track );
			action.setLabelName( label );
			
			hide();			
			viewManager.updateHistoryDisplay( Constants.HISTORY_SEARCH 
												+ action.getActionString() );
		}
	}
	
	
	private class DirectLinkListener implements ClickHandler
	{
		private PartnerInventory pi = null;
		
		public DirectLinkListener( PartnerInventory pi ) {
			this.pi = pi;
		}
		
		public void onClick( ClickEvent event ) 
		{
			PartnerTransferAction action = new PartnerTransferAction();
			action.setPartnerID( pi.getPartnerID() );
			action.setTrackIDs( new int[] { BuyPopupPanel.this.rd.getTrackID() } );
			
			User currentUser = viewManager.getCurrentUser();
			if ( currentUser != null )
				action.setUserID( currentUser.getUserID() );
			
			viewManager.deferAction( action, new DoNothingController() );
		}
	}

}
