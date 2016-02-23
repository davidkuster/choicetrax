package com.choicetrax.client.logic.callbacks;

import com.google.gwt.user.client.Window;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.User;


public class AccountConfigController implements ChoicetraxLoaderResponseCallback
{
	private String actionType = null;
	private ChoicetraxViewManager viewManager = null;
	
	
	public AccountConfigController( ChoicetraxViewManager manager, String type )
	{
		this.viewManager = manager;
		this.actionType = type;
	}
	
	
	public void onFailure( Throwable t )
	{
		viewManager.setWaitingState( Constants.PANEL_ACCOUNT_CONFIG, false );
		
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}
	
	public void onSuccess( LoaderResponse result )
	{
		viewManager.setWaitingState( Constants.PANEL_ACCOUNT_CONFIG, false );
		
		User user = (User) result;
		viewManager.setCurrentUser( user );
		
		if ( Constants.USER_CREATE.equals( actionType ) )
			Window.alert( "Thanks for creating an account!" );
		else
			Window.alert( "Your account details have been updated." );
		
		viewManager.showCenterPanel( Constants.PANEL_NEWS );
	}
}

