package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class WishlistIconListener 
	implements ClickHandler
{
	private ChoicetraxViewManager viewManager = null;
	private ReleaseDetail rd = null;
	
	
	public WishlistIconListener( ChoicetraxViewManager manager, 
							 ReleaseDetail rd )
	{
		this.viewManager = manager;
		this.rd = rd;
	}
	
	public void onClick( ClickEvent event )
	{
		boolean added = viewManager.getUserManager().addWishlist( rd );
		
		Widget sender = (Widget) event.getSource();
		
		if ( added )
			sender.addStyleName( "clickedIcon" );
		
		if ( sender instanceof FocusWidget )
			((FocusWidget) sender).setFocus( false );
	}
	
}