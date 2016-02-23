package com.choicetrax.client.input;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;


public class ClickableImageHTML extends ClickableImage
{
	
	
	public ClickableImageHTML( Element element, ClickHandler cl ) 
	{
		super( element );
		this.addClickHandler( cl );	
		sinkEvents( Event.ONCLICK );
		onAttach();
	}
	
	
	public void onBrowserEvent( Event event ) {
		int type = DOM.eventGetType( event );
		if ( type == Event.ONCLICK )
			this.click();
	}
	
	
	public void setUrl( String url ) {
		DOM.setElementProperty( this.getElement(), "src", url );
	}

}
