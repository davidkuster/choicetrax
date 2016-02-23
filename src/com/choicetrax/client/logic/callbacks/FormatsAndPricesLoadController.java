package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.actions.responses.FormatsAndPrices;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.display.panels.BuyPopupPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.ChoicetraxLoaderResponseCallback;


public class FormatsAndPricesLoadController 
	implements ChoicetraxLoaderResponseCallback 
{

	private ChoicetraxViewManager viewManager = null;
	private BuyPopupPanel buyPopup = null;
	
	public FormatsAndPricesLoadController( 
							ChoicetraxViewManager manager,
							BuyPopupPanel popup )
	{
		this.viewManager = manager;
		this.buyPopup = popup;
	}
	
	public void onFailure( Throwable t ) 
	{
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}

	public void onSuccess( LoaderResponse result ) 
	{
		FormatsAndPrices formats = (FormatsAndPrices) result;
		buyPopup.populateFormats( formats.getFormats() );
	}

}
