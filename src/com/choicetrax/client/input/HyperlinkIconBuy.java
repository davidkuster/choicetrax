package com.choicetrax.client.input;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;

import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.input.listeners.BuyIconListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class HyperlinkIconBuy 
	extends Composite
{
	
	
	public HyperlinkIconBuy( ChoicetraxViewManager manager, ReleaseDetail rd, int partnerID )
	{
		final ChoicetraxViewManager viewManager = manager;
		
		ChoicetraxConfigData configData = viewManager.getConfigData();

		String iconFilename = configData.lookupPartnerIconFilename( partnerID );
		String partnerName	= configData.lookupPartnerName( partnerID );
		
		if ( partnerID == 3 ) {
			PartnerInventory pi = rd.getPartnerInventory( partnerID );
			if ( viewManager.getUserManager().isRestricted( pi.getTerritoryRestrictions() ) )
				iconFilename = "test_beatport_icon_restricted3.gif";				
		}
		
		final HyperlinkImage buyIcon = new HyperlinkImage( 
											"img/buyicons/" + iconFilename,
											"Buy from " + partnerName,
											new BuyIconListener( viewManager, rd, partnerID ) );
		
		initWidget( buyIcon );
		
		final int trackID = rd.getTrackID();
		final int pID = partnerID;
			
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				if ( viewManager.getUserManager().isPurchased( trackID, pID ) )
					buyIcon.addStyleName( "clickedIcon" );
			}
		});
	}

}
