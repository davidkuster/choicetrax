package com.choicetrax.client.input;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.listeners.WishlistIconListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class HyperlinkIconWishlist 
	extends Composite
{
	
	public HyperlinkIconWishlist( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		final ChoicetraxViewManager viewManager = manager;
		
		final HyperlinkImage wishlistButton = new HyperlinkImage( 
													"img/layout/iconWishlist.gif",
													"Add to wishlist",
													new WishlistIconListener( viewManager, rd ) );
		
		initWidget( wishlistButton );
		
		final int trackID = rd.getTrackID();

		DeferredCommand.addCommand( new Command() {
			public void execute() {
				if ( viewManager.getUserManager().isInWishlist( trackID ) )
					wishlistButton.addStyleName( "clickedIcon" );
			}
		});
	}

}
