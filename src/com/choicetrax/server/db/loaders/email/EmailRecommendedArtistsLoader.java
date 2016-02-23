package com.choicetrax.server.db.loaders.email;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.choicetrax.client.data.Releases;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.loaders.BaseReleasesLoader;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class EmailRecommendedArtistsLoader extends BaseReleasesLoader
{
	
	private Logger logger = Logger.getLogger( EmailRecommendedArtistsLoader.class );
	
	private int userID = 0;
	private int numReleasesToLoad = 0;
	private String artistIDs = null;

	
	
	public EmailRecommendedArtistsLoader( int userID ) {
		super();
		this.userID = userID;
	}
	
	
	/*
	 * select st.TrackingType, st.TrackingID, sum(st.numsearches), a.ArtistName 
	 * from SearchTracking st, Artists a
	   where st.userid = 1 and st.trackingtype = 'ARTIST' 
			and st.trackingid = a.artistid and a.legitname = 'Y'
	   group by st.trackingid order by sum(st.numsearches) desc limit 50;

	   select t.albumid, a.artistname, t.trackname, t.mixname, l.labelname, t.loaddate, 
	   t.releasedate
	   from Tracks t, Artists a, Labels l
	   where t.artistid = a.artistid and t.labelid = l.labelid
  			and ( t.artistid in ( 168, 5294, 1233, 9648, 2844 )
  				or t.remixerartistid in ( 168, 5294, 22007, 641, 514 ) )
	   group by t.albumid
	   order by t.loaddate desc limit 50;
	 */
	public Releases getReleases( int numReleasesToLoad ) 
	{
		this.numReleasesToLoad = numReleasesToLoad;
		
		try
		{
			this.artistIDs = getArtistIDs();
			
			int[] trackIDs = getTrackIDs();
			Releases releases = loadReleases( trackIDs );
			
			return releases;
		}
		catch ( ChoicetraxException ce ) 
		{
			logger.error( "Error loading recommended artists for userID [" + userID + "]", ce );
			return new Releases();
		}
	}
	
	
	private String getArtistIDs()
		throws ChoicetraxException
	{
		String methodName = "getArtistIDs()";
			
		String sql = "select st.TrackingID " 
					+ "from SearchTracking st, Artists a "
					+ "where st.userid = " + this.userID + " "
					+	"and st.trackingtype = 'ARTIST' " 
					+	"and st.trackingid = a.artistid "
					+	"and a.legitname = 'Y' "
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
				
				int artistID = rs.getInt( "TrackingID" );
				
				sb.append( artistID );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte ) {
			throw cte;
		}
		catch ( Throwable t ) {
			throw new ChoicetraxException( "Error loading artistIDs: " + t,
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
				+ "where ( t.artistid in ( " + artistIDs + " ) "
	  			+	"or t.remixerartistid in ( " + artistIDs + " ) ) "
	  			+ "group by t.albumid "
	  			+ "order by t.loaddate desc limit " + numReleasesToLoad;
	}

}
