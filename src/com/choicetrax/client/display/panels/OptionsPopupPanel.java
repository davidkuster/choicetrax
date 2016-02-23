package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class OptionsPopupPanel extends PopupPanel
{
	
	private ChoicetraxViewManager viewManager = null;
	private ReleaseDetail rd = null;
	
	private int senderLeft 	= 0;
	private int senderWidth = 0;
	private int senderTop 	= 0;
	private int senderHeight = 0;
	
	
	public OptionsPopupPanel( ChoicetraxViewManager manager,
							ReleaseDetail rd, 
							Widget widgetSender )
	{
		super( true );
		this.setAnimationEnabled( true );
		
		this.viewManager = manager;
		this.rd = rd;
		
		this.senderLeft 	= widgetSender.getAbsoluteLeft();
		this.senderWidth 	= widgetSender.getOffsetWidth();
		this.senderTop 		= widgetSender.getAbsoluteTop();
		this.senderHeight 	= widgetSender.getOffsetHeight();
		
		//this.add( new PopupDecoratorPanel( createDisplayPanel() ) );
		this.add( createDisplayPanel() );
		this.setStyleName( "popup" );
		
		this.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) 
			{
				int left = senderLeft + senderWidth - offsetWidth;
				int top = senderTop + senderHeight;
				OptionsPopupPanel.this.setPopupPosition(left, top);
			}
		});
	}
	
		
	
	public FlexTable createDisplayPanel()
	{
		Label search = new HyperlinkLabel( "Search for this track at all stores",
											new SearchListener() );
		
		HTML recommend = new HTML( "Recommend similar tracks <i>(coming soon!)</i>" );
		
		SharingPanel addThis = new SharingPanel( viewManager, rd );
		
		FlexTable table = new FlexTable();	
		int row = 0;
		
		table.setWidget( row, 0, new HTML( "<b>Search</b>" ) );
		table.getFlexCellFormatter().setHorizontalAlignment( row++, 0, 
											HasHorizontalAlignment.ALIGN_CENTER );
		table.setWidget( row++, 0, search );
		table.setWidget( row++, 0, recommend );
		table.setWidget( row++, 0, new Label( " " ) );
		table.setWidget( row, 0, new HTML( "<b>Share This Track</b>" ) );
		table.getFlexCellFormatter().setHorizontalAlignment( row++, 0, 
											HasHorizontalAlignment.ALIGN_CENTER );
		table.setWidget( row, 0, addThis );
		table.getFlexCellFormatter().setHorizontalAlignment( row, 0, 
											HasHorizontalAlignment.ALIGN_CENTER );
				
		return table;
	}
	
	
	
	
	
	
	private class SearchListener implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{
			String artist = readFirstWord( rd.getArtist().getArtistName() );
			String track = readFirstWord( rd.getTrackName() );
			String mix = readFirstWord( rd.getMixName() );
			String label = readFirstWord( rd.getLabel().getLabelName() );
			
			if ( ( artist != null ) && ( artist.indexOf( '-' ) != -1 ) )
				artist = "\"" + artist + "\"";
			
			if ( ( mix != null ) && ( ! mix.equalsIgnoreCase( "original" ) ) )
				track = track + " " + mix;
			
			ReleasesSearchAdvancedAction action = new ReleasesSearchAdvancedAction();
			action.setArtistName( artist );
			action.setTrackName( track );
			action.setLabelName( label );
			
			OptionsPopupPanel.this.hide();			
			viewManager.updateHistoryDisplay( Constants.HISTORY_SEARCH 
												+ action.getActionString() );
		}
		
		private String readFirstWord( String text ) 
		{
			if ( text == null || "".equals( text ) ) return null;
			
			int index = text.indexOf( ' ' );
			if ( index != -1 ) {
				return text.substring( 0, index );
			}
			else 
				return text;
		}
	}

}
