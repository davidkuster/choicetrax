package com.choicetrax.client.logic.callbacks;

import java.util.LinkedHashMap;
import java.util.Iterator;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.ReleaseDetail;


public class AudioSavedPlaylistLoadController 
	implements ChoicetraxLoaderResponseCallback 
{
	
	private ChoicetraxViewManager viewManager = null;
	private LinkedHashMap<Integer, Integer> partnerMap = null;
	
	
	public AudioSavedPlaylistLoadController( ChoicetraxViewManager manager,
										LinkedHashMap<Integer, Integer> map )
	{
		this.viewManager = manager;
		this.partnerMap = map;
	}
	
	
	public void onFailure( Throwable t ) 
	{
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}

	public void onSuccess( LoaderResponse result )
	{
		viewManager.getListenManager().setAutoStart( false );
		
		Releases releases = (Releases) result;
		Iterator<ReleaseDetail> i = releases.getReleasesList().iterator();
		while ( i.hasNext() )
		{
			ReleaseDetail rd = i.next();
			int partnerID = (int) partnerMap.get( new Integer( rd.getTrackID() ) );
			
			viewManager.getUserManager().listenTrack( rd, partnerID );
		}
		
		viewManager.getListenManager().setAutoStart( true );
	}	

}
