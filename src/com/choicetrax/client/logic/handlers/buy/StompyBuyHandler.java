package com.choicetrax.client.logic.handlers.buy;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class StompyBuyHandler 
	extends AbstractBuyHandler 
{

	private static final String cartURL = "https://www.stompy.com/buy"
										+ "#s=buy&v=&sv=&c=all&st=all&pg="
										+ "&pro1=&pro2=&o=&pn=";
	
	private static final String loginURL = "https://www.stompy.com/#login";
	
	
	public StompyBuyHandler( ChoicetraxViewManager manager ) 
	{
		super( manager,
				Constants.PARTNER_NAME_STOMPY,
				Constants.PARTNER_WINDOW_STOMPY,
				loginURL,
				cartURL );
	}
	
	
	
	protected void loadTrack( ReleaseDetail rd ) 
	{
		String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
		
		// http://www.stompy.com/account/crate.php?
		// add_to_crate.x=3&add%5B%5D=71961&product_id=71961
			
		/*String buyURL = "http://www.stompy.com"
						+ "/account/crate.php"
						+ "?add_to_crate.x=3&add%5B%5D="
						+ partnerTrackID
						+ "&product_id="
						+ partnerTrackID;*/
		
		String buyURL = "https://www.stompy.com/server.php"
						+ "?action=cart&op=add&pid="
						+ partnerTrackID
						+ "&type=buy";
		
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
