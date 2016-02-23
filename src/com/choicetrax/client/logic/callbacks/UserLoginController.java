package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.data.User;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;


public class UserLoginController 
	implements ChoicetraxLoaderResponseCallback 
{
	private ChoicetraxViewManager viewManager = null;
	private int accountPanel = Constants.PANEL_ACCOUNT_DETAILS;
	
	
	public UserLoginController( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		viewManager.setWaitingState( accountPanel, true );
	}
	
	public void onFailure( Throwable t )
	{
		viewManager.setWaitingState( accountPanel, false );
		
		viewManager.handleError( t, "UserLoginController.onFailure()" );
	}
	
	public void onSuccess( LoaderResponse result )
	{
		viewManager.clearViewComponent( accountPanel );
		viewManager.setWaitingState( accountPanel, false );
		
		User user = (User) result;
		
		viewManager.updateDisplay( accountPanel, user );
		
		viewManager.setCurrentUser( user );
		viewManager.clearViewComponent( accountPanel );
	}

}
