package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.display.panels.OptionsPopupPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class OptionsIconListener 
	implements ClickHandler
{
	
	private ChoicetraxViewManager viewManager = null;
	private ReleaseDetail rd = null;
	
				
	public OptionsIconListener( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		this.viewManager = manager;
		this.rd = rd;
	}
	
	
	public void onClick( ClickEvent event )
	{
		new OptionsPopupPanel( viewManager, rd, (Widget) event.getSource() );
	}
	
}