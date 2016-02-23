package com.choicetrax.client.input;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

public class HyperlinkButton extends Button 
{
	
	public HyperlinkButton( String text, ClickHandler cl )
	{
		super( text );
		this.addClickHandler( cl );
		
		this.addStyleName( "hyperlinkButton" );
	}
	
	
	public HyperlinkButton( String text, String title, ClickHandler cl )
	{
		super( text );
		this.setTitle( title );
		this.addClickHandler( cl );
		
		this.addStyleName( "hyperlinkButton" );
	}

}
