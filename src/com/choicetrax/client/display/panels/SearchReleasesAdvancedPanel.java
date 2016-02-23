package com.choicetrax.client.display.panels;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.data.MultiSelectList;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.input.DateField;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.constants.Constants;


public class SearchReleasesAdvancedPanel 
	extends SearchPanel
{
		
	protected DateField dateBeginBox = new DateField( "Begin Date" );
	protected DateField dateEndBox = new DateField( "End Date" );

	
	
	public SearchReleasesAdvancedPanel( ChoicetraxViewManager manager )
	{
		super( manager );	
		
		initializeInputBoxes();
		
		this.add( createInputPanel() );
	}
	
	
	private void initializeInputBoxes()
	{
		artistSearchBox.addKeyDownHandler( this );
		titleSearchBox.addKeyDownHandler( this );
		labelSearchBox.addKeyDownHandler( this );
		//dateBeginBox.addKeyboardListener( this );
		//dateEndBox.addKeyboardListener( this );
		
		artistSearchBox.setTitle( "Artist / Remixer Name" );
		titleSearchBox.setTitle( "Track / Remix / EP Name" );
		labelSearchBox.setTitle( "Label Name" );
		dateBeginBox.setTitle( "Begin Date - mm/dd/yyyy" );
		dateEndBox.setTitle( "End Date - mm/dd/yyyy" );
		
		dateBeginBox.setWidth( "100px" );
		dateEndBox.setWidth( "100px" );
	}
	
	
	
	private Panel createSaveClearPanel()
	{
		HyperlinkLabel save = new HyperlinkLabel( "Save Search", new EnterSearchNameListener() );
		HyperlinkLabel clear = new HyperlinkLabel( "Clear Search", new ClearSearchListener() );
		
		save.setWordWrap( false );
		clear.setWordWrap( false );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add( save );
		hp.add( new HTML( "&nbsp;" ) );
		hp.add( clear );
						
		return hp;
	}
	
	
	protected Panel createInputPanel()
	{
		FlexTable table = new FlexTable();
		table.setBorderWidth( 0 );
		table.setCellPadding( 0 );
		table.setCellSpacing( 0 );
		
		table.setWidget( 0, 0, artistSearchBox );
		table.setWidget( 0, 1, titleSearchBox );
		table.setWidget( 0, 2, labelSearchBox );
		
		table.setWidget( 1, 0, dateBeginBox );
		table.setWidget( 1, 1, dateEndBox );
		table.setWidget( 1, 2, partnerSelectPanel );
		
		FlexTable wrapper = new FlexTable();
		wrapper.setWidth( "100%" );
		wrapper.setBorderWidth( 0 );
		
		wrapper.setWidget( 0, 0, table );
		wrapper.getFlexCellFormatter().setColSpan( 0, 0, 3 );
		
		wrapper.setWidget( 1, 0, genreSelectPanel );
		wrapper.setWidget( 1, 1, createSaveClearPanel() );
		wrapper.setWidget( 1, 2, searchButton );
		
		wrapper.getCellFormatter().setHorizontalAlignment( 1, 1, 
										HasHorizontalAlignment.ALIGN_RIGHT );
		
		return wrapper;
	}
		
	
	
	public void onClick( ClickEvent event )
	{
		artistSearchBox.setFocus( false );
		titleSearchBox.setFocus( false );
		labelSearchBox.setFocus( false );
		
		LoaderHistoryAction action = createLoaderActionObj();
		
		if ( action != null )
			//viewManager.executeAction( action, new SearchReleasesController( viewManager, action.getActionString() ) );
			viewManager.updateHistoryDisplay( Constants.HISTORY_SEARCH + action.getActionString() );
		else
			Window.alert( "Please enter search data." );
	}
	
	
	protected LoaderHistoryAction createLoaderActionObj()
	{
		ReleasesSearchAdvancedAction requestObj = new ReleasesSearchAdvancedAction();
	    
		boolean initialized = false;
		
	    String artistName 	= artistSearchBox.getText();
	    String title		= titleSearchBox.getText();
	    String labelName	= labelSearchBox.getText();
	    Date dateBegin		= dateBeginBox.getDateValue();
	    Date dateEnd		= dateEndBox.getDateValue();
	        
	    if ( ( artistName != null ) 
	    	&& ( ! "".equals( artistName.trim() ) ) 
	    	&& ( ! artistName.equals( artistSearchBox.getInitialText() ) ) )
	    {
	    	requestObj.setArtistName( artistName );
	    	initialized = true;
	    }
	    
	    if ( ( title != null ) 
	    	&& ( ! "".equals( title.trim() ) )
	    	&& ( ! title.equals( titleSearchBox.getInitialText() ) ) )
	    {
	    	requestObj.setTrackName( title );
	    	initialized = true;
	    }
	    
	    if ( ( labelName != null ) 
	    	&& ( ! "".equals( labelName.trim() ) ) 
	    	&& ( ! labelName.equals( labelSearchBox.getInitialText() ) ) )
	    {
	    	requestObj.setLabelName( labelName );
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
	    
	    if ( dateBegin != null )
	    {
	    	requestObj.setReleaseDateBegin( dateBegin );
	    	initialized = true;
	    }
	    
	    if ( dateEnd != null )
	    {
	    	requestObj.setReleaseDateEnd( dateEnd );
	    	initialized = true;
	    }
	    
	    //if ( viewManager.getCurrentUser() != null )
	    //	requestObj.setUserID( viewManager.getCurrentUser().getUserID() );
	    
	    if ( initialized )
	    	return requestObj;
	    else
	    	return null;
	}
	
	
	public void updateDisplay( ReleasesSearchAdvancedAction action )
	{
	    artistSearchBox.setText( 	action.getArtistName()	);
	    titleSearchBox.setText(		action.getTrackName() 	);
	    labelSearchBox.setText( 	action.getLabelName() 	);
	    
	    Date beginDT = action.getReleaseDateBegin();
	    if ( beginDT != null )
	    	//dateBeginBox.setDate( Constants.DATE_FORMATTER_USA.format( beginDT ) );
	    	dateBeginBox.setSelectedDate( beginDT );
	    else 
	    	dateBeginBox.setText( dateBeginBox.getInitialText() );
	    
	    Date endDT = action.getReleaseDateEnd();
	    if ( endDT != null )
	    	//dateEndBox.setDate(	Constants.DATE_FORMATTER_USA.format( endDT ) );
	    	dateEndBox.setSelectedDate( endDT );
	    else
	    	dateEndBox.setText( dateEndBox.getInitialText() );

	    genreSelectPanel.populateSelectedIDs( action.getGenreIDs() );
	    partnerSelectPanel.populateSelectedIDs( action.getPartnerIDs() );
	}
	
	
	
	protected class ClearSearchListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			ReleasesSearchAdvancedAction action = new ReleasesSearchAdvancedAction();
			action.setArtistName( artistSearchBox.getInitialText() );
			action.setTrackName( titleSearchBox.getInitialText() );
			action.setLabelName( labelSearchBox.getInitialText() );
			
			updateDisplay( action );
		}	
	}
	
}
