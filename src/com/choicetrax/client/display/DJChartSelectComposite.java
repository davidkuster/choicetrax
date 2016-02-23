package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class DJChartSelectComposite 
	extends AbstractDJChartSelectComposite
{
	

	
	public DJChartSelectComposite( ChoicetraxViewManager manager ) {
		super( manager, Constants.PANEL_DJ_CHARTS_SELECT );
	}
	
	
	
	protected Widget createHeaderWidget()
	{
		Label header = new Label( "DJ Charts" );
		header.setStylePrimaryName( "mainPanelHeaderLabel" );
		
		return header;
	}

}
