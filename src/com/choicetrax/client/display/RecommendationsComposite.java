package com.choicetrax.client.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.loaderactions.RecommendedDJChartsAction;
import com.choicetrax.client.actions.loaderactions.RecommendedTracksAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.User;
import com.choicetrax.client.display.panels.decorators.BoxTabPanel;
import com.choicetrax.client.input.HyperlinkLabel;


public class RecommendationsComposite 
	extends Composite
	implements ChoicetraxViewComponent
{
	private ChoicetraxViewManager viewManager = null;
	private VerticalPanel recommendedPanel = null;
	
	
	public RecommendationsComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		VerticalPanel panel = new VerticalPanel();
		initWidget( panel );
				
		BoxTabPanel tp = new BoxTabPanel();
		tp.add( createRecommendedPanel(), new Image( "img/layout/tabs/recommended_on.gif" ) );
		tp.selectTab( 0 );
		tp.setStyleName( "recommendedPanel" );	
		panel.add( tp );
	}
	
	
	private Panel createRecommendedPanel()
	{
		recommendedPanel = new VerticalPanel();
		recommendedPanel.add( new HTML( "Please login to load Recommendations.<br>&nbsp;" ) );
		recommendedPanel.setStyleName( "recommendedInfoPanel" );
		
		return recommendedPanel;
	}
	
	
	public void clearDisplay() 
	{ 
		recommendedPanel.clear();
		recommendedPanel.add( new HTML( "Please login to load Recommendations.<br>&nbsp;" ) );
		recommendedPanel.setStyleName( "recommendedInfoPanel" );
	}
	
	public void setWaitingState( boolean waiting ) { }
	
	
	public void updateDisplay( LoaderResponse responseObj ) 
	{ 
		User user = (User) responseObj;
		if ( user != null )
		{
			recommendedPanel.clear();
			
			//recommendedPanel.add( new Label( "You have XX recommendations based on XX ratings. " ) );
			//recommendedPanel.add( new HyperlinkLabel( 
			//						"View Recommendations", 
			//						new RecommendedListener() ) );
			//recommendedPanel.add( new HTML( "<i>Recommendations <br>Coming Soon!</i>" ) );
			
			HyperlinkLabel tracks = new HyperlinkLabel( "View Recommended Tracks",
														new RecommendedTracksListener() );		
			HyperlinkLabel charts = new HyperlinkLabel( "View Recommended Charts",
														new RecommendedChartsListener() );
			
			tracks.addStyleName( "italicText" );
			charts.addStyleName( "italicText" );
			
			recommendedPanel.add( tracks );
			recommendedPanel.add( charts );
			
			recommendedPanel.setStyleName( "recommendedInfoPanel" );
		}
	}
	
	
	
	private class RecommendedTracksListener implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{
			RecommendedTracksAction action = new RecommendedTracksAction();
			action.setRecommendationType( Constants.LOAD_RECOMMENDED_TRACKS );
			
			viewManager.updateHistoryDisplay( Constants.HISTORY_RECOMMENDED_TRACKS
												+ action.getActionString() );
		}
	}
	
	private class RecommendedChartsListener implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{
			RecommendedDJChartsAction action = new RecommendedDJChartsAction();
			action.setRecommendationType( Constants.LOAD_RECOMMENDED_CHARTS );
			
			viewManager.updateHistoryDisplay( Constants.HISTORY_RECOMMENDED_CHARTS
												+ action.getActionString() );			
		}
	}
	
}
