package com.choicetrax.client.logic.handlers.buy;

import com.google.gwt.user.client.*;
import com.google.gwt.http.client.URL;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class JunoBuyHandler 
	extends AbstractBuyHandler 
{

	private static final String cartURL = "https://www.junodownload.com/cart/?ref=ctrx";
	private static final String loginURL = "https://www.junodownload.com/login/?ref=ctrx";
		
	
	public JunoBuyHandler( ChoicetraxViewManager manager ) 
	{
		super( manager, 
				Constants.PARTNER_NAME_JUNODOWNLOAD,
				Constants.PARTNER_WINDOW_JUNODOWNLOAD,
				loginURL,
				cartURL );
	}
	
	
	/**
	 * note that for Junodownload, partnerTrackIDs should be stored
	 * in the database in the format "1089073-2-1-4".
	 * 
	 * @param partnerTrackID
	 * @return
	 */
	private String createBuyURL( String partnerTrackID, String formatCode )
	{
		String url = "https://www.junodownload.com/cart/add/"
					+ "?popup=no&track[]=" 
					+ partnerTrackID
					+ "-" 
					+ formatCode
					+ "&ref=ctrx";
		
		return url;
	}
	
	
	protected void loadTrack( ReleaseDetail rd ) 
	{
		String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
		Format format = cart.getSelectedFormat( rd.getTrackID() );
		
		String buyURL = createBuyURL( partnerTrackID, format.getFormatCode() );
		
		Window.open( URL.encode( buyURL ), partnerWindow, "" );
		
		Timer t = new Timer() {
			public void run() {
				loadNextTrack();
			}
		};
		t.schedule( 15000 );
		// wait 15 seconds until trying to load next track
	}
	
}
