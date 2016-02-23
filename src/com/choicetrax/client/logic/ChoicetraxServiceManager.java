package com.choicetrax.client.logic;

import com.choicetrax.client.actions.Action;
import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.logic.callbacks.ChoicetraxActionResponseCallback;
import com.choicetrax.client.logic.callbacks.ChoicetraxCallback;
import com.choicetrax.client.logic.callbacks.ChoicetraxLoaderResponseCallback;
import com.choicetrax.client.services.CommunicationServiceRPC;
import com.choicetrax.client.util.exception.ChoicetraxException;


public class ChoicetraxServiceManager 
{
	
	public ChoicetraxServiceManager() {}
	
	
	public void callService( Action action, ChoicetraxCallback callback )
		throws ChoicetraxException
	{
		CommunicationServiceRPC service = CommunicationServiceRPC.getInstance();
		
		if ( action instanceof LoaderAction )
		{
			service.loadData( (LoaderAction) action, 
							  (ChoicetraxLoaderResponseCallback) callback );
		}
		else if ( action instanceof HandlerAction )
		{
			service.handleAction( (HandlerAction) action, 
								  (ChoicetraxActionResponseCallback) callback );
		}
		else 
			throw new ChoicetraxException( "Unknown Action type: " + action );
	}
	
}
