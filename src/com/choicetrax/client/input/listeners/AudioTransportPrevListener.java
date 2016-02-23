package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.choicetrax.client.logic.ChoicetraxListenManager;


public class AudioTransportPrevListener
	implements ClickHandler
{
	
	private ChoicetraxListenManager listenManager = null;
	
	
	public AudioTransportPrevListener( ChoicetraxListenManager manager ) {
		this.listenManager = manager;
	}
	
	
	public void onClick( ClickEvent event ) {
		listenManager.playPreviousPlaylistItem();
	}
	
}
