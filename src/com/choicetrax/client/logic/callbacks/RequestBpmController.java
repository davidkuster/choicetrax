package com.choicetrax.client.logic.callbacks;

import com.google.gwt.user.client.Timer;

import com.choicetrax.client.actions.loaderactions.RequestBpmAction;
import com.choicetrax.client.actions.responses.BpmResponse;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.AudioPlaylistItem;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxListenManager;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class RequestBpmController implements ChoicetraxLoaderResponseCallback
{
	
	private ChoicetraxViewManager viewManager = null;
	private int attemptNum = 0;
	
	
	public RequestBpmController( ChoicetraxViewManager manager ) {
		super();
		this.viewManager = manager;
	}

	
	public void onFailure( Throwable t ) {
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}
	

	public void onSuccess( LoaderResponse result ) 
	{
		BpmResponse response = (BpmResponse) result;
		
		ChoicetraxListenManager listenManager = viewManager.getListenManager();
		final AudioPlaylistItem item = listenManager.getPlayingItem();
		
		final ReleaseDetail rd = item.getReleaseDetail();
		int currentTrackID = rd.getTrackID();
		
		if ( response.getTrackID() == currentTrackID ) 
		{
			float bpm = response.getBPM();
			
			// if BPM has been returned, store it in ReleaseDetail obj
			// and update audio player UI
			if ( bpm > 0 || bpm == -1 )
			{
				rd.setBpm( bpm + "" );
				rd.setBpmConfidence( response.getConfidence() + "" );
				
				viewManager.getView().getAudioPlayerComposite().updateTrackInfo( item );
			}
			else
			{
				// sleep 30 seconds, then poll server
				attemptNum++;
				
				if ( attemptNum < 3 )
				{
					Timer t = new Timer() 
					{
						public void run() 
						{
							int trackID = rd.getTrackID();
							int partnerID = item.getPartnerID();
							String audioPreviewURL = rd.getPartnerInventory( partnerID ).getAudioPreviewURL();
							
							viewManager.deferAction( new RequestBpmAction( trackID, audioPreviewURL ), 
													 RequestBpmController.this );
						}
					};
					
					t.schedule( 30000 );
				}
			}
		}
	}


}
