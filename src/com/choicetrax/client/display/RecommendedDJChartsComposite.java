package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class RecommendedDJChartsComposite 
	extends AbstractDJChartSelectComposite
{

	
	public RecommendedDJChartsComposite( ChoicetraxViewManager manager ) {
		super( manager, Constants.PANEL_RECOMMENDED_CHARTS );
	}
	
	
	protected Widget createHeaderWidget()
	{
		Label header = new Label( "Recommended DJ Charts" );
		header.setStylePrimaryName( "mainPanelHeaderLabel" );
		
		return header;
	}
	
	
	
	protected Widget createHelpInfo()
	{
		String text1 = "<p><br>"
					+ "<center><b>You have no recommended DJ charts.</b></center>"
					+ "<p>"
					+ "Recommendations are based on the trax you have rated and added "
					+ "to your wishlist, virtual carts, and purchase history.  To get "
					+ "more recommendations, rate some trax and/or add trax to your wishlist "
					+ "or virtual carts."
					+ "<p>"
					+ "The more you use Choicetrax the more we will be able to recommend "
					+ "to you!";
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( new HTML( text1 ) );
		
		return vp;
	}

	
}
