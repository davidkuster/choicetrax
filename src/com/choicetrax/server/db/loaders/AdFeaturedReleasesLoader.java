package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.config.AdFeaturedRelease;
import com.choicetrax.client.data.config.AdFeaturedReleases;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class AdFeaturedReleasesLoader 
	extends BaseReleasesLoader
{
	
	private static Logger logger = Logger.getLogger( AdFeaturedReleasesLoader.class );
	
	
	private LinkedHashMap<String,AdFeaturedRelease> genreTrackIdMap = 
			new LinkedHashMap<String,AdFeaturedRelease>();
	
	
	// maybe what i should do with this is essentially populate the AdFeaturedReleases
	// object but only load full ReleaseDetail data for the requested genre to
	// initially show.  after that this can work in a caching fashion - if the user
	// wants to view featured releases for a genre that is not populated, send the
	// requested track IDs to the server and just load those.
	
	// should i think about re-using a ReleasesCache object to easily indicate
	// whether there is a cache miss or not?  or maybe some kind of similar object,
	// specific to featured releases...
	
	// for now at least, just load all the featured releases.  worry about caching
	// & whatnot a little later on...
	
	public AdFeaturedReleases loadFeaturedReleasesData()
		throws ChoicetraxException
	{
		AdFeaturedReleases featuredReleases = new AdFeaturedReleases();
		
		setOrdering( false );
		
		populateGenreTrackIdMap();
		
		int[] trackIDs = createTrackIdArray();
		
		Releases releases = loadReleases( trackIDs );
		List<ReleaseDetail> releasesList = releases.getReleasesList();
		if ( releasesList != null ) 
		{
			Hashtable<Integer,ReleaseDetail> rdHash = new Hashtable<Integer,ReleaseDetail>();
			Iterator<ReleaseDetail> i = releasesList.iterator();
			while ( i.hasNext() ) {
				ReleaseDetail rd = i.next();
				rdHash.put( new Integer( rd.getTrackID() ), rd );
			}
			
			Iterator<AdFeaturedRelease> f = genreTrackIdMap.values().iterator();
			while ( f.hasNext() ) 
			{
				AdFeaturedRelease featuredRelease = f.next();
				int genreID = featuredRelease.getTargetGenre().getGenreID();
				Integer trackID = featuredRelease.getReleaseDetail().getTrackID();
				ReleaseDetail rd = rdHash.get( trackID );
				featuredRelease.setReleaseDetail( rd );
				
				if ( rd != null )
					featuredReleases.addRelease( genreID, featuredRelease );
				else
					logger.error( "ReleaseDetail not found for "
								+ "trackID [" + trackID + "], "
								+ "genreID [" + genreID + "], "
								+ "probably no rows in PartnerInventory table" );
			}
		}
		
		return featuredReleases;		
	}
	
	
	protected String createTrackIdSQL() {	
		return null;
	}
	
	
	private void populateGenreTrackIdMap()
		throws ChoicetraxException
	{
		String methodName = "()";
		
		String sql = "select f.TargetGenreID, t.TrackID "
					+ "from AdsFeaturedReleases f, Tracks t, PartnerInventory pi "
					+ "where f.AlbumID = t.AlbumID "
					+	"and t.TrackID = pi.TrackID "
					+	"and f.FeaturedEndDate >= sysdate() "
					+ "group by f.FeaturedID "
					+ "order by rand() " ;
		
		DBResource dbHandle = null;
    	
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		while ( rs.next() )
    		{
				int genreID = rs.getInt( "TargetGenreID" );
				int trackID = rs.getInt( "TrackID" );
				
				String key = genreID + "+" + trackID;
				
				Genre genre = new Genre();
				genre.setGenreID( genreID );
				
				ReleaseDetail rd = new ReleaseDetail();
				rd.setTrackID( trackID );
				
				AdFeaturedRelease featuredRelease = new AdFeaturedRelease();
				featuredRelease.setTargetGenre( genre );
				featuredRelease.setReleaseDetail( rd );
				
				genreTrackIdMap.put( key, featuredRelease );
    		}
    		rs.close();
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing featured releases load "
    										+ t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}
	}
	
	
	private int[] createTrackIdArray()
	{
		int size = genreTrackIdMap.values().size();
		int[] trackIDs = new int[ size ];
		
		Iterator<AdFeaturedRelease> i = genreTrackIdMap.values().iterator();
		for ( int x=0; i.hasNext(); x++ ) 
		{
			AdFeaturedRelease featuredRelease = i.next();
			ReleaseDetail rd = featuredRelease.getReleaseDetail();
			
			trackIDs[ x ] = rd.getTrackID();
		}
		
		return trackIDs;
	}

}
