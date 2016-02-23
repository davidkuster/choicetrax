package com.choicetrax.client.input;

import com.choicetrax.client.logic.ChoicetraxViewManager;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;


public class UserSearchHyperlink extends Label 
{
	private ChoicetraxViewManager viewManager = null;
	private String searchName = null;
	
	
	public UserSearchHyperlink( String searchName, ChoicetraxViewManager manager )
	{
		super( searchName );

		this.setWordWrap( true );
		
		this.searchName = searchName;
		this.viewManager = manager;
		
		this.addClickHandler( new UserSearchHyperlinkListener() );
		this.setStyleName( "labelLink" );
	}
	
	
	
	private class UserSearchHyperlinkListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			viewManager.executeUserSearch( searchName );
		}
	}

}
