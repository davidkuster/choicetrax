package com.choicetrax.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.actions.responses.LoaderResponse;


public interface CommunicationServiceAsync 
{

	public void loadData( LoaderAction action, 
							AsyncCallback<LoaderResponse> callback );
	
	public void handleAction( HandlerAction action, 
							AsyncCallback<ActionResponse> callback );
	
}
