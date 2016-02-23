package com.choicetrax.client.constants;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;


public interface ImageBundleCorners extends ClientBundle
{
	
	// box corners
	
	@Source( "com/choicetrax/client/img/layout/corners/boxBottomLeft.gif" )
	public ImageResource boxBottomLeft();
	
	@Source( "com/choicetrax/client/img/layout/corners/boxBottomRight.gif" )
	public ImageResource boxBottomRight();
	
	@Source( "com/choicetrax/client/img/layout/corners/boxTopLeft.gif" )
	public ImageResource boxTopLeft();
	
	@Source( "com/choicetrax/client/img/layout/corners/boxTopRight.gif" )
	public ImageResource boxTopRight();
	
	
	// popup corners
	
	@Source( "com/choicetrax/client/img/layout/corners/popupBottomLeft.gif" )
	public ImageResource popupBottomLeft();
	
	@Source( "com/choicetrax/client/img/layout/corners/popupBottomRight.gif" )
	public ImageResource popupBottomRight();
	
	@Source( "com/choicetrax/client/img/layout/corners/popupTopLeft.gif" )
	public ImageResource popupTopLeft();
	
	@Source( "com/choicetrax/client/img/layout/corners/popupTopRight.gif" )
	public ImageResource popupTopRight();

}
