package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.loaderactions.ReleasesBrowseAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.MultiSelectList;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class ReleasesBrowsePanel 
	extends SearchPanel
{
	
	
	protected ListBox recentDateBox			= new ListBox();
	protected CheckBox uniqueReleasesBox	= new CheckBox();
	protected ListBox minRatingBox			= new ListBox();
		
	
	
	public ReleasesBrowsePanel( ChoicetraxViewManager viewManager )
	{
		super( viewManager );

		// populate recentDateBox field
		for ( int x=0; x < Constants.BROWSE_DATE_OPTIONS.length; x++ )
			recentDateBox.addItem( Constants.BROWSE_DATE_OPTIONS[ x ] );
		
		minRatingBox.addItem( "" );
		minRatingBox.addItem( "5" );
		minRatingBox.addItem( "4" );
		minRatingBox.addItem( "3" );
		minRatingBox.addItem( "2" );
		minRatingBox.addItem( "1" );
		
		this.add( createInputPanel() );
	}
	
	
	protected Panel createInputPanel()
	{
		//uniqueReleasesBox.setEnabled( false );
		
		FlexTable row1 = new FlexTable();
		row1.setWidth( "100%" );
		row1.setBorderWidth( 0 );
		row1.setWidget( 0, 0, new Label( "Recent Date: " ) );
		row1.setWidget( 0, 1, recentDateBox );
		//row1.setWidget( 0, 2, new Label( "Only Exclusive Releases:" ) );
		//row1.setWidget( 0, 3, uniqueReleasesBox );
		row1.setWidget( 0, 4, new Label( "Minimum Rating:" ) );
		row1.setWidget( 0, 5, minRatingBox );
		
		FlexTable row2 = new FlexTable();
		row2.setWidth( "100%" );
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
		LoaderHistoryAction action = createLoaderActionObj();
				
		if ( action != null )
			//viewManager.executeAction( action, new SearchReleasesController( viewManager, action.getActionString() ) );
			viewManager.updateHistoryDisplay( Constants.HISTORY_BROWSE + action.getActionString() );
		else
			Window.alert( "Please enter browse data." );
	}
	
	
	
	public void updateDisplay( ReleasesBrowseAction action )
	{
		uniqueReleasesBox.setValue( action.isOnlyExclusive() );		
		
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
		minRatingBox.setItemSelected( action.getMinRating(), true );
	}
	
	
	
	protected LoaderHistoryAction createLoaderActionObj()
	{
		ReleasesBrowseAction requestObj = new ReleasesBrowseAction();
	    
		boolean initialized = false;
		
	    boolean exclusive	= uniqueReleasesBox.getValue();
	    int recentIndex		= recentDateBox.getSelectedIndex();
	    int ratingIndex		= minRatingBox.getSelectedIndex();
	    
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
	    
	    if ( ratingIndex > 0 )
	    {
	    	requestObj.setMinRating( Integer.parseInt( minRatingBox.getItemText( ratingIndex ) ) );
	    	initialized = true;
	    }
	    
	    //if ( viewManager.getCurrentUser() != null )
	    //	requestObj.setUserID( viewManager.getCurrentUser().getUserID() );
	    
	    requestObj.setOnlyExclusive( exclusive );
	    if ( exclusive ) initialized = true;
	    
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
			updateDisplay( new ReleasesBrowseAction() );
		}	
	}

}
