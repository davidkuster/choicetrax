package com.choicetrax.client.input;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.input.listeners.ListenIconListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class HyperlinkIconListen 
	extends Composite
{

	public HyperlinkIconListen( ChoicetraxViewManager manager, ReleaseDetail rd, int partnerID )
	{
		final ChoicetraxViewManager viewManager = manager;
		
		ChoicetraxConfigData configData = viewManager.getConfigData();
		String iconFilename = configData.lookupPartnerIconFilename( partnerID );
		String partnerName = configData.lookupPartnerName( partnerID );
		
		final HyperlinkImage listenIcon = new HyperlinkImage(
												"img/listenicons/listen_" + iconFilename,
												"Listen to audio clip from " + partnerName,
												new ListenIconListener( viewManager,
																		rd,
																		partnerID ) );
		
		initWidget( listenIcon );
		
		final int trackID = rd.getTrackID();
		final int pID = partnerID;

		DeferredCommand.addCommand( new Command() {
			public void execute() {
				if ( viewManager.getUserManager().hasListened( trackID, pID ) )
					listenIcon.addStyleName( "clickedIcon" );
			}
		});
	}
}
