package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.display.panels.ListenBuyPanelPurchaseHistory;
import com.choicetrax.client.display.panels.ListenBuyPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.constants.Constants;


public class PurchaseHistoryReleasesComposite 
	extends AbstractReleasesComposite 
{
	
	private int partnerID = -1;
	
	
	public PurchaseHistoryReleasesComposite( ChoicetraxViewManager manager, int partnerID )
	{
		super( manager, Constants.PANEL_PURCHASE_HISTORY );
		
		this.partnerID = partnerID;
	}
	
	
	
	protected Label createActionHeader()
	{
		return new Label( "Remove" );
	}
	
	
	protected ListenBuyPanel createListenBuyPanel( ReleaseDetail rd )
	{		
		return new ListenBuyPanelPurchaseHistory( viewManager, rd, partnerID );
	}
	
	
	protected Widget createHeaderWidget( int cacheType )
	{
		return null;
	}

}
