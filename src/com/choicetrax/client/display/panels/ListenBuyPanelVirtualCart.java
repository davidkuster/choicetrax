package com.choicetrax.client.display.panels;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.HyperlinkIconDelete;
import com.choicetrax.client.input.HyperlinkIconWishlist;
import com.choicetrax.client.logic.ChoicetraxViewManager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ListenBuyPanelVirtualCart 
	extends ListenBuyPanel 
{
	
	private int parterID;
	
	
	public ListenBuyPanelVirtualCart( ChoicetraxViewManager manager, ReleaseDetail rd, int partnerID )
	{
		super( manager, rd );
		
		this.parterID = partnerID;
	}
	
	
	protected Widget createActionButton()
	{
		HyperlinkIconWishlist wishlist = new HyperlinkIconWishlist( 
											viewManager, 
											releaseDetail );
		
		HyperlinkIconDelete remove = new HyperlinkIconDelete( 
											"Remove from Virtual Cart", 
											new VirtualCartRemoveListener( releaseDetail ) );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.setSpacing( 8 );
		
		vp.add( wishlist );
		vp.add( remove );
				
		return vp;
	}
	
	
	
	/**
	 * inner class that removes a release from user's purchase
	 * history display
	 */
	private class VirtualCartRemoveListener
		implements ClickHandler
	{
		private ReleaseDetail rd = null;
		
		public VirtualCartRemoveListener( ReleaseDetail rd )
		{
			this.rd = rd;
		}
		
		public void onClick( ClickEvent event )
		{
			viewManager.getUserManager().removeFromVirtualCart( 
												rd, 
												ListenBuyPanelVirtualCart.this.parterID );
		}
	}

}
