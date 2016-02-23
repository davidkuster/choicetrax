package com.choicetrax.server.db.loaders;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.ViewDJChartAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.DJChartDetail;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;


public class DJChartTracksLoader
	extends BaseReleasesLoader
	implements DataLoader
{
	
	private int chartID = -1;
	
	
	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		ViewDJChartAction requestObj = (ViewDJChartAction) action;
		chartID = requestObj.getChartID();
		
		setUserID( requestObj.getUserID() );
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( requestObj ) );
		
		DJChartsLoader chartLoader = new DJChartsLoader();
		DJCharts charts = chartLoader.loadCharts( new int[] { chartID } );
		
		int[] trackIDs = getTrackIDs();
		
		ReleasesCache tracks = new ReleasesCache( Constants.CACHE_RELEASES_DJ_TRACKS );
		
		Releases firstPage = loadReleases( trackIDs );
		tracks.addReleases( firstPage, 1 );
		
		DJChartDetail chart = charts.getChartsList().get( 0 );
		chart.setChartTracks( tracks );
		
		DJChartsCache cache = new DJChartsCache( Constants.CACHE_CHARTS_DJ );
		cache.addChart( chart );
		
        return cache;
	}
	
	
	protected String createTrackIdSQL()
	{
		return "select TrackID from ChartsDJsTracks "
				+ "where ChartID = " + chartID + " "
				+ "order by TrackChartNumber";
	}

}