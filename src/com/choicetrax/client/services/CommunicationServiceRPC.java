package com.choicetrax.client.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.rpc.ServiceDefTarget;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.logic.callbacks.ChoicetraxActionResponseCallback;
import com.choicetrax.client.logic.callbacks.ChoicetraxLoaderResponseCallback;


/**
 * this class serves as a facade to hide the complexity
 * of making remote procedure calls (RPCs) against the
 * ChoicetraxConfigService GWT RPC service.
 * 
 * @author David Kuster
 */
public class CommunicationServiceRPC 
{
	
	private static CommunicationServiceRPC service = new CommunicationServiceRPC();
	private CommunicationServiceAsync proxy = null;
		
	
	/**
	 * singleton constructor
	 */
	private CommunicationServiceRPC()
	{
		proxy = (CommunicationServiceAsync)
					GWT.create( CommunicationService.class );
		
		//String serviceUrl = GWT.getModuleBaseURL() + "comm";
		
		//((ServiceDefTarget) proxy).setServiceEntryPoint( serviceUrl );
	}
	
	
	public static CommunicationServiceRPC getInstance()
	{
		//if ( service == null )
		//	service = new CommunicationServiceRPC();
		
		return service;
	}
	
		
	public void loadData( LoaderAction action, ChoicetraxLoaderResponseCallback callback )
	{
		proxy.loadData( action, (AsyncCallback<LoaderResponse>) callback );
	}
	
	public void handleAction( HandlerAction action, ChoicetraxActionResponseCallback callback )
	{
		proxy.handleAction( action, (AsyncCallback<ActionResponse>) callback );
	}

}
