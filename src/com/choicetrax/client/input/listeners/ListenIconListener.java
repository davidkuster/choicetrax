package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FocusWidget;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.data.ReleaseDetail;


/**
 * class that handles functionality when user clicks
 * on a listen icon
 * 
 * @author David Kuster
 */
public class ListenIconListener 
	implements ClickHandler
{
	
	private ChoicetraxViewManager viewManager = null;
	private ReleaseDetail rd = null;
	private int partnerID = -1;
	
	
	public ListenIconListener( ChoicetraxViewManager manager,
								ReleaseDetail rd,
								int partnerID )
	{
		this.viewManager = manager;
		this.rd = rd;
		this.partnerID = partnerID;
	}

	public void onClick( ClickEvent event )
	{
		// play audio file from partner's web site
		// using our embedded player
		
		// may need to pass reference to ReleaseDetail
		// object which can then be passed on to audio
		// player so it can display artist name, track title, etc
		
		Widget sender = (Widget) event.getSource();
		
		sender.addStyleName( "clickedIcon" );
		
		if ( sender instanceof FocusWidget )
			((FocusWidget) sender).setFocus( false );
		
		viewManager.getUserManager().listenTrack( rd, partnerID );
	}
	
}