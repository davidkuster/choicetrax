package com.choicetrax.client.logic.handlers.buy;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class DJDownloadBuyHandler 
	extends AbstractBuyHandler 
{

	private static final String cartURL = "http://www.djdownload.com/cart.php";
	private static final String loginURL = "http://www.djdownload.com/index.php";
	
	
	public DJDownloadBuyHandler( ChoicetraxViewManager manager ) 
	{
		super( manager,
				Constants.PARTNER_NAME_DJDOWNLOAD,
				Constants.PARTNER_WINDOW_DJDOWNLOAD,
				loginURL,
				cartURL );
	}
	
	
	
	protected void loadTrack( ReleaseDetail rd ) 
	{
		String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
		
		String buyURL = "http://www.djdownload.com/cartAjax.php"
						+ "?a=add" 
						+ "&id=" + partnerTrackID;
		
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
