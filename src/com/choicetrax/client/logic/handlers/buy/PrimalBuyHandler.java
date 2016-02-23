package com.choicetrax.client.logic.handlers.buy;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class PrimalBuyHandler 
	extends AbstractBuyHandler 
{

	private static final String cartURL = "http://www.primalrecords.com/store/cart.php";
	private static final String loginURL = "http://www.primalrecords.com/store/home.php";
	
	
	public PrimalBuyHandler( ChoicetraxViewManager manager ) 
	{
		super( manager,
				Constants.PARTNER_NAME_PRIMAL,
				Constants.PARTNER_WINDOW_PRIMAL,
				loginURL,
				cartURL );
	}
	
	
	
	protected void loadTrack( ReleaseDetail rd ) 
	{
		String partnerTrackID = rd.getPartnerInventory( partnerID ).getPartnerTrackID();
		
		String buyURL = "http://www.primalrecords.com"
						+ "/store/add_to_cart.php"
						+ "?add_productid="
						+ partnerTrackID;
		
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
