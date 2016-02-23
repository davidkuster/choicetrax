package com.choicetrax.client.display;

import com.choicetrax.client.actions.responses.LoaderResponse;

public interface ChoicetraxViewComponent 
{
	
	public void setWaitingState( boolean waiting );
	
	public void updateDisplay( LoaderResponse responseObj );
	
	public void clearDisplay();

}
