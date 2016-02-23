package com.choicetrax.client.input;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;


public class ClickableImage extends Image
{
	
	private ClickHandler clickListener = null;
	private boolean enabled = true;
	
	
	public ClickableImage( String url ) {
		super( url );
		this.setStylePrimaryName( "clickableImage" );
	}
	
	public ClickableImage( Element element ) {
		super( element );
		this.setStylePrimaryName( "clickableImage" );
	}
	
	
	public HandlerRegistration addClickHandler( ClickHandler cl ) 
	{
		HandlerRegistration hr = super.addClickHandler( cl );
		this.clickListener = cl;
		
		return hr;
	}
	
	
	public void click() {
		// TODO: passing null to onClick might be a problem...
		clickListener.onClick( null );
	}
	
	
	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
		
		if ( enabled ) 
			this.removeStyleName( "clickedIcon" );
		else
			this.addStyleName( "clickedIcon" );
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	

	//public void setFocus( boolean focus ) {
	//	this.setFocus( focus );
	//}

}
