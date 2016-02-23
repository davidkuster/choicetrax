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


public class ListenBuyPanelRecommended 
	extends ListenBuyPanel 
{
	
	
	
	/**
	 * inner class that removes a release from the user's recommended releases
	 */
	private class RecommendedRemoveListener
		implements ClickHandler
	{
		private ReleaseDetail rd = null;
		
		public RecommendedRemoveListener( ReleaseDetail rd )
		{
			this.rd = rd;
		}
		
		public void onClick( ClickEvent event )
		{
			viewManager.getUserManager().removeRecommendation( rd );
		}
	}
	
	
	
	
	public ListenBuyPanelRecommended( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		super( manager, rd );
	}
	
	
	protected Widget createActionButton()
	{
		HyperlinkIconWishlist wishlist = new HyperlinkIconWishlist( 
											viewManager, 
											releaseDetail );
		
		HyperlinkIconDelete remove = new HyperlinkIconDelete( "Remove from Recommendations", 
											new RecommendedRemoveListener( releaseDetail ) );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.setSpacing( 8 );
		
		vp.add( wishlist );
		vp.add( remove );
		
		return vp;		
	}

}
