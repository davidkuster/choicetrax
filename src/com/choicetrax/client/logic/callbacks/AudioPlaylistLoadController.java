package com.choicetrax.client.logic.callbacks;

import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.user.client.Random;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.ReleaseDetail;


public class AudioPlaylistLoadController 
	implements ChoicetraxLoaderResponseCallback 
{
	
	private ChoicetraxViewManager viewManager = null;
	
	
	public AudioPlaylistLoadController( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
	}
	
	
	public void onFailure( Throwable t ) 
	{
		viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
	}

	public void onSuccess( LoaderResponse result )
	{
		Releases releases = (Releases) result;
		Iterator<ReleaseDetail> i = releases.getReleasesList().iterator();
		while ( i.hasNext() )
		{
			ReleaseDetail rd = i.next();
			
			LinkedList<PartnerInventory> inventoryList = rd.getPartnerAvailability();
			int inventorySize = inventoryList.size();
			int rand = Random.nextInt( inventorySize - 1 );
			int partnerID = inventoryList.get( rand ).getPartnerID();
			
			if ( viewManager.getListenManager().isPlaying() )
				viewManager.getListenManager().setAutoStart( false );
			
			viewManager.getUserManager().listenTrack( rd, partnerID );
		}
		
		viewManager.getListenManager().setAutoStart( true );
	}	

}
