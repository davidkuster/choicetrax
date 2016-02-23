package com.choicetrax.client.display.panels;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.HyperlinkIconDelete;
import com.choicetrax.client.logic.ChoicetraxViewManager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class ListenBuyPanelWishlist 
	extends ListenBuyPanel 
{
	
	
	
	/**
	 * inner class that removes a release from the user's wishlist
	 */
	private class WishlistRemoveListener
		implements ClickHandler
	{
		private ReleaseDetail rd = null;
		
		public WishlistRemoveListener( ReleaseDetail rd )
		{
			this.rd = rd;
		}
		
		public void onClick( ClickEvent event )
		{
			viewManager.getUserManager().removeWishlist( rd );
		}
	}
	
	
	
	
	public ListenBuyPanelWishlist( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		super( manager, rd );
	}
	
	
	protected Widget createActionButton()
	{
		HyperlinkIconDelete remove = new HyperlinkIconDelete( "Remove from Wishlist", 
											new WishlistRemoveListener( releaseDetail ) );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.setSpacing( 8 );
		
		vp.add( remove );
		
		return vp;		
	}

}
