package com.choicetrax.client.input;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

public class HyperlinkLabel extends Label 
{
	
	public HyperlinkLabel( String text, ClickHandler cl )
	{
		super( text );
		this.addClickHandler( cl );
		this.setStyleName( "labelLink" );
	}
	
	
	public HyperlinkLabel( String text, String title, ClickHandler cl )
	{
		super( text );
		this.setTitle( title );
		this.addClickHandler( cl );
		this.setStyleName( "labelLink" );
	}
	
	public void setWrap( boolean wrap )
	{
		this.setWordWrap( wrap );
	}

}
