package com.choicetrax.client.logic.callbacks;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;


public class AlbumEPLoadController implements ChoicetraxLoaderResponseCallback 
{
	private ChoicetraxViewManager viewManager = null;
	private int epPanel = Constants.PANEL_ALBUM_EP_DETAILS;
	private int releasesPanel = Constants.PANEL_RELEASES;

	public AlbumEPLoadController( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		viewManager.setWaitingState( releasesPanel, true );
	}
	
	public void onFailure( Throwable t )
	{
		viewManager.setWaitingState( releasesPanel, false );
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}
	
	public void onSuccess( LoaderResponse result )
	{
		viewManager.setWaitingState( releasesPanel, false );
		
		viewManager.updateDisplay( epPanel, result );
	}

}
