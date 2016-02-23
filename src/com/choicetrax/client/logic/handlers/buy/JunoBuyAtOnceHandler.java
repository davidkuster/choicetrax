package com.choicetrax.client.logic.handlers.buy;

import com.google.gwt.user.client.*;
import com.google.gwt.http.client.URL;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;


/**
 * Figured out that I can transfer all tracks to the Juno cart in one shot, by adding
 * multiple track[] parameters to the URL.  Example:
 * 
 * http://www.junodownload.com/cart/add/?popup=no
 * 	&track[]=1304666-02-1-4-mp3_320
 * 	&track[]=1546431-02-1-4-mp3_320
 * 	&track[]=1273885-02-1-4-mp3_320
 * 
 * This will add all 3 tracks to the Juno cart at once.
 * 
 * @author David
 *
 */
public class JunoBuyAtOnceHandler 
	extends AbstractBuyHandler 
{

	private static final String cartURL = "https://www.junodownload.com/cart/?ref=ctrx";
	private static final String loginURL = "https://www.junodownload.com/login/?ref=ctrx";
		
	
	public JunoBuyAtOnceHandler( ChoicetraxViewManager manager ) 
	{
		super( manager, 
				Constants.PARTNER_NAME_JUNODOWNLOAD,
				Constants.PARTNER_WINDOW_JUNODOWNLOAD,
				loginURL,
				cartURL );
	}
	
	
	
	/**
	 * Overriding this from the parent AbstractBuyHandler to do the track transfer
	 * in one shot instead of incrementally.
	 */
	public void loadNextTrack()
	{
		loadAllTracksAtOnce();
	}
	
	
	private void loadAllTracksAtOnce()
	{
		String allTracksURL = createAllTracksAtOnceURL();
		
		Window.open( URL.encode( allTracksURL ), partnerWindow, "" );
		
		Timer t = new Timer() {
			public void run() 
			{
				promptUserIfTransferCompletedCorrectly();
			}
		};
		t.schedule( 15000 );
	}
	
	
	
	private String createAllTracksAtOnceURL()
	{
		StringBuffer sb = new StringBuffer();
		sb.append( "https://www.junodownload.com/cart/add/?popup=no" );
		
		while ( cartIterator.hasNext() )
		{
			ReleaseDetail rd = cartIterator.next();
			
			String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
			Format format = cart.getSelectedFormat( rd.getTrackID() );
			
			sb.append( "&track[]=" 
						+ partnerTrackID 
						+ "-" 
						+ format.getFormatCode() );
		}
		
		sb.append( "&ref=ctrx" );
		
		return sb.toString();
	}
	
	
	/**
	 * note that for Junodownload, partnerTrackIDs should be stored
	 * in the database in the format "1089073-2-1-4".
	 * 
	 * @param partnerTrackID
	 * @return
	 */
	/*private String createBuyURL( String partnerTrackID, String formatCode )
	{
		String url = "https://www.junodownload.com/cart/add/"
					+ "?popup=no&track[]=" 
					+ partnerTrackID
					+ "-" 
					+ formatCode
					+ "&ref=ctrx";
		
		return url;
	}*/
	

	
	protected void loadTrack( ReleaseDetail rd ) 
	{
		/*String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
		Format format = cart.getSelectedFormat( rd.getTrackID() );
		
		String buyURL = createBuyURL( partnerTrackID, format.getFormatCode() );
		
		Window.open( URL.encode( buyURL ), partnerWindow, "" );
		
		Timer t = new Timer() {
			public void run() {
				loadNextTrack();
			}
		};
		t.schedule( 15000 );
		// wait 15 seconds until trying to load next track*/
		
		Window.alert( "loadTrack( rd ) called for JunoBuyAtOnceHandler - should not be called." );
	}
	
}
