package com.choicetrax.server.db.loaders;

import java.util.*;
import java.sql.*;

import com.choicetrax.client.data.*;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.data.tracking.TrackingQueues;
import com.choicetrax.server.data.tracking.TrackingSet;
import com.choicetrax.server.util.jdbc.*;


public abstract class BaseReleasesLoader
{
	
	// userID to load associated track ratings
	protected int userID = 0;
	
	// indicates whether BaseReleasesLoader should add its own
	// ordering to the results
	private boolean ordering = true;
	
	// indicates whether BaseReleasesLoader should perform
	// search tracking on the results found
	private boolean tracking = false;
	private String userTrackingID = null;
	
	// search tracking
	private TrackingSet artistTracking	= new TrackingSet();
	private TrackingSet labelTracking 	= new TrackingSet();
	private TrackingSet genreTracking 	= new TrackingSet();
	
	
	
	public BaseReleasesLoader() { }
	
	
	public void setUserID( int userID ) 
	{
		if ( userID > 0 )
			this.userID = userID;
	}
	
	
	protected void setOrdering( boolean ordering ) {
		this.ordering = ordering;
	}
	
	
	/**
	 * Parameters are boolean - to track or not to track, and the ID
	 * to associate to the tracking.  The ID can either be a userID 
	 * or the IP address of the client machine if the user is not logged in.
	 * 
	 * @param tracking
	 * @param trackingID
	 */
	protected void setTracking( boolean tracking, String userTrackingID ) {
		this.tracking = tracking;
		this.userTrackingID = userTrackingID;
	}
	
	
	abstract protected String createTrackIdSQL();
	
	
	protected int[] getTrackIDs()
		throws ChoicetraxException
	{
		String methodName = "getTrackIDs()";
		
		LinkedList<Integer> idList = new LinkedList<Integer>();
		
		DBResource dbHandle = null;
		
		try 
		{
			dbHandle = ResourceManager.getDBConnection();
			
			ResultSet rs = dbHandle.executeQuery( createTrackIdSQL() );
			while ( rs.next() )
			{
				int trackID = rs.getInt( "TrackID" );
				idList.add( new Integer( trackID ) );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte ) {
			throw cte;
		}
		catch ( Throwable t ) {
			throw new ChoicetraxException( "Error loading trackIDs: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally {
			if ( dbHandle != null )
				dbHandle.close();
		}
		
		// isn't there a more efficient way of doing this without having to iterate
		// over the entire list again ???
		
		int[] trackIDs = new int[ idList.size() ];
		
		Iterator<Integer> i = idList.iterator();
		for ( int x=0; i.hasNext(); x++ )
		{
			trackIDs[ x ] = i.next().intValue();
		}
		
		return trackIDs;
	}
	
	
	protected Releases loadReleases( int[] trackIDs ) 
		throws ChoicetraxException
	{
		LinkedHashMap<Integer,ReleaseDetail> releasesHash 
			= new LinkedHashMap<Integer,ReleaseDetail>();
		
		String methodName = "loadReleases()";

		Releases responseObj = new Releases();
		
		// if no trackIDs are passed in, skip this method
		if ( ( trackIDs == null ) || trackIDs.length == 0 ) 
			return responseObj;
		
		DBResource dbHandle = null;
		
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		String sql = createReleasesSQL( trackIDs );
    		if ( sql == null ) return responseObj;
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		while ( rs.next() )
    		{
    			int trackID = rs.getInt( "TrackID" );
    			Integer tID = new Integer( trackID );
    			int partnerID = rs.getInt( "PartnerID" ) ;
    			
    			ReleaseDetail rd = releasesHash.get( tID );
    			
    			if ( rd == null )
    			{
    				rd = new ReleaseDetail();
    				
    				Artist artist = new Artist();
    				artist.setArtistID( 	rs.getInt(		"ArtistID"				) );
    				artist.setArtistName(	rs.getString(	"ArtistName"			) );
    				artist.setLegitName(  	rs.getString(  	"LegitArtistName"		) );
    				rd.setArtist( artist );
    				
    				Artist remixer = new Artist();
    				remixer.setArtistID(	rs.getInt( 		"remixerArtistID"		) );
    				remixer.setArtistName(	rs.getString(	"remixerName"			) );
    				remixer.setLegitName( 	rs.getString(	"LegitRemixerName"		) );
    				rd.setRemixer( remixer );
    				
    				Artist vocalist = new Artist();
    				vocalist.setArtistID(	rs.getInt(		"vocalistArtistID"		) );
    				vocalist.setArtistName(	rs.getString(	"vocalistName"			) );
    				vocalist.setLegitName( 	rs.getString(	"LegitVocalistName"		) );
    				rd.setVocalist( vocalist );
    				
    				RecordLabel label = new RecordLabel();
    				label.setLabelID( 		rs.getInt( 		"LabelID"				) );
    				label.setLabelName(		rs.getString(	"LabelName"				) );
    				rd.setLabel( label );
    				
    				rd.setTrackID( 			trackID 								);
    				rd.setTrackName( 		rs.getString( 	"TrackName" 			) );
    				rd.setMixName( 			rs.getString( 	"MixName" 				) );
    				rd.setAlbumName(		rs.getString(	"AlbumName"			) );
    				rd.setLabelCatalogNum( 	rs.getString( 	"LabelCatalogNumber" 	) );
    				rd.setTimeLength(		rs.getString( 	"TrackLength" 			) );
    				
    				rd.setTrackRating( 		rs.getFloat( 	"TrackRatingOverall" 	) );
    				rd.setNumTrackRatings(	rs.getInt(		"TrackNumRatings"		) );
    				
    				rd.setBpm( 				rs.getString(	"BPM"					) );
    				rd.setBpmConfidence( 	rs.getString( 	"BPMConfidence"			) );
    				
    				if ( userID > 0 )
    					rd.setUserRating(	rs.getInt(		"UserRating"			) );
    				
     				java.sql.Date releaseDate = rs.getDate( "ReleaseDate" );
    				if ( releaseDate != null )
    				{
	    				Calendar c = Calendar.getInstance();
	    				c.setTimeInMillis( releaseDate.getTime() );
	    				rd.setReleaseDate( c.getTime() );
    				}
    				
    				if ( tracking ) 
    				{
	    				// add IDs to tracking objects
	    				artistTracking.add( artist.getArtistID(), userTrackingID );
	    				labelTracking.add( label.getLabelID(), userTrackingID );
	    				
	    				if ( remixer.getArtistID() > 0 )
	    					artistTracking.add( remixer.getArtistID(), userTrackingID );
	    				if ( vocalist.getArtistID() > 0 )
	    					artistTracking.add( vocalist.getArtistID(), userTrackingID );
    				}
    			}
    			
    			LinkedList<PartnerInventory> piList = rd.getPartnerAvailability();
    			if ( piList == null ) piList = new LinkedList<PartnerInventory>();
    			
    			PartnerInventory pi = null;
    			if ( piList.size() == 0 )
    				pi = new PartnerInventory();
    			else
    			{
    				pi = (PartnerInventory) piList.get( piList.size() - 1 );
    				if ( pi.getPartnerID() != partnerID )
    					pi = new PartnerInventory();
    			}
    			
    			pi.setPartnerID( 				partnerID								);
    			pi.setPartnerTrackID( 			rs.getString( "PartnerTrackID" ) 		);
    			pi.setAudioPreviewURL(			rs.getString( "AudioPreviewURL" )		);
    			pi.setImageURL(					rs.getString( "ImageURL" )				);
    			pi.setTerritoryRestrictions(	rs.getString( "TerritoryRestrictions" )	);
    			pi.setReleaseID( 				rs.getInt( "ReleaseID" )				);
    			pi.setReleaseTrackID(			rs.getInt( "ReleaseTrackID" )			);
    			   			
    			piList.add( pi );
    			rd.setPartnerAvailability( piList );
    			
    			
    			LinkedList<Genre> genreList = rd.getGenres();
    			if ( genreList == null ) 
    			{
    				// values in TrackGenreIDs can be comma delimited list
    				String genreIDs = rs.getString( "TrackGenreIDs" );
    				
    				genreList = createGenreList( genreIDs );
    				rd.setGenres( genreList );
    			}

    			releasesHash.put( tID, rd );
    		}
    		rs.close();
    		    		
    		
    		// take data out of hashtable & put it in SearchResponse obj
    		Iterator<ReleaseDetail> i = releasesHash.values().iterator();
    		while ( i.hasNext() ) {
    			responseObj.addRelease( i.next() ); 
    		}
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing releases load: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}
    	
    	if ( tracking 
    		&& ( responseObj.size() > 0 ) ) 
    	{
   			TrackingQueues.add( ServerConstants.TRACKING_QUEUE_ARTIST, artistTracking );
   			TrackingQueues.add( ServerConstants.TRACKING_QUEUE_LABEL, labelTracking );
   			TrackingQueues.add( ServerConstants.TRACKING_QUEUE_GENRE, genreTracking );
    	}

		return responseObj;
	}
	
	
	/*
	 * assuming that the number of genre IDs and names will be 
	 * equal, and that they will be in matching order 
	 */
	private LinkedList<Genre> createGenreList( String genreIDs )
	{
		LinkedList<Genre> genreList = new LinkedList<Genre>();
		
		if ( genreIDs != null )
		{
			// TODO change this to use indexOf to increase performance
			// (instead of StringTokenizer)
			
			StringTokenizer ids = new StringTokenizer( genreIDs, "," );
			while ( ids.hasMoreTokens() )
			{
				int genreID = Integer.parseInt( ids.nextToken() );
				
				Genre genre = new Genre();
				genre.setGenreID( genreID );
				
				genreList.add( genre );
				
				if ( tracking ) {
					// add tracking
					genreTracking.add( genreID, userTrackingID );
				}
			}
		}
		
		return genreList;
	}
	
		
	/**
	 * creates general releases load SQL for input trackIDs
	 * 
	 * @param trackIDs
	 * @return
	 */
	private String createReleasesSQL( int[] trackIDs )
	{
		StringBuffer idList = new StringBuffer();
    	for ( int x=0; x < trackIDs.length; x++ )
    	{
    		int trackID = trackIDs[ x ];
    		if ( trackID > 0 )
    		{
    			if ( idList.length() > 0 ) idList.append( ", " );
    			idList.append( trackID );
    		}
    	}
    	 
    	// if no track IDs > 0, return null
		if ( idList.length() == 0 ) return null;
		
		StringBuffer sql = new StringBuffer();
		
		sql.append( "select t.TrackID, "
						+ "ar.ArtistID, "
						+ "ar.ArtistName, "
						+ "ar.LegitName as LegitArtistName, "
						+ "t.TrackName, "
						+ "t.MixName, "
						+ "remixer.ArtistID as remixerArtistID, "
						+ "remixer.ArtistName as remixerName, "
						+ "remixer.LegitName as LegitRemixerName, "
						+ "vocalist.ArtistID as vocalistArtistID, "
						+ "vocalist.ArtistName as vocalistName, "
						+ "vocalist.LegitName as LegitVocalistName, "
						+ "t.LabelID, "
						+ "l.LabelName, "
						+ "t.ReleaseDate, "
						+ "t.TrackGenreIDs, "
						+ "t.TrackLength, "
						+ "t.TrackRatingOverall, "
						+ "t.TrackNumRatings, "
						+ "t.AlbumID, "
						+ "t.BPM, "
						+ "t.BPMConfidence, "
						+ "ae.AlbumName, "
						+ "ae.LabelCatalogNumber, "
						+ "pi.PartnerID, "
						+ "pi.PartnerTrackID, "
						+ "pi.AudioPreviewURL, "
						+ "pi.ImageURL, "
						+ "pi.TerritoryRestrictions, " 
						+ "pi.ReleaseID, "
						+ "pi.ReleaseTrackID " );
		
		if ( userID > 0 )
			sql.append( ", ur.Rating as UserRating " );
						
		sql.append( "from Tracks t "
						+ 	"left outer join Artists remixer "
						+		"on ( t.RemixerArtistID = remixer.ArtistID ) "
						+	"left outer join Artists vocalist "
						+		"on ( t.VocalistArtistID = vocalist.ArtistID ) "
						+	"left outer join AlbumEPs ae "
						+		"on ( t.AlbumID = ae.AlbumID ) "  );
		
		if ( userID > 0 )
			sql.append( "left outer join UserRatedTracks ur "
						+	"on ( t.TrackID = ur.TrackID "
						+		"and ur.UserID = " + userID + " ) " );
		
		sql.append( ", "
						+ "Artists ar, "
						+ "Labels l, "
						+ "PartnerInventory pi, "
						+ "PartnerSites ps "
						
					+ "where t.ArtistID = ar.ArtistID "
						+ "and t.LabelID = l.LabelID "
						+ "and t.TrackID = pi.TrackID "
						+ "and pi.PartnerID = ps.PartnerID "
						+ "and t.TrackID in ( " + idList.toString() + " ) "
						+ "and ps.PartnerOrdering > 0 " );
						
		if ( ordering )
			sql.append( "order by " //t.ReleaseDate desc, "
						//+ "t.AlbumID desc, "
						+ "t.LoadDate desc, "
						+ "t.ArtistID, "
						//+ "t.TrackName, "
						//+ "t.MixName, "
						+ "ps.PartnerOrdering " );
		//else
			//sql.append( "order by "
			//			+ "ps.PartnerOrdering" );
		
		return sql.toString();
	}
	

}
