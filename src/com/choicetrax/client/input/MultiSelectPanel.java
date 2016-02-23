package com.choicetrax.client.input;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.data.MultiSelectList;
import com.choicetrax.client.data.MultiSelectItem;
import com.choicetrax.client.display.panels.decorators.PopupDecoratorPanel;
import com.choicetrax.client.input.HyperlinkLabel;


public class MultiSelectPanel 
	extends Composite
{
	
	private MultiSelectList multiSelectList = null;
	
	private HorizontalPanel hp 	= null;
	private PopupPanel popup 	= null;
	private boolean autoHide 	= true;
	
	private String name = null;
	private String selectString = " search all";
	
	
	
	public MultiSelectPanel( String name )
	{
		this.name = name;
		
		hp = new HorizontalPanel();
		initWidget( hp );
		
		initPanel();
	}
	
	
	public void setAutoHide( boolean hide ) {
		this.autoHide = hide;
	}
	
	
	private void initPanel()
	{
		HyperlinkLabel nameLabel = new HyperlinkLabel( name + ":", new SelectListener() );
		nameLabel.setWordWrap( false );
		
		hp.add( nameLabel );
		hp.add( new Label( selectString ) );
	}
	
	
	
	public void populateSelectedIDs( int[] selectedIDs )
	{
    	MultiSelectList multiSelectList = getMultiSelectList();
    	
    	Iterator<MultiSelectItem> i = multiSelectList.iterator();
    	while ( i.hasNext() )
    	{
    		MultiSelectItem item = i.next();
    		item.setSelected( false );
    		int itemID = item.getID();
    		
    		if ( selectedIDs != null )
    		{
	    		for ( int x=0; x < selectedIDs.length; x++ )
	    		{
	    			int id = selectedIDs[ x ];
	    			if ( itemID == id )
	    			{
	    				item.setSelected( true );
	    				break;
	    			}
	    		}
    		}
    	}
	    
	    updateDisplay();
	}
	
	
	public void setMultiSelectList( List<? extends MultiSelectItem> list )
	{
		this.multiSelectList = new MultiSelectList( list );
	}
	
	
	public MultiSelectList getMultiSelectList()
	{
		return multiSelectList;
	}
	
	
	public void updateDisplay()
	{
		if ( ( popup != null ) && popup.isVisible() )
			popup.hide();
		
		if ( multiSelectList.getSelected().size() == 0 )
		{
			hp.clear();
			initPanel();
		}
		else
		{
			hp.remove( 1 );
			hp.add( getSelectedNamesDisplay() );
		}
	}
	
	
	public Widget getSelectedNamesDisplay() 
	{
		FlowPanel fp = new FlowPanel();
		
		Iterator<MultiSelectItem> i = multiSelectList.getSelected().iterator();
		while ( i.hasNext() )
		{
			MultiSelectItem item = i.next();
			
			if ( fp.getWidgetCount() > 0 )
				fp.add( new Label( ", " ) );
			else
				fp.add( new Label( " " ) );
			
			Label name = null;
			if ( item.getShortName() != null ) 
			{
				name = new Label( item.getShortName() );
				name.setTitle( item.getName() );
			}
			else
				name = new Label( item.getName() );
			
			name.setStylePrimaryName( "boldText" );
			
			fp.add( name );
		}
		return fp;
	}
	
	
	private class SelectListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{
			VerticalPanel vp = new VerticalPanel();
			vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
			
			if ( multiSelectList != null )
			{
				ClearListener clearListener = new ClearListener();
				
				int size = multiSelectList.size();
				if ( size % 2 != 0 ) size++;
				int numRows = size / 2;
				
				Grid grid = new Grid( numRows, 7 ); // 4 );
				grid.setStyleName( "multiSelectGrid" );
				
				// spacer between column sets
				grid.setWidget( 0, 4, new HTML( "&nbsp;&nbsp;&nbsp;" ) );
				
				int row = 0;
				int col = 0;
				int colReset = 2;
				
				Iterator<MultiSelectItem> i = multiSelectList.iterator();
				for ( int x=1; i.hasNext(); x++ )
				{
					MultiSelectItem item = i.next();
					
					CheckBox checkBox = new CheckBox();
					if ( item.isSelected() ) checkBox.setValue( true );
					checkBox.addClickHandler( new SelectItemListener( checkBox, item ) );
					clearListener.addCheckBox( checkBox );
					
					String name = item.getName();
					if ( item.getShortName() != null )
						name += " (" + item.getShortName() + ")";
					
					grid.setWidget( row, col++, checkBox );
					
					String iconURL = item.getIconURL();
					if ( iconURL != null )
						grid.setWidget( row, col++, 
										new HyperlinkImage( iconURL,
											new IconSelectListener( checkBox, item ) ) );
					else
						col++;
					
					Label nameLabel = new Label( name );
					nameLabel.setWordWrap( false );
					grid.setWidget( row, col++, nameLabel );
					
					row++;
					if ( col > colReset ) 	col = col - 3;
					
					if ( row >= numRows ) 	
					{
						row = 0;
						col = 4;
						colReset = 5;
					}
				}
				
				vp.add( new HTML( "<b>" + name + "</b>" ) );
				//vp.add( new HTML( "<hr>" ) );
				vp.add( grid );
				
				HorizontalPanel hp = new HorizontalPanel();
				hp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
				hp.setWidth( "50%" );
				hp.add( new HyperlinkLabel( "Save", new SaveListener() ) );
				hp.add( new HyperlinkLabel( "Clear", clearListener ) );
				
				vp.add( hp );
			}
			else
			{
				vp.add( new Label( "Data not populated" ) );
				vp.add( new Label( "Please wait or try reloading page" ) );
				vp.add( new HyperlinkLabel( "Close", new CloseListener() ) );
			}
			
			vp.setStyleName( "multiSelectVerticalPanel" );
			
			Widget sender = (Widget) event.getSource();
			
			popup = new PopupPanel( autoHide );
			popup.setAnimationEnabled( true );
			popup.add( new PopupDecoratorPanel( vp ) );
			popup.show();
			popup.setPopupPosition( sender.getAbsoluteLeft(), sender.getAbsoluteTop() + sender.getOffsetHeight() );
			//popup.setStyleName( "popup" );
		}
	}
	
	
	private class CloseListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			popup.hide();
		}
	}
	
	
	private class SaveListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			updateDisplay();
		}
	}
	
	
	private class ClearListener implements ClickHandler
	{
		private ArrayList<CheckBox> checkBoxList = new ArrayList<CheckBox>();
		
		public void addCheckBox( CheckBox checkBox ) {
			checkBoxList.add( checkBox );
		}
		
		public void onClick( ClickEvent event ) 
		{
			Iterator<MultiSelectItem> mi = multiSelectList.iterator();
			while ( mi.hasNext() ) {
				mi.next().setSelected( false );
			}
			
			Iterator<CheckBox> ci = checkBoxList.iterator();
			while ( ci.hasNext() ) {
				ci.next().setValue( false );
			}
		}
	}
	
	
	private class SelectItemListener
		implements ClickHandler
	{
		private CheckBox checkBox = null;
		private MultiSelectItem item = null;
		
		public SelectItemListener( CheckBox cb, MultiSelectItem msi )
		{
			this.checkBox = cb;
			this.item = msi;
		}
		
		public void onClick( ClickEvent event )
		{
			item.setSelected( checkBox.getValue() );
		}
	}
	
	
	private class IconSelectListener
		implements ClickHandler
	{
		private CheckBox checkBox = null;
		private MultiSelectItem item = null;
		
		public IconSelectListener( CheckBox cb, MultiSelectItem msi )
		{
			this.checkBox = cb;
			this.item = msi;
		}
		
		public void onClick( ClickEvent event )
		{
			checkBox.setValue( ! checkBox.getValue() );
			item.setSelected( ! item.isSelected() );
		}
	}

}
