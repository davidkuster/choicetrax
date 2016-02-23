package com.choicetrax.client.input.listeners;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;


public class EPImageLoadListener implements LoadHandler, ErrorHandler
{
	
	public void onLoad( LoadEvent event ) {}
	
	public void onError( ErrorEvent event ) 
	{
		try {
			((Image) event.getSource()).setUrl( "img/ep_img.gif" );
		} catch ( Throwable e ) {
			GWT.log( "Error setting Image URL to missing EP image", e );
		}
	} 

}
