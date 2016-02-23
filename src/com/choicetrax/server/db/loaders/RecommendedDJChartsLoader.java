package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.RecommendedDJChartsAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class RecommendedDJChartsLoader
	extends DJChartsLoader
{
	
	
	public RecommendedDJChartsLoader() {
		super();
	}
	
	
	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		RecommendedDJChartsAction requestObj = (RecommendedDJChartsAction) action;
		
		int userID = requestObj.getUserID();
		
		// get all trackIDs associate to the input userID
		UserTrackIDsLoader trackIDsLoader = new UserTrackIDsLoader( userID );
		trackIDsLoader.setMinTrackRating( 3 );
		int[] userTrackIDs = trackIDsLoader.getTrackIDs();
		
		// initialize cache
		int[] chartIDs = null;
		if ( userTrackIDs.length > 0 )
			chartIDs = getChartIDs( userTrackIDs );
		else
			chartIDs = new int[] { };
		
		DJChartsCache cache = new DJChartsCache( chartIDs, Constants.CACHE_CHARTS_DJ_RECOMMENDED );
		
		DJCharts firstPage = loadCharts( cache.getIDsForPage( 1 ) );
		cache.addCharts( firstPage, 1 );
		
		if ( cache.getNumPages() >= 2 ) {
			DJCharts secondPage = loadCharts( cache.getIDsForPage( 2 ) );
			cache.addCharts( secondPage, 2 );
		}
		
        return cache;
	}
	
	
	private int[] getChartIDs( int[] trackIDs )
		throws ChoicetraxException
	{
		String methodName = "getChartIDs()";
		
		LinkedList<Integer> idList = new LinkedList<Integer>();
		
		// SQL to order by highest recommended chart
		/*String sql = "select distinct ChartID, count(ChartID) "
					+ "from ChartsDJsTracks "
					+ "where TrackID in ( " 
					+ 	ServerConstants.convertIntArrayToString( trackIDs )+ " ) "
					+ "group by ChartID "
					+ "having count(ChartID) > 1 "
					+ "order by count(ChartID) desc ";*/

		// SQL to order by most recent chart date
		String sql = "select c.ChartID "
					+ "from ChartsDJs c, "
					+	"( select distinct ChartID, count(ChartID) "
					+ 		"from ChartsDJsTracks "
					+ 		"where TrackID in ( " 
					+ 			ServerConstants.convertIntArrayToString( trackIDs )+ " ) "
					+ 		"group by ChartID "
					+		"having count(ChartID) > 1 ) as CDJT "
					+ "where c.ChartID = CDJT.ChartID "
					+ "order by c.ChartDate desc";
		
		DBResource dbHandle = null;
		
		try 
		{
			dbHandle = ResourceManager.getDBConnection();
			
			ResultSet rs = dbHandle.executeQuery( sql );
			while ( rs.next() )
			{
				int chartID = rs.getInt( "ChartID" );
				idList.add( new Integer( chartID ) );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte ) {
			throw cte;
		}
		catch ( Throwable t ) {
			throw new ChoicetraxException( "Error loading chartIDs: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally {
			if ( dbHandle != null )
				dbHandle.close();
		}
		
		// isn't there a more efficient way of doing this without having to iterate
		// over the entire list again ???
		
		int[] ids = new int[ idList.size() ];
		
		Iterator<Integer> i = idList.iterator();
		for ( int x=0; i.hasNext(); x++ )
		{
			ids[ x ] = i.next().intValue();
		}
		
		return ids;
	}
	

	
}
