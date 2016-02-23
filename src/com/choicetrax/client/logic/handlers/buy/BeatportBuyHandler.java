package com.choicetrax.client.logic.handlers.buy;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class BeatportBuyHandler 
	extends AbstractBuyHandler 
{

	private static final String loginURL = "http://www.beatport.com";
	
	
	
	public BeatportBuyHandler( ChoicetraxViewManager manager ) 
	{
		super( manager,
				Constants.PARTNER_NAME_BEATPORT,
				Constants.PARTNER_WINDOW_BEATPORT,
				loginURL,
				loginURL );		
	}
	
	
	protected void loadTrack( ReleaseDetail rd ) 
	{
		String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
		
		String buyURL = "http://www.beatport.com/en-US/xml/purchase/orders/addOrderItem";
		// need to send postdata:
		// trackId=430037 (partnerTrackID)
		
		final FormPanel fp = new FormPanel();
		fp.setVisible( false );
		fp.setMethod( FormPanel.METHOD_POST );
		fp.setAction( buyURL );
		VerticalPanel vp = new VerticalPanel();
		fp.setWidget( vp );
		final TextBox tb = new TextBox();
		tb.setName( "trackId" );
		tb.setText( partnerTrackID );
		vp.add( tb );
		
		LoadNextTrackFormHandler handler = new LoadNextTrackFormHandler();
		fp.addSubmitHandler( handler );
		fp.addSubmitCompleteHandler( handler );
		
		RootPanel.get( "buyDiv" ).clear();
		RootPanel.get( "buyDiv" ).add( fp );
					
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				fp.submit();
			}
		});
	}	

}
