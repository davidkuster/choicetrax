package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.config.ChoicetraxInitialData;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class InitialDataLoadController 
	implements ChoicetraxLoaderResponseCallback 
{
	
	private ChoicetraxViewManager viewManager = null;
	
	
	public InitialDataLoadController( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
	}
	
	public void onFailure( Throwable t ) 
	{
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}

	public void onSuccess( LoaderResponse result )
	{
		viewManager.initialize( (ChoicetraxInitialData) result );
	}

}
