package com.choicetrax.server.db.loaders.email;

import org.apache.log4j.Logger;

import com.choicetrax.client.actions.loaderactions.RecommendedDJChartsAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.loaders.RecommendedDJChartsLoader;


public class EmailRecommendedChartsLoader
{
	
	private Logger logger = Logger.getLogger( EmailRecommendedChartsLoader.class );
	
	private int userID = 0;
	
	private int totalRecommendedCharts = 0;
	
	
	public EmailRecommendedChartsLoader( int userID ) {
		super();
		this.userID = userID;
	}
	
		
	
	public DJCharts getDJCharts( int numChartsToLoad ) 
	{
		try
		{
			RecommendedDJChartsAction action = new RecommendedDJChartsAction();
			action.setUserID( userID );
			
			RecommendedDJChartsLoader loader = new RecommendedDJChartsLoader();
			LoaderResponse response = loader.loadData( action );
			DJChartsCache cache = (DJChartsCache) response;
			
			this.totalRecommendedCharts = cache.size();
			
			return cache.getChartsForPage( 1 );
		}
		catch ( ChoicetraxException ce ) 
		{
			logger.error( "Error loading recommended charts for userID [" + userID + "]", ce );
			return new DJCharts();
		}
	}
	
	
	public int getTotalRecommendedChartsNum() {
		return this.totalRecommendedCharts;
	}

}
