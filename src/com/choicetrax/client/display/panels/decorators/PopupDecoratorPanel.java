package com.choicetrax.client.display.panels.decorators;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.constants.Constants;


public class PopupDecoratorPanel extends SimplePanel
{

	
	public PopupDecoratorPanel( Widget centerWidget ) {
		super();
		this.add( createBoxTable( centerWidget ) );
	}
	
	
	private FlexTable createBoxTable( Widget centerWidget )
	{
		FlexTable table = new FlexTable();
		table.setStylePrimaryName( "popup" );
		table.setCellPadding( 0 );
		table.setCellSpacing( 0 );
		
		table.setWidget( 0, 0, new Image( Constants.IMAGE_BUNDLE_CORNERS.popupTopLeft() ) );
		table.getCellFormatter().addStyleName( 0, 1, "popupBg" );
		table.setWidget( 0, 2, new Image( Constants.IMAGE_BUNDLE_CORNERS.popupTopRight() ) );
		
		table.getCellFormatter().addStyleName( 1, 0, "popupBg" );
		table.setWidget( 1, 1, centerWidget );
		table.getCellFormatter().setStylePrimaryName( 1, 1, "popupContent" );
		table.getCellFormatter().addStyleName( 1, 2, "popupBg" );
		
		table.setWidget( 2, 0, new Image( Constants.IMAGE_BUNDLE_CORNERS.popupBottomLeft() ) );
		table.getCellFormatter().addStyleName( 2, 1, "popupBg" );
		table.setWidget( 2, 2, new Image( Constants.IMAGE_BUNDLE_CORNERS.popupBottomRight() ) );
				
		return table;
	}

}
