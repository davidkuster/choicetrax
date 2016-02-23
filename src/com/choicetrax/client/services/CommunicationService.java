package com.choicetrax.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;


@RemoteServiceRelativePath("comm")
public interface CommunicationService extends RemoteService 
{

	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException;

	public ActionResponse handleAction( HandlerAction action )
		throws ChoicetraxException;
}
