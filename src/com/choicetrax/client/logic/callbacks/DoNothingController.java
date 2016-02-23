package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.actions.responses.ActionResponse;


public class DoNothingController implements ChoicetraxActionResponseCallback
{

	public void onFailure( Throwable caught ) {
	}

	public void onSuccess( ActionResponse result ) {
	}

}
