package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.VirtualCarts;


public class PurchaseHistoryLoadController 
	implements ChoicetraxLoaderResponseCallback 
{
	private ChoicetraxViewManager viewManager = null;
	
	public PurchaseHistoryLoadController( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
	}
	
	public void onFailure( Throwable t ) 
	{
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}

	public void onSuccess( LoaderResponse result )
	{
		viewManager.setWaitingState( Constants.PANEL_RELEASES, false );
		
		VirtualCarts carts = (VirtualCarts) result;
		viewManager.getCurrentUser().setPurchaseHistory( carts );
		
		viewManager.updateHistoryDisplay( Constants.HISTORY_PURCHASE_HISTORY );
	}

}
