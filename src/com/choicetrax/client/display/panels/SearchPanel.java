package com.choicetrax.client.display.panels;

import java.util.*;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.data.*;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.input.ClearableTextBox;
import com.choicetrax.client.input.ClickableImage;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.input.MultiSelectPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public abstract class SearchPanel
	extends SimplePanel
	implements ClickHandler, KeyDownHandler
{	
	protected ChoicetraxViewManager viewManager = null;
	
	protected ClickableImage searchButton = new ClickableImage( "img/layout/btnSearchBlue.gif" );
	
	protected ClearableTextBox artistSearchBox 	= new ClearableTextBox( "Artist" );
	protected ClearableTextBox titleSearchBox	= new ClearableTextBox( "Title" );
	protected ClearableTextBox labelSearchBox	= new ClearableTextBox( "Label" );
	protected ClearableTextBox quickSearchBox	= new ClearableTextBox( 
													"Artist / Remixer / Title / Mix / Label" );
	
	protected MultiSelectPanel genreSelectPanel 	= new MultiSelectPanel( "Genres" );
	protected MultiSelectPanel partnerSelectPanel	= new MultiSelectPanel( "Stores" );
		


	public SearchPanel( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		searchButton.addClickHandler( this );
	}
	
	
	public void populateConfigData( ChoicetraxConfigData configData )
	{
		List<Genre> genreList = configData.getChoicetraxGenres();
		List<Partner> partnerList = configData.getChoicetraxPartners();
		
		if ( genreList != null )
			genreSelectPanel.setMultiSelectList( genreList );
		
		if ( partnerList != null )
			partnerSelectPanel.setMultiSelectList( partnerList );
	}
	
	
	
	protected abstract Panel createInputPanel();
	
	
	
	public void setSearchButtonEnabled( boolean enabled )
	{
		searchButton.setEnabled( enabled );
	}
	
	public void setSearchButtonFocus( boolean focus )
	{
		//searchButton.setFocus( focus );
	}
	
	
	public abstract void onClick( ClickEvent event );
		
	
	/* KEYBOARD LISTENER METHODS */
	public void onKeyDown( KeyDownEvent event )
	{
		if ( ( event.getNativeKeyCode() == KeyCodes.KEY_ENTER )
				&& searchButton.isEnabled() )
		{
			//artistSearchBox.setFocus( false );
			//titleSearchBox.setFocus( false );
			//labelSearchBox.setFocus( false );
			//dateBeginBox.setFocus( false );
			//dateEndBox.setFocus( false );
			
			onClick( null );
		}
	}
	
	
	protected abstract LoaderHistoryAction createLoaderActionObj();
	
		
	
	
	protected class EnterSearchNameListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{ 
			if ( viewManager.getCurrentUser() != null )
				createSaveSearchPanel( (Widget) event.getSource() );
			else
				Window.alert( "You must be logged in to save a search." );
		}
		
		private void createSaveSearchPanel( Widget sender )
		{
			VerticalPanel vp = new VerticalPanel();
			vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
			vp.setSpacing( 5 );
			
			PopupPanel popup = new PopupPanel( true );
			popup.setAnimationEnabled( true );
			
			TextBox searchNameBox = new TextBox();
			searchNameBox.setMaxLength( 100 );
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.add( new Label( "Search Name: " ) );
			hp.add( searchNameBox );
			vp.add( hp );
			
			vp.add( new HyperlinkLabel( "Save", new SaveSearchListener( popup, searchNameBox ) ) );
			
			popup.add( vp );
			popup.setStyleName( "popup" );
			popup.show();
			popup.setPopupPosition( sender.getAbsoluteLeft(), sender.getAbsoluteTop() + sender.getOffsetHeight() );
		}
	}
	
	
	
	protected class SaveSearchListener
		implements ClickHandler
	{
		private TextBox nameBox = null;
		private PopupPanel popup = null;
		
		public SaveSearchListener( PopupPanel popup, TextBox searchNameBox )
		{
			this.popup = popup;
			this.nameBox = searchNameBox;
		}
		
		public void onClick( ClickEvent event ) 
		{
			final String searchName = nameBox.getText();
			
			if ( ( searchName != null ) 
				&& ( ! searchName.trim().equals( "" ) ) )
			{
				popup.hide();
				
				DeferredCommand.addCommand( new Command() {
					public void execute() {
						viewManager.getUserManager().addFavoriteSearch( searchName, 
																		createLoaderActionObj() );
					}
				});
			}
			else
				Window.alert( "Please enter a search name." );
		}
	}
	
}
