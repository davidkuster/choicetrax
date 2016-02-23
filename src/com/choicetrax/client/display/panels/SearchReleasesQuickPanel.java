package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.data.MultiSelectList;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchQuickAction;
import com.choicetrax.client.constants.Constants;


public class SearchReleasesQuickPanel 
	extends SearchPanel
{
	
	
	public SearchReleasesQuickPanel( ChoicetraxViewManager manager )
	{
		super( manager );	
		
		initializeInputBoxes();
		
		this.add( createInputPanel() );
	}
	
	
	private void initializeInputBoxes()
	{
		quickSearchBox.addKeyDownHandler( this );
		quickSearchBox.setWidth( "300px" );
		
		quickSearchBox.setTitle( "Artist / Remixer / Title / Mix / Label" );
	}
	
	
	
	protected Panel createInputPanel()
	{
		HyperlinkLabel clear = new HyperlinkLabel( "Clear Search", new ClearSearchListener() );
		clear.setWordWrap( false );
		
		FlexTable table = new FlexTable();
		table.setBorderWidth( 0 );
		table.setCellPadding( 5 );
		table.setCellSpacing( 0 );
		
		table.setWidget( 0, 0, quickSearchBox );
		table.getFlexCellFormatter().setColSpan( 0, 0, 2 );
		table.getFlexCellFormatter().setHorizontalAlignment( 0, 0, 
										HasHorizontalAlignment.ALIGN_CENTER );
		
		table.setWidget( 1, 0, genreSelectPanel );
		table.getFlexCellFormatter().setHorizontalAlignment( 1, 0, 
										HasHorizontalAlignment.ALIGN_CENTER );
		
		table.setWidget( 1, 1, partnerSelectPanel );
		table.getFlexCellFormatter().setHorizontalAlignment( 1, 1, 
										HasHorizontalAlignment.ALIGN_CENTER );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing( 2 );
		hp.add( clear );
		hp.add( new HTML( "&nbsp;" ) );
		hp.add( searchButton );
		
		//table.setWidget( 2, 0, clear );
		//table.getFlexCellFormatter().setHorizontalAlignment( 2, 0, 
		//								HasHorizontalAlignment.ALIGN_RIGHT );
		
		table.setWidget( 2, 1, hp );
		table.getFlexCellFormatter().setHorizontalAlignment( 2, 1, 
										HasHorizontalAlignment.ALIGN_RIGHT );
		
		FlexTable wrapper = new FlexTable();
		wrapper.setWidth( "100%" );
		wrapper.setBorderWidth( 0 );
		
		wrapper.setWidget( 0, 0, table );
		wrapper.getFlexCellFormatter().setHorizontalAlignment( 0, 0, 
										HasHorizontalAlignment.ALIGN_CENTER );
		wrapper.getFlexCellFormatter().setVerticalAlignment( 0, 0, 
										HasVerticalAlignment.ALIGN_TOP );
				
		return wrapper;
	}
		
	
	
	public void onClick( ClickEvent event )
	{
		quickSearchBox.setFocus( false );
		
		LoaderHistoryAction action = createLoaderActionObj();
		
		if ( action != null )
			viewManager.updateHistoryDisplay( Constants.HISTORY_QUICKSEARCH 
												+ action.getActionString() );
		else
			Window.alert( "Please enter search data." );
	}
	
	
	/*public void onKeyDown( Widget sender, char keyCode, int modifiers )
	{
		if ( ( keyCode == KeyboardListener.KEY_ENTER )
				&& searchButton.isEnabled() )
		{
			quickSearchBox.setFocus( false );
			
			onClick( sender );
		}
	}*/
	
	
	protected LoaderHistoryAction createLoaderActionObj()
	{
		ReleasesSearchQuickAction requestObj = new ReleasesSearchQuickAction();
	    
		boolean initialized = false;
		
	    String searchTerms 	= quickSearchBox.getText();
	        
	    if ( ( searchTerms != null ) 
	    	&& ( ! "".equals( searchTerms.trim() ) ) 
	    	&& ( ! searchTerms.equals( quickSearchBox.getInitialText() ) ) )
	    {
	    	requestObj.setSearchTerms( searchTerms );
	    	initialized = true;
	    }
	    	    
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
	    
	    
	    //if ( viewManager.getCurrentUser() != null )
	    //	requestObj.setUserID( viewManager.getCurrentUser().getUserID() );
	    
	    if ( initialized )
	    	return requestObj;
	    else
	    	return null;
	}
	
	
	public void updateDisplay( ReleasesSearchQuickAction action )
	{
	    quickSearchBox.setText( action.getSearchTerms() );
	    
	    genreSelectPanel.populateSelectedIDs( action.getGenreIDs() );
	    partnerSelectPanel.populateSelectedIDs( action.getPartnerIDs() );
	}
	
	
	protected class ClearSearchListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			ReleasesSearchQuickAction action = new ReleasesSearchQuickAction();
			action.setSearchTerms( quickSearchBox.getInitialText() );
			
			updateDisplay( action );
		}	
	}
	
	
}
