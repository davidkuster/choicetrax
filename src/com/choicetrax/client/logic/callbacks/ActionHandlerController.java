package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.ActionHandlerMessage;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;


public class ActionHandlerController 
	implements ChoicetraxActionResponseCallback 
{
	private ChoicetraxViewManager viewManager = null;
	
	public ActionHandlerController( ChoicetraxViewManager manager )
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
	}

}
