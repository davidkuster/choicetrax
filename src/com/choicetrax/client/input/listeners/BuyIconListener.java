package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.display.panels.BuyPopupPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;

public class BuyIconListener 
	implements ClickHandler
{
	
	private ChoicetraxViewManager viewManager = null;
	private ReleaseDetail rd = null;
	private int partnerID = 0;
	
				
	public BuyIconListener( ChoicetraxViewManager manager,
							ReleaseDetail rd, 
							int partnerID )
	{
		this.viewManager = manager;
		this.rd = rd;
		this.partnerID = partnerID;
	}
	
	
	//public void onClick( Widget sender )
	public void onClick( ClickEvent event )
	{
		PartnerInventory pi = rd.getPartnerInventory( partnerID );
		new BuyPopupPanel( viewManager, rd, pi, (Widget) event.getSource() );
	}
	
}