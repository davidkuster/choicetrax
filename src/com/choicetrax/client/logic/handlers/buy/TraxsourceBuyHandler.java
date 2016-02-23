package com.choicetrax.client.logic.handlers.buy;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class TraxsourceBuyHandler 
	extends AbstractBuyHandler 
{
	
	private static final String cartURL = "http://www.traxsource.com/index.php?act=cart";
	private static final String loginURL = "http://www.traxsource.com";
		
	
	public TraxsourceBuyHandler( ChoicetraxViewManager manager ) 
	{
		super( manager,
				Constants.PARTNER_NAME_TRAXSOURCE,
				Constants.PARTNER_WINDOW_TRAXSOURCE,
				loginURL,
				cartURL );
	}
	
	
	/**
	 * note that for Traxsource, partnerTrackIDs should be stored
	 * in the database in the format "ep=123,id=345".
	 * 
	 * for Traxsource it's necessary to send both title_id (ep)
	 * and track_id (id) to their buy URL.
	 * 
	 * @param partnerTrackID
	 * @return
	 */
	private String createBuyURL( String partnerTrackID, String formatCode )
	{
		int index = partnerTrackID.indexOf( "," );
		String title_id = partnerTrackID.substring( 3, index );
		String track_id = partnerTrackID.substring( index + 4 );
		
		StringBuffer sb = new StringBuffer();
		sb.append( "http://www.traxsource.com/"
			+ "scripts/minicart.php?act=sc_add" );
		
		sb.append( "&title_id=" );
		sb.append( title_id );
		
		sb.append( "&track_id=" );
		sb.append( track_id );
		
		sb.append( "&bitrate=" );
		sb.append( formatCode );
		
		return sb.toString();
	}
	
	
	protected void loadTrack( ReleaseDetail rd ) 
	{
		String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
		Format format = cart.getSelectedFormat( rd.getTrackID() );
		
		String buyURL = createBuyURL( partnerTrackID, format.getFormatCode() );
		
		final FormPanel fp = new FormPanel();
		fp.setVisible( false );
		fp.setMethod( FormPanel.METHOD_POST );
		fp.setAction( buyURL );
		VerticalPanel vp = new VerticalPanel();
		fp.setWidget( vp );
		
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
