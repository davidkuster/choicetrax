package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.loaderactions.DJChartsSearchAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.MultiSelectList;
import com.choicetrax.client.input.ClearableTextBox;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class SearchChartsPanel 
	extends SearchPanel
{
	
	
	protected ListBox recentDateBox	= new ListBox();
	
	protected ClearableTextBox chartSearchBox	= new ClearableTextBox( 
														"Artist / Remixer / Label" );
		
	
	
	public SearchChartsPanel( ChoicetraxViewManager viewManager )
	{
		super( viewManager );

		initializeInputBoxes();
		
		this.add( createInputPanel() );
	}
	
	
	private void initializeInputBoxes()
	{
		// populate recentDateBox field
		for ( int x=0; x < Constants.BROWSE_DATE_OPTIONS.length; x++ )
			recentDateBox.addItem( Constants.BROWSE_DATE_OPTIONS[ x ] );
		
		chartSearchBox.addKeyDownHandler( this );
		chartSearchBox.setWidth( "165px" );
		
		chartSearchBox.setTitle( "Artist / Remixer / Label" );
	}
	
	
	protected Panel createInputPanel()
	{
		FlexTable row1 = new FlexTable();
		row1.setBorderWidth( 0 );
		row1.setWidget( 0, 0, chartSearchBox );
		row1.setWidget( 0, 1, new Label( "Recent Date: " ) );
		row1.setWidget( 0, 2, recentDateBox );
		
		FlexTable row2 = new FlexTable();
		row2.setWidth( "100%" );
		//row2.setWidget( 0, 0, hp );
		row2.setWidget( 0, 0, genreSelectPanel );
		row2.setWidget( 0, 1, partnerSelectPanel );
		row2.getCellFormatter().setHorizontalAlignment( 0, 0, HasHorizontalAlignment.ALIGN_CENTER );
		row2.getCellFormatter().setHorizontalAlignment( 0, 1, HasHorizontalAlignment.ALIGN_CENTER );
		
		FlexTable row3 = new FlexTable();
		row3.setWidget( 0, 0, createSaveClearPanel() );
		row3.setWidget( 0, 1, searchButton );
				
		
		FlexTable table = new FlexTable();
		table.setHeight( "100%" );
		table.setBorderWidth( 0 );
		table.setCellPadding( 3 );
		table.setCellSpacing( 0 );		
				
		table.setWidget( 0, 0, row1 );
		table.setWidget( 1, 0, row2 );
		table.setWidget( 2, 0, row3 );
		
		table.getCellFormatter().setHorizontalAlignment( 0, 0, HasHorizontalAlignment.ALIGN_CENTER );
		table.getCellFormatter().setHorizontalAlignment( 1, 0, HasHorizontalAlignment.ALIGN_CENTER );
		table.getCellFormatter().setHorizontalAlignment( 2, 0, HasHorizontalAlignment.ALIGN_RIGHT );
		
		FlexTable wrapper = new FlexTable();
		wrapper.setWidth( "100%" );
		wrapper.setBorderWidth( 0 );
		
		wrapper.setWidget( 0, 0, table );
		wrapper.getCellFormatter().setHorizontalAlignment( 0, 0, 
										HasHorizontalAlignment.ALIGN_CENTER );
						
		return wrapper;
	}
	
	
	
	private Panel createSaveClearPanel()
	{
		HyperlinkLabel save = new HyperlinkLabel( "Save Search", new EnterSearchNameListener() );
		HyperlinkLabel clear = new HyperlinkLabel( "Clear Search", new ClearSearchListener() );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing( 2 );
		hp.add( save );
		hp.add( new HTML( "&nbsp;" ) );
		hp.add( clear );
						
		return hp;
	}
	

	
	public void onClick( ClickEvent event )
	{
		chartSearchBox.setFocus( false );
		
		LoaderHistoryAction action = createLoaderActionObj();
				
		if ( action != null )
			viewManager.updateHistoryDisplay( Constants.HISTORY_CHARTS_DJ_SELECT 
												+ action.getActionString() );
		else
			Window.alert( "Please enter search data." );
	}
	
	
	
	public void updateDisplay( DJChartsSearchAction action )
	{
		chartSearchBox.setText( action.getSearchTerms() );
		
		String recentDate = action.getRecentDate();
		if ( recentDate != null )
		{
			int recentCount = recentDateBox.getItemCount();
			for ( int r=0; r < recentCount; r++ )
			{
				String recent = recentDateBox.getItemText( r );
				if ( recent.equals( recentDate ) )
				{
					recentDateBox.setSelectedIndex( r );
					break;
				}
			}
		} else recentDateBox.setSelectedIndex( 0 );
	    
		genreSelectPanel.populateSelectedIDs( action.getGenreIDs() );
		partnerSelectPanel.populateSelectedIDs( action.getPartnerIDs() );
	}
	
	
	
	protected LoaderHistoryAction createLoaderActionObj()
	{
		DJChartsSearchAction requestObj = new DJChartsSearchAction();
	    
		boolean initialized = false;
		
		String searchTerms = chartSearchBox.getText();
		if ( ( searchTerms != null ) 
		    	&& ( ! "".equals( searchTerms.trim() ) ) 
		    	&& ( ! searchTerms.equals( chartSearchBox.getInitialText() ) ) )
	    {
			requestObj.setSearchTerms( searchTerms );
			initialized = true;
		}
		
	    int recentIndex	= recentDateBox.getSelectedIndex();
	    
	    MultiSelectList genreList = genreSelectPanel.getMultiSelectList();
	    if ( ( genreList != null ) && ( genreList.getSelected().size() > 0 ) )
	    {
	    	requestObj.setGenreIDs( genreList.getSelectedIDs() );
	    	initialized = true;
	    }
	    
	    MultiSelectList partnerList = partnerSelectPanel.getMultiSelectList();
	    if ( ( partnerList != null ) && ( partnerList.getSelected().size() > 0 ) )
	    {
	    	requestObj.setPartnerIDs( partnerList.getSelectedIDs() );
	    	initialized = true;
	    }

	    if ( recentIndex > 0 )
	    {
	    	requestObj.setRecentDate( recentDateBox.getItemText( recentIndex ) );
	    	initialized = true;
	    }
	    	    
	    if ( initialized )
	    	return requestObj;
	    else
	    	return null;
	}
	
	
	
	protected class ClearSearchListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			DJChartsSearchAction action = new DJChartsSearchAction();
			action.setSearchTerms( chartSearchBox.getInitialText() );
			
			updateDisplay( action );
		}	
	}

}
