package com.choicetrax.client.logic.callbacks;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.User;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class PasswordResetController 
	implements ChoicetraxLoaderResponseCallback
{
	
	private ChoicetraxViewManager viewManager = null;
	private int accountPanel = Constants.PANEL_ACCOUNT_DETAILS;
	
	
	public PasswordResetController( ChoicetraxViewManager manager ) 
	{
		super();
		this.viewManager = manager;
		
		viewManager.setWaitingState( accountPanel, true );
	}

		
		
	public void onFailure( Throwable t )
	{
		viewManager.setWaitingState( accountPanel, false );
		
		viewManager.handleError( t, "PasswordResetController.onFailure()" );
	}
	
	public void onSuccess( LoaderResponse result )
	{
		viewManager.clearViewComponent( accountPanel );
		viewManager.setWaitingState( accountPanel, false );
		
		User user = (User) result;
		
		viewManager.updateDisplay( accountPanel, user );
		
		viewManager.setCurrentUser( user );
		viewManager.clearViewComponent( accountPanel );
		
		viewManager.showCenterPanel( Constants.PANEL_ACCOUNT_CONFIG );
		
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				Window.alert( "You have been logged in.  Please enter a new password." );
			}
		});
	}

}
