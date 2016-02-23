package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.logic.ChoicetraxViewManager;


public class PurchaseHistoryCartsComposite 
	extends AbstractMultiCartsComposite
{

	public PurchaseHistoryCartsComposite( ChoicetraxViewManager manager ) {
		super( manager );
	}

	protected Label createHeaderLabel() {
		return new Label( "Purchase History" );
	}

	protected void emptyCartsAlert() {
		Window.alert( "You have no purchase history.\n\n"
					+ "(After you transfer trax to a download store from your Choicetrax\n"
					+ "Virtual Cart they will be moved to Purchase History so you can\n"
					+ "keep track of all your past purchases in one place.)\n\n"
					+ "Happy hunting!" );
	}

	protected AbstractReleasesComposite createMultiCartReleasesComposite(
												ChoicetraxViewManager viewManager, 
												int partnerID ) 
	{
		return new PurchaseHistoryReleasesComposite( viewManager, partnerID );
	}

}
