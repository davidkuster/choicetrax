package com.choicetrax.server.db.loaders.sphinx;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.util.jdbc.ResourceManager;


public class SphinxDJChartsLoader extends SphinxLoader
{

	
	public SphinxDJChartsLoader( String sortType, String sortOrder ) 
		throws ChoicetraxException
	{
		super();
		
		this.setIndex( ResourceManager.getSphinxIndexDJCharts() );
		
		if ( Constants.SORT_BY_DEFAULT.equalsIgnoreCase( sortType ) )
			this.setSortMode( "ChartDate DESC" );
		else 
			throw new ChoicetraxException( "SphinxDJChartsLoader init - unknown sort type: "
											+ sortType );
	}
	
	
}
