package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.Label;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.display.panels.ListenBuyPanelWishlist;
import com.choicetrax.client.display.panels.ListenBuyPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.constants.Constants;


public class WishlistReleasesComposite 
	extends AbstractReleasesComposite 
{
	
	
	public WishlistReleasesComposite( ChoicetraxViewManager manager )
	{
		super( manager, Constants.PANEL_WISHLIST );
	}
	
	
	protected Label createActionHeader()
	{
		return new Label( "Rem" );
	}
	
	
	protected ListenBuyPanel createListenBuyPanel( ReleaseDetail rd )
	{		
		return new ListenBuyPanelWishlist( viewManager, rd );
	}

}
