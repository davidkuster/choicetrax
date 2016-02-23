package com.choicetrax.client.data;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class MultiSelectList 
	//extends ArrayList<MultiSelectItem>
{
	
	private List<MultiSelectItem> selectList = new ArrayList<MultiSelectItem>();
	
	
	public MultiSelectList() { 
		super();
	}
	
	/**
	 *  List<com.choicetrax.client.data.MultiSelectItem>
	 */
	public MultiSelectList( List<? extends MultiSelectItem> list )
	{
		super();
		
		// perform deep copy on input list
		
		Iterator<? extends MultiSelectItem> i = list.iterator();
		while ( i.hasNext() ) 
		{
			MultiSelectItem inputItem = i.next();
			selectList.add( inputItem.clone() );
		}
	}
	
	
	public List<MultiSelectItem> getSelected()
	{
		List<MultiSelectItem> selectedList = new ArrayList<MultiSelectItem>();
		
		Iterator<MultiSelectItem> i = selectList.iterator();
		while ( i.hasNext() )
		{
			MultiSelectItem item = i.next();
			
			if ( item.isSelected() )
				selectedList.add( item );
		}
		
		return selectedList;
	}
	
	
	public List<String> getSelectedNames()
	{
		List<String> selectedNames = new ArrayList<String>();
		
		Iterator<MultiSelectItem> i = selectList.iterator();
		while ( i.hasNext() )
		{
			MultiSelectItem item = i.next();
			
			if ( item.isSelected() )
				selectedNames.add( item.getName() );
		}
		
		return selectedNames;
	}
	
	
	public int[] getSelectedIDs()
	{
		List<MultiSelectItem> selectedList = getSelected();
		
		int[] selectedIDs = new int[ selectedList.size() ];
		
		Iterator<MultiSelectItem> i = selectedList.iterator();
		for ( int x=0; i.hasNext(); x++ )
		{
			MultiSelectItem item = i.next();
			
			selectedIDs[ x ] = item.getID();
		}
		
		return selectedIDs;
	}
	
	
	public Iterator<MultiSelectItem> iterator() {
		return selectList.iterator();
	}
	
	public int size() {
		return selectList.size();
	}

}
