package com.choicetrax.client.input;

import com.google.gwt.user.client.ui.Composite;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.listeners.EPImageLoadListener;
import com.choicetrax.client.input.listeners.EPListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class HyperlinkIconCoverArt extends Composite
{
	
	public HyperlinkIconCoverArt( ChoicetraxViewManager viewManager, ReleaseDetail rd )
	{
		HyperlinkImage coverArt = createImage( viewManager, rd, 25 );
		initWidget( coverArt );
	}
	
	
	public HyperlinkIconCoverArt( ChoicetraxViewManager viewManager, ReleaseDetail rd, int size )
	{
		HyperlinkImage coverArt = createImage( viewManager, rd, size );
		initWidget( coverArt );
	}
	
	
	private HyperlinkImage createImage( ChoicetraxViewManager viewManager, 
										ReleaseDetail rd,
										int size )
	{
		String url = rd.getCoverArtURL();
		if ( ( url == null ) || "".equals( url ) ) url = "img/ep_img.gif";
				
		HyperlinkImage coverArt = new HyperlinkImage( 
										url,
										"View EP", 
										new EPListener( viewManager, rd.getTrackID() ) );
		
		coverArt.setSize( size + "px", size + "px" );
		
		coverArt.addLoadHandler( new EPImageLoadListener() );
		
		return coverArt;		
	}

}
