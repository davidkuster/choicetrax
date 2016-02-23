package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;


public class SearchResultsDJChartController 
	extends SearchResultsController 
{	
	
	public SearchResultsDJChartController( ChoicetraxViewManager manager, 
									 String actionString, 
									 int pageNum )
	{
		super( manager, actionString, pageNum );
		this.setRedirectPanel( Constants.PANEL_DJ_CHARTS_VIEW );
	}
	
	public void onSuccess( LoaderResponse result ) 
	{ 
		viewManager.setWaitingState( searchPanel, false );
		viewManager.setWaitingState( releasesPanel, false );
		
		DJChartsCache chart = (DJChartsCache) result;
		
		viewManager.updateDisplay( redirectPanel, chart );
	}

}
