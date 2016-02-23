package com.choicetrax.client.input;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;


public class HyperlinkIconDelete 
	extends Composite
{
	
	
	public HyperlinkIconDelete( String title, ClickHandler listener )
	{
		final HyperlinkImage optionsIcon = new HyperlinkImage( 
											"img/layout/iconDelete.gif",
											title,
											listener );
		
		initWidget( optionsIcon );
	}

}
