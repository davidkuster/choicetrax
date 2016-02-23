package com.choicetrax.client.display.panels.decorators;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.constants.Constants;


public class BoxDecoratorPanel extends SimplePanel
{
	
	private FlexTable table = null;
	
	
	
	public BoxDecoratorPanel( Widget centerWidget )
	{
		super();
		
		this.table = createBoxTable( centerWidget );
		this.add( table );
	}
	
	
	public void setHeight( String height ) {
		table.getCellFormatter().setHeight( 1, 1, height );
	}
	
	
	
	private FlexTable createBoxTable( Widget centerWidget )
	{
		FlexTable table = new FlexTable();
		table.setStylePrimaryName( "box" );
		table.setCellPadding( 0 );
		table.setCellSpacing( 0 );
		table.setWidth( "100%" );
		
		table.setWidget( 0, 0, new Image( Constants.IMAGE_BUNDLE_CORNERS.boxTopLeft() ) );
		table.getCellFormatter().addStyleName( 0, 1, "boxBg" );
		table.getCellFormatter().setWidth( 0, 1, "100%" );
		table.setWidget( 0, 2, new Image( Constants.IMAGE_BUNDLE_CORNERS.boxTopRight() ) );
		
		table.getCellFormatter().addStyleName( 1, 0, "boxBg" );
		table.setWidget( 1, 1, centerWidget );
		table.getCellFormatter().setStylePrimaryName( 1, 1, "boxContent" );
		table.getCellFormatter().addStyleName( 1, 2, "boxBg" );
		
		table.setWidget( 2, 0, new Image( Constants.IMAGE_BUNDLE_CORNERS.boxBottomLeft() ) );
		table.getCellFormatter().addStyleName( 2, 1, "boxBg" );
		table.setWidget( 2, 2, new Image( Constants.IMAGE_BUNDLE_CORNERS.boxBottomRight() ) );
		
		return table;
	}

}
