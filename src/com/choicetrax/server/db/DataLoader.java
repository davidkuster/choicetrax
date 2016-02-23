package com.choicetrax.server.db;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;

public interface DataLoader 
{
	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException;

}
