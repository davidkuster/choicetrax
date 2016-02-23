package com.choicetrax.client.display.panels;

import java.util.*;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.*;

import com.choicetrax.client.data.*;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.input.HyperlinkIconBuy;
import com.choicetrax.client.input.HyperlinkIconListen;
import com.choicetrax.client.input.HyperlinkImage;
import com.choicetrax.client.input.listeners.*;


public class ListenBuyPanel 
	//extends Composite
{
	protected ChoicetraxViewManager viewManager = null;
	protected ReleaseDetail releaseDetail = null;
	protected int cacheType = -1;
	
	protected DisclosurePanel parentPanel = new DisclosurePanel();
	protected VerticalPanel actionPanel = new VerticalPanel();
		
	// number of listen/buy icons to display per row
	protected static final int NUM_ICONS_PER_ROW = 3;
	
	
	
	/**
	 * constructor and UI generation for ListenBuyPanel
	 * @param ReleaseDetail rd
	 */
	public ListenBuyPanel( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		this.viewManager = manager;
		this.releaseDetail = rd;
		
		//initWidget( parentPanel );
		parentPanel.setAnimationEnabled( true );
				
		parentPanel.setHeader( createListenBuyPanel() );
	}
	
	
	public DisclosurePanel getListenBuyPanel()
	{
		return parentPanel;
	}
	
	
	public Panel getActionPanel()
	{
		return actionPanel;
	}
	
	
	protected Panel createListenBuyPanel() 
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		int inventoryNum = 0;
		
		LinkedList<PartnerInventory> partnerInventory = this.releaseDetail.getPartnerAvailability();
		if ( partnerInventory != null ) 
		{
			inventoryNum = partnerInventory.size();
			
			hp.add( createListenBuyRow( partnerInventory, 0, NUM_ICONS_PER_ROW ) );
		}
		
		Widget actionButton = createActionButton(); 
		//Widget optionButton = new HyperlinkIconOptions( viewManager, releaseDetail );
				
		actionPanel.setSpacing( 2 );
		
		actionPanel.add( actionButton );
		actionPanel.setCellVerticalAlignment( actionButton, HasVerticalAlignment.ALIGN_TOP );
		actionPanel.setCellHorizontalAlignment( actionButton, HasHorizontalAlignment.ALIGN_CENTER );
		
		//actionPanel.add( optionButton );
		//actionPanel.setCellVerticalAlignment( optionButton, HasVerticalAlignment.ALIGN_MIDDLE );
		//actionPanel.setCellHorizontalAlignment( optionButton, HasHorizontalAlignment.ALIGN_CENTER );
		
		if ( inventoryNum > NUM_ICONS_PER_ROW )
		{
			Widget moreButton = createMoreButton( partnerInventory, 
													inventoryNum - NUM_ICONS_PER_ROW, 
													NUM_ICONS_PER_ROW );
			
			actionPanel.add( moreButton );
			actionPanel.setCellVerticalAlignment( moreButton, HasVerticalAlignment.ALIGN_BOTTOM );
			actionPanel.setCellHorizontalAlignment( moreButton, HasHorizontalAlignment.ALIGN_CENTER );
			//actionPanel.setSpacing( 1 );
		}
				
		return hp;
	}
	

	
	protected Widget createMoreButton( LinkedList<PartnerInventory> partnerInventory, 
										int extraInventory, 
										int numInitial )
	{
		//Button moreButton = new Button( "+" + extraInventory );
		Image moreButton = new Image( "img/layout/iconMore.gif" );
		
		String title = "This release is available at " + extraInventory 
						+ " additional download store";
		if ( extraInventory > 1 ) title += "s";
		title += ".";
		moreButton.setTitle( title );
		moreButton.addClickHandler( new MoreIconListener( partnerInventory, numInitial ) );
		moreButton.addStyleName( "clickableImage" );
		
		return moreButton;
	}
	
	
	protected HorizontalPanel createListenBuyRow( LinkedList<PartnerInventory> partnerInventory, 
													int inventoryBegin, 
													int inventoryEnd )
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		int inventoryNum = partnerInventory.size();
		for ( int x=inventoryBegin; x < inventoryNum && x < inventoryEnd; x++ )
		{
			PartnerInventory pi = partnerInventory.get( x );
			int partnerID = pi.getPartnerID();

			HyperlinkIconBuy buyIcon = new HyperlinkIconBuy(
												viewManager, releaseDetail, partnerID );
						
			HyperlinkIconListen listenIcon = new HyperlinkIconListen( 
												viewManager, releaseDetail, partnerID );
			
			VerticalPanel partnerPanel = new VerticalPanel();
			partnerPanel.setStylePrimaryName( "listenBuyColumn" );
			partnerPanel.add( buyIcon );
			partnerPanel.add( listenIcon );
			
			hp.add( partnerPanel );
		}
		
		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.add( hp );
		hp2.setStyleName( "listenBuyRow" );
		
		return hp2;
	}
	

	
	protected Widget createActionButton()
	{
		final HyperlinkImage w = new HyperlinkImage( "img/layout/iconWishlist.gif", 
										"Add to Wishlist", 
										new WishlistIconListener( viewManager, releaseDetail ) );
		
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				if ( viewManager.getUserManager().isInWishlist( releaseDetail.getTrackID() ) )
					w.addStyleName( "clickedIcon" );
			}
		});
		
		return w;
	}
	
	
	
	protected class MoreIconListener 
		implements ClickHandler
	{
		private LinkedList<PartnerInventory> partnerInventory = null;
		private int numInitialIcons = 0;
				
		public MoreIconListener( LinkedList<PartnerInventory> pi, int numInitial )
		{
			this.partnerInventory = pi;
			this.numInitialIcons = numInitial;
		}
		
		public void onClick( ClickEvent event )
		{			
			final Widget sender = (Widget) event.getSource();
			
			if ( parentPanel.isOpen() )
			{
				DeferredCommand.addCommand( new Command() {
					public void execute() {
						((Image) sender).setUrl( "img/layout/iconMore.gif" );
						parentPanel.setOpen( false );
					}
				});
			}
			else
			{
				if ( parentPanel.getContent() == null )
					parentPanel.setContent( createExtendedRows() );
	
				DeferredCommand.addCommand( new Command() { 
					public void execute() {
						((Image) sender).setUrl( "img/layout/iconLess.gif" );
						parentPanel.setOpen( true );
					}
				});
			}
			
			//((Button) sender).setFocus( false );
		}
		
		private Panel createExtendedRows()
		{
			VerticalPanel extendedRows = new VerticalPanel();
			
			int inventoryNum = partnerInventory.size();
			
			int x = numInitialIcons;
			while ( x < inventoryNum )
			{
				HorizontalPanel extendedRow = createListenBuyRow( 
												partnerInventory, x, x + NUM_ICONS_PER_ROW );
				extendedRows.add( extendedRow );
				x = x + NUM_ICONS_PER_ROW;
			}
			
			return extendedRows;
		}
	}

	
}
