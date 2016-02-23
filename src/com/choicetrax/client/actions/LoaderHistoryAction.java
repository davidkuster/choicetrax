package com.choicetrax.client.actions;

import java.util.ArrayList;
import java.util.Iterator;


public abstract class LoaderHistoryAction 
	implements LoaderAction 
{

	public static final String DELIMITER			= ")";
	protected static final String ID_DELIMITER		= "+";
	
	public static final String PREFIX_SORT_BY		= "sort(";
	public static final String PREFIX_ORDER 		= "order(";
	
	private String sortBy = null;
	private String sortOrder = null;
	
	
	private int requestedPageNum = 1;  // default to page 1
	
	
	public LoaderHistoryAction() {
		super();
	}
	
	
	public LoaderHistoryAction( String actionString ) {
		super();
		
		if ( actionString == null ) return;
		
		sortBy = readEncodedValue( actionString, PREFIX_SORT_BY );
		sortOrder = readEncodedValue( actionString, PREFIX_ORDER );
	}
	
	
	
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy( String sort ) {
		this.sortBy = sort;
	}
	
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder( String order ) {
		this.sortOrder = order;
	}
	
	
	protected String getSortActionString() 
	{
		String actionString = "";
		
		if ( sortBy != null )
			actionString += PREFIX_SORT_BY + sortBy + DELIMITER;
		
		if ( sortOrder != null )
			actionString += PREFIX_ORDER + sortOrder + DELIMITER;
		
		return actionString;
	}
	
	
	public abstract String getActionString();
	
	
	public abstract void setUserID( int userID );
	
	
	
	public int getRequestedPage() {
		return this.requestedPageNum;
	}
	public void setRequestedPage( int requestedPage ) {
		this.requestedPageNum = requestedPage;
	}
	
	
	protected String readEncodedValue( String encodedString, String prefix )
    {
    	String value = null;
    	
    	int index = encodedString.indexOf( prefix );
		if ( index != -1 ) {
			index += prefix.length();
			int endIndex = encodedString.indexOf( DELIMITER, index );
			value = encodedString.substring( index, endIndex );
		}
		
		return value;
    }
	
	
	
	protected int[] createIntArray( String delimitedString )
	{
		if ( delimitedString == null ) return null;
		
		ArrayList<String> list = new ArrayList<String>();
		
		while ( true ) 
		{
			int commaIndex = delimitedString.indexOf( ID_DELIMITER );
			if ( commaIndex != -1 )  {
				String value = delimitedString.substring( 0, commaIndex );
				list.add( value );
				delimitedString = delimitedString.substring( commaIndex + 1 );
			}
			else {
				list.add( delimitedString );
				break;
			}
		}
				
		int[] array = new int[ list.size() ];
		
		Iterator<String> i = list.iterator();
		for ( int x=0; i.hasNext(); x++ ) {
			array[ x ] = Integer.parseInt( i.next() );
		}
		
		return array;
	}

}
