package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimpleCheckBox;

import com.choicetrax.client.Choicetrax;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.logic.ChoicetraxListenManager;


public class AudioQueueClickListener 
	implements ClickHandler 
{
	
	private ChoicetraxListenManager listenManager = null;
	
	
	public AudioQueueClickListener( ChoicetraxListenManager manager ) {
		this.listenManager = manager;
	}
	

	public void onClick( ClickEvent event ) 
	{
		SimpleCheckBox checkBox = (SimpleCheckBox) event.getSource();

		listenManager.setQueued( checkBox.isChecked() );
		
		Choicetrax.setCookie( Constants.COOKIE_QUEUE_TRACKS, 
								checkBox.isChecked() + "",
								Constants.COOKIE_DURATION_1YEAR );
	}

}
