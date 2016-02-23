package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.choicetrax.client.actions.loaderactions.AlbumEPSearchAction;
import com.choicetrax.client.logic.callbacks.AlbumEPLoadController;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class EPListener 
	implements ClickHandler
{
	private ChoicetraxViewManager viewManager = null;
	private int trackID = -1;
	
	
	public EPListener( ChoicetraxViewManager manager, int trackID ) 
	{
		this.viewManager = manager;
		this.trackID = trackID;
	}
	
	
	public void onClick( ClickEvent event )
	{
		AlbumEPSearchAction action = new AlbumEPSearchAction();
		action.setTrackID( trackID );
		
		if ( viewManager.getCurrentUser() != null )
			action.setUserID( viewManager.getCurrentUser().getUserID() );
		
		//viewManager.executeAction( action, new AlbumEPLoadController( viewManager ) );
		viewManager.deferAction( action, new AlbumEPLoadController( viewManager ) );
	}
	
}