package com.choicetrax.server.db;

import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;

public interface ActionHandler 
{
	
	public ActionResponse handleAction()
		throws ChoicetraxException;

}
