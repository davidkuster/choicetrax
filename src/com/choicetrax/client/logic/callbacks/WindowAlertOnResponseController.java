package com.choicetrax.client.logic.callbacks;

import com.google.gwt.user.client.Window;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.ActionHandlerMessage;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;


public class WindowAlertOnResponseController 
	implements ChoicetraxActionResponseCallback 
{
	private ChoicetraxViewManager viewManager = null;
	
	public WindowAlertOnResponseController( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
	}
	
	public void onFailure( Throwable t ) 
	{
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}

	public void onSuccess( ActionResponse result )
	{
		ActionHandlerMessage msg = (ActionHandlerMessage) result;
		
		if ( msg.getResponseCode() != Constants.ACTION_RESPONSE_NORMAL )
			viewManager.handleError( new Exception( msg.getResponseText() ),
									 this.getClass().getName() + ".onSuccess()" ); 
		else
			// window alert response text, even on success
			Window.alert( msg.getResponseText() );
	}

}
