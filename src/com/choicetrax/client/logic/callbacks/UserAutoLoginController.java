package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.data.User;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;


public class UserAutoLoginController implements ChoicetraxLoaderResponseCallback
{
	ChoicetraxViewManager viewManager = null;
	
	
	public UserAutoLoginController( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
	}
	
	public void onFailure( Throwable t )
	{
		viewManager.setCurrentUser( null );
	}
	
	public void onSuccess( LoaderResponse result )
	{
		viewManager.clearViewComponent( Constants.PANEL_ACCOUNT_DETAILS );
		
		User user = (User) result;
		viewManager.setCurrentUser( user );
		
		viewManager.updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, user );
	}
}
