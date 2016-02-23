package com.choicetrax.client.display.panels;

import java.util.LinkedList;

import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.input.HyperlinkIconWishlist;


public class ListenBuyPanelEP 
	extends ListenBuyPanel 
{
	
	
	public ListenBuyPanelEP( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		super( manager, rd );
	}
	

	protected Panel createListenBuyPanel() 
	{
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing( 2 );
		
		LinkedList<PartnerInventory> partnerInventory = this.releaseDetail.getPartnerAvailability();
		if ( partnerInventory != null )
		{
			HyperlinkIconWishlist wishlistIcon = new HyperlinkIconWishlist( viewManager, 
																			releaseDetail );
			//HyperlinkIconOptions optionsIcon = new HyperlinkIconOptions( viewManager,
			//																releaseDetail );
			
			VerticalPanel wishlistPanel = new VerticalPanel();
			wishlistPanel.setStyleName( "listenBuyColumn" );
			wishlistPanel.setSpacing( 2 );
			
			wishlistPanel.add( wishlistIcon );
			//wishlistPanel.add( optionsIcon );
			
			int inventoryNum = partnerInventory.size();
			if ( inventoryNum > 1 )
			{
				Widget moreButton = createMoreButton( partnerInventory, inventoryNum - 1, 1 );
				wishlistPanel.add( moreButton );
				//wishlistPanel.setSpacing( 1 );
			}
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setWidth( "100%" );
			
			HorizontalPanel listenBuyRow = createListenBuyRow( partnerInventory, 0, 1 );
			
			ReleaseTrackNamePanel trackNamePanel = new ReleaseTrackNamePanel( this.releaseDetail, 
																				this.viewManager );
			trackNamePanel.addStyleName( "listenBuyRowEP" );
				
			hp.add( trackNamePanel );
			hp.add( listenBuyRow );
			hp.add( wishlistPanel );
			vp.add( hp );
		}
		
		return vp;
	}

}
