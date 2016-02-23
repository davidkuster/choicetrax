package com.choicetrax.client.display.panels;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class ReleaseRatingPanelUserOnly extends ReleaseRatingPanel
{

	public ReleaseRatingPanelUserOnly( ChoicetraxViewManager manager, ReleaseDetail rd ) {
		super( manager, rd );
	}
	
	
	protected void initPanel()
	{
		VerticalPanel ratingPanel = new VerticalPanel();
		initWidget( ratingPanel );
			
		ratingPanel.setStylePrimaryName( "ratingPanel" );
		userRatingPanel.addStyleName( "smallerText" );
		
		ratingPanel.add( userRatingPanel );
		ratingPanel.setCellHorizontalAlignment( userRatingPanel, HasHorizontalAlignment.ALIGN_CENTER );
		
		updateRatingDisplay( rd.getUserRating() );
	}

}
