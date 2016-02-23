package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.DJChartsSearchAction;
import com.choicetrax.client.actions.loaderactions.ReleasesBrowseAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.display.panels.SearchChartsPanel;
import com.choicetrax.client.display.panels.ReleasesBrowsePanel;
import com.choicetrax.client.display.panels.SearchReleasesAdvancedPanel;
import com.choicetrax.client.display.panels.SearchReleasesQuickPanel;
import com.choicetrax.client.display.panels.decorators.BoxTabPanel;
import com.choicetrax.client.input.ClickableImageOnOff;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class SearchComposite 
	extends Composite 
	implements ChoicetraxViewComponent
{
	
	private ChoicetraxViewManager viewManager = null; 
	private BoxTabPanel tabPanel = null;
	
	private SearchReleasesQuickPanel quickSearchPanel;
	private SearchReleasesAdvancedPanel searchReleasesPanel;
	private ReleasesBrowsePanel browseReleasesPanel;
	private SearchChartsPanel chartsBrowsePanel;
	
	
	public SearchComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		tabPanel = new BoxTabPanel();
		initWidget( tabPanel );
		tabPanel.getDeckPanel().setAnimationEnabled( true );
		
		quickSearchPanel	= new SearchReleasesQuickPanel( viewManager );
		searchReleasesPanel = new SearchReleasesAdvancedPanel( viewManager );
		browseReleasesPanel = new ReleasesBrowsePanel( viewManager );
		chartsBrowsePanel	= new SearchChartsPanel( viewManager );
		
		quickSearchPanel.setSearchButtonEnabled( false );
		searchReleasesPanel.setSearchButtonEnabled( false );
		browseReleasesPanel.setSearchButtonEnabled( false );
		chartsBrowsePanel.setSearchButtonEnabled( false );
		
		tabPanel.add( quickSearchPanel,
				new ClickableImageOnOff(
						new Image( Constants.IMAGE_BUNDLE_TABS.searchquick_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.searchquick_off() ),
						ClickableImageOnOff.DEFAULT_ON ) );
		tabPanel.add( searchReleasesPanel, 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.searchadvanced_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.searchadvanced_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		tabPanel.add( browseReleasesPanel, 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.browse_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.browse_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		tabPanel.add( chartsBrowsePanel, 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.charts_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.charts_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		
		tabPanel.selectTab( 0 );
		
		tabPanel.setStylePrimaryName( "searchTabPanel" );
		//tabPanel.setStylePrimaryName( "box" );
		//tabPanel.setWidth( "100%" );
		tabPanel.setHeight( "100px" );
	}
	
	
	public void initialize()
	{
		ChoicetraxConfigData configData = viewManager.getConfigData();
		
		quickSearchPanel.populateConfigData( configData );
		searchReleasesPanel.populateConfigData( configData );
		browseReleasesPanel.populateConfigData( configData );
		chartsBrowsePanel.populateConfigData( configData );
		
		quickSearchPanel.setSearchButtonEnabled( true );
		searchReleasesPanel.setSearchButtonEnabled( true );
		browseReleasesPanel.setSearchButtonEnabled( true );
		chartsBrowsePanel.setSearchButtonEnabled( true );
	}
	
	
	public void setWaitingState( boolean waiting )
	{
		// don't disable the search buttons in case of error
		// (if response never returns, search buttons will stay
		// disabled)
		
		//searchReleasesPanel.setSearchButtonEnabled( ! waiting );
		//browseReleasesPanel.setSearchButtonEnabled( ! waiting );

		quickSearchPanel.setSearchButtonFocus( false );
		searchReleasesPanel.setSearchButtonFocus( false );
		browseReleasesPanel.setSearchButtonFocus( false );
		chartsBrowsePanel.setSearchButtonFocus( false );
	}
	
	
	public void clearDisplay() { }
	
	
	public void updateDisplay( LoaderResponse responseObj ) { }
	
	
	/**
	 * note that this isn't exactly following protocol...
	 * @param action
	 */
	public void updateDisplay( LoaderAction action )
	{
		if ( action instanceof ReleasesSearchAdvancedAction )
		{
			tabPanel.selectTab( 1 );
			searchReleasesPanel.updateDisplay( (ReleasesSearchAdvancedAction) action );
		}
		else if ( action instanceof ReleasesBrowseAction )
		{
			tabPanel.selectTab( 2 );
			browseReleasesPanel.updateDisplay( (ReleasesBrowseAction) action );
		}
		else if ( action instanceof DJChartsSearchAction )
		{
			tabPanel.selectTab( 3 );
			chartsBrowsePanel.updateDisplay( (DJChartsSearchAction ) action );
		}
	}
	
}
