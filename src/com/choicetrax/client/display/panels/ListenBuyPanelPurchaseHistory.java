package com.choicetrax.client.display.panels;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.HyperlinkIconDelete;
import com.choicetrax.client.logic.ChoicetraxViewManager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;


public class ListenBuyPanelPurchaseHistory 
	extends ListenBuyPanel 
{
	
	private int partnerID = -1;
	
	
	public ListenBuyPanelPurchaseHistory( ChoicetraxViewManager manager, ReleaseDetail rd, int partnerID )
	{
		super( manager, rd );
		
		this.partnerID = partnerID;
	}
	
	
	protected Widget createActionButton()
	{
		return new HyperlinkIconDelete( "Remove from Purchase History display", 
										new PurchaseHistoryRemoveListener( releaseDetail ) );
	}
	
	
	
	/**
	 * inner class that removes a release from user's purchase
	 * history display
	 */
	private class PurchaseHistoryRemoveListener
		implements ClickHandler
	{
		private ReleaseDetail rd = null;
		
		public PurchaseHistoryRemoveListener( ReleaseDetail rd )
		{
			this.rd = rd;
		}
		
		public void onClick( ClickEvent event )
		{
			viewManager.getUserManager().removePurchaseHistory( rd, partnerID );
		}
	}

}
