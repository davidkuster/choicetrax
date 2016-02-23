package com.choicetrax.server.db.loaders.email;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.choicetrax.client.data.Releases;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.loaders.BaseReleasesLoader;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class EmailRecommendedLabelsLoader extends BaseReleasesLoader
{
	
	private Logger logger = Logger.getLogger( EmailRecommendedLabelsLoader.class );
	
	private int userID = 0;
	private int numReleasesToLoad = 0;
	private String labelIDs = null;
	
	
	
	public EmailRecommendedLabelsLoader( int userID ) {
		super();
		this.userID = userID;
	}
	
	/*
	select st.TrackingType, st.TrackingID, sum(st.numsearches), l.LabelName 
	from SearchTracking st, Labels l
	where st.userid = 1 and st.trackingtype = 'LABEL' 
		and st.trackingid = l.labelid and l.legitname = 'Y'
	group by st.trackingid 
	order by sum(st.numsearches) desc limit 50;

	select a.artistname, t.trackname, t.mixname, l.labelname, t.loaddate, t.releasedate
	from Tracks t, Artists a, Labels l
	where t.artistid = a.artistid and t.labelid = l.labelid
	  and l.labelid in ( 166, 10, 8576, 673, 153, 5877, 20, 3349, 90, 80, 86, 7660, 33, 1598 )
	group by t.albumid
	order by t.loaddate desc limit 50;
	 */
	public Releases getReleases( int numReleasesToLoad ) 
	{
		this.numReleasesToLoad = numReleasesToLoad;
		
		try
		{
			this.labelIDs = getLabelIDs();
			
			int[] trackIDs = getTrackIDs();
			Releases releases = loadReleases( trackIDs );
			
			return releases;
		}
		catch ( ChoicetraxException ce ) 
		{
			logger.error( "Error loading recommended labels for userID [" + userID + "]", ce );
			return new Releases();
		}
	}
	
	
	private String getLabelIDs()
		throws ChoicetraxException
	{
		String methodName = "getLabelIDs()";
			
		String sql = "select st.TrackingID " 
					+ "from SearchTracking st, Labels l "
					+ "where st.userid = " + this.userID + " "
					+	"and st.trackingtype = 'LABEL' " 
					+	"and st.trackingid = l.LabelID "
					+	"and l.legitname = 'Y' "
					+ "group by st.trackingid "
					+ "order by sum(st.numsearches) desc limit 50";
		
		DBResource dbHandle = null;
		
		StringBuffer sb = new StringBuffer();
		
		try 
		{
			dbHandle = ResourceManager.getDBConnection();
			
			ResultSet rs = dbHandle.executeQuery( sql );
			while ( rs.next() )
			{
				if ( sb.length() > 0 ) sb.append( ", " );
				
				int labelID = rs.getInt( "TrackingID" );
				
				sb.append( labelID );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte ) {
			throw cte;
		}
		catch ( Throwable t ) {
			throw new ChoicetraxException( "Error loading labelIDs: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally {
			if ( dbHandle != null )
				dbHandle.close();
		}
		
		return sb.toString();
	}


	@Override
	protected String createTrackIdSQL() 
	{
		return "select t.TrackID "
				+ "from Tracks t "
				+ "where t.LabelID in ( " + labelIDs + " ) "
	  			+ "group by t.albumid "
	  			+ "order by t.loaddate desc limit " + numReleasesToLoad;
	}

}
