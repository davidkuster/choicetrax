package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;


public class LoadingComposite 
	extends Composite 
{
	
	
	public LoadingComposite()
	{
		/*VerticalPanel vp = new VerticalPanel();
		initWidget( vp );
		
		vp.add( new Label( "Loading..." ) );
		vp.add( new Image( "img/loading3.gif" ) );*/
		
		HorizontalPanel hp = new HorizontalPanel();
		initWidget( hp );
		
		hp.setSpacing( 5 );
		hp.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
				
		hp.add( new Image( "img/loading4.gif" ) );
		hp.add( new Label( "Loading... " ) );
	}

}
