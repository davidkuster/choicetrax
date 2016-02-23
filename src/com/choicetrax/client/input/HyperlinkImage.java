package com.choicetrax.client.input;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;


public class HyperlinkImage extends Image 
{
	
	public HyperlinkImage( String url, ClickHandler cl )
	{
		super();
		initImage( url, null, cl );
	}
	
	
	public HyperlinkImage( String url, String title, ClickHandler cl )
	{
		super();
		initImage( url, title, cl );
		
		/*try {
			this.setUrl( url );
			this.setTitle( title );
			this.addClickListener( cl );
			
			DOM.setElementAttribute( this.getElement(), "src", url );
			
			this.setStylePrimaryName( "imageLink" );
		//}
		//catch ( Exception e ) {
		//	GWT.log( "HyperlinkImage constructor error "
		//			+ "w/URL [" + url + "], title [" + title + "], clicklistener [" + cl + "]",
		//			e );
		//}*/
	}
	
	
	public HyperlinkImage( Element element, ClickHandler cl )
	{
		super( element );
		this.addClickHandler( cl );
		
		this.setStylePrimaryName( "imageLink" );
	}
	
	
	
	private void initImage( String url, String title, ClickHandler cl )
	{
		this.setUrl( url );
		
		if ( title != null )
			this.setTitle( title );
		
		try 
		{
			if ( ( url != null ) && (! "".equals( url ) ) ) 
				//DOM.setElementAttribute( this.getElement(), "src", url );
				DOM.setImgSrc( this.getElement(), url );
		} 
		// catch ( Exception e ) {
		catch ( Throwable e ) {
			GWT.log( "HyperlinkImage constructor error "
						+ "w/URL [" + url + "], clicklistener [" + cl + "]",
					e );
		}
		
		this.setStylePrimaryName( "imageLink" );
		
		// try setting this last to potentially avoid weird errors
		this.addClickHandler( cl );
	}
	
	
	
	public void setSize( String width, String height )
	{
		super.setSize( width, height );
	}

}
