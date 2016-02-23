package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.LinkedList;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.RecommendedTracksAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class RecommendedTracksLoader
	extends BaseReleasesLoader
	implements DataLoader 
{
	
	private RecommendedTracksAction requestObj = null;
	
	private String sortBy 		= Constants.SORT_BY_DEFAULT;
	private String sortOrder	= Constants.SORT_ORDER_DEFAULT;
	
	private int[] chartIDs = null;
	private int[] userTrackIDs = null;
	private int[] excludedTrackIDs = null;
	
	
	public RecommendedTracksLoader() {
		super();
	}
	
	
	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		this.requestObj = (RecommendedTracksAction) action;
		if ( requestObj.getSortBy() != null ) sortBy = requestObj.getSortBy();
		if ( requestObj.getSortOrder() != null ) sortOrder = requestObj.getSortOrder();
		
		setUserID( requestObj.getUserID() );	
		setTracking( true, ServerConstants.readUserIdOrIpForMySQL( requestObj ) );
		
		// get all trackIDs associate to the input userID
		UserTrackIDsLoader trackIDsLoader = new UserTrackIDsLoader( this.userID );
		trackIDsLoader.setMinTrackRating( 3 );
		userTrackIDs = trackIDsLoader.getTrackIDs();
		
		// get all excluded tracksIDs
		UserTrackIDsExcludedLoader excludedLoader = new UserTrackIDsExcludedLoader( this.userID );
		excludedLoader.setMaxTrackRating( 3 ); // load tracks rated < 3
		excludedTrackIDs = excludedLoader.getTrackIDs();
		
		// get chartIDs associated to the resulting trackIDs
		chartIDs = getChartIDs( userTrackIDs );
		
		// initialize cache
		int[] trackIDs = null;
		
		if ( chartIDs == null )
			trackIDs = new int[] { };
		else
			trackIDs = getTrackIDs();
		
		int requestedPage = requestObj.getRequestedPage();
		
		ReleasesCache cache = new ReleasesCache( trackIDs, Constants.CACHE_RELEASES_RECOMMENDED );
		
		Releases firstPage = loadReleases( cache.getIDsForPage( requestedPage ) );
		cache.addReleases( firstPage, requestedPage );
		
		int secondPageNum = requestedPage + 1;
		if ( cache.getNumPages() >= secondPageNum ) {
			Releases secondPage = loadReleases( cache.getIDsForPage( secondPageNum ) );
			cache.addReleases( secondPage, secondPageNum );
		}
		
        return cache;
	}
	

	// TODO: think about having this processing happen offline?
	// could potentially generate recommendations and then store them in 
	// UserRecommendations table, to coincide with UserRecommendationsRemoved.
	// if i did that, drop the UserRecommendationsRemoved table and just have
	// a DateRemoved field on the UserRecommendations table, just like how
	// Wishlist, VC, PH, etc work.
	protected String createTrackIdSQL() 
	{
		String sql = null;
		
		if ( Constants.SORT_BY_DEFAULT.equals( sortBy ) ) 
		{
			sql = "select distinct ct.TrackID " //, count(TrackID) "
				+ "from ChartsDJsTracks ct "
				+ "where ct.ChartID in ( "
				+		ServerConstants.convertIntArrayToString( chartIDs ) + " ) "
				+	createNotInClause()
				+	"and ct.TrackID in ( select distinct TrackID from PartnerInventory ) "
				+ "group by ct.TrackID "
				+ "having count(ct.TrackID) > 1 "
				+ "order by count(ct.TrackID) desc";
		}
		else if ( Constants.RELEASES_SORT_BY_DATE.equals( sortBy ) ) 
		{
			sql ="select distinct ct.TrackID "
				+ "from ChartsDJsTracks ct, Tracks t "
				+ "where ct.TrackID = t.TrackID "
				+	"and ct.ChartID in ( "
				+		ServerConstants.convertIntArrayToString( chartIDs ) + " ) "
				+	createNotInClause()
				+	"and ct.TrackID in ( select distinct TrackID from PartnerInventory ) "
				+ "group by ct.TrackID "
				+ "having count(ct.TrackID) > 1 "
				+ "order by t.LoadDate ";
			
			// avoid possible sql injection by not just appending client input into sql statement
			if ( Constants.SORT_ORDER_DESCENDING.equals( sortOrder ) )
				sql += "desc";
		}
		
		return sql;
	}
	
	
	private String createNotInClause()
	{
		StringBuffer sql = new StringBuffer();
		
		int trackIDsLen = userTrackIDs.length;
		int excludedLen = excludedTrackIDs.length;
		if ( trackIDsLen > 0 || excludedLen > 0 )
		{
			sql.append( "and ct.TrackID not in ( " );
			
			if ( trackIDsLen > 0 ) {
				sql.append( ServerConstants.convertIntArrayToString( userTrackIDs ) );
				
				if ( excludedLen > 0 ) sql.append( ", " );
			}
			
			if ( excludedLen > 0 )
				sql.append( ServerConstants.convertIntArrayToString( excludedTrackIDs ) );
			
			sql.append( " ) " );
		}
		
		return sql.toString();
	}
	
	
	private int[] getChartIDs( int[] trackIDs )
		throws ChoicetraxException
	{
		String methodName = "getChartIDs()";

		if ( trackIDs == null || trackIDs.length == 0 )
			return null;
		
		LinkedList<Integer> idList = new LinkedList<Integer>();
		
		String sql = "select distinct ChartID "
					+ "from ChartsDJsTracks "
					+ "where TrackID in ( " 
					+ 	ServerConstants.convertIntArrayToString( trackIDs )+ " ) ";
		
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
