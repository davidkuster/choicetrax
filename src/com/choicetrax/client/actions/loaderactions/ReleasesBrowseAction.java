package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.UserIpAction;


public class ReleasesBrowseAction 
	extends LoaderHistoryAction 
	implements UserIpAction
{
	
	private int[] genreIDs = null;
	private String recentDate = null;
	private int[] partnerIDs = null;
	private boolean onlyExclusive = false;
	private int minRating = 0;
	
	private int userID = 0;
	private String ipAddress = null;
	
	
	private static final String PREFIX_GENRE 		= "genres(";
	private static final String PREFIX_PARTNER		= "partners(";
	private static final String PREFIX_DATE			= "date(";
	private static final String PREFIX_EXCLUSIVE	= "exclusive(";
	private static final String PREFIX_RATING		= "rating(";
	private static final String PREFIX_USER			= "i(";
	
	
	public ReleasesBrowseAction() {
		super();
	}
	
	
	public String getLogString() 
	{
		StringBuffer log = new StringBuffer();
		if ( userID != 0 ) 
			log.append( "userID [" + userID + "] " );
		if ( recentDate != null )
			log.append( "recentDate [" + recentDate + "] " );
		if ( genreIDs != null )
			log.append( "genreIDs [" + genreIDs + "] " );
		if ( partnerIDs != null )
			log.append( "partnerIDs [" + partnerIDs + "] " );
		if ( minRating != 0 )
			log.append( "minRating [" + minRating + "] " );

		// onlyExclusive has been removed from UI
		
		return "ReleasesBrowseAction: " + log.toString();
	}
	
	
	
	public ReleasesBrowseAction( String actionString ) 
	{
		super( actionString );
		
		if ( actionString == null ) return;
		
		String gIDs = readEncodedValue( actionString, PREFIX_GENRE );
		genreIDs = createIntArray( gIDs );
		
		recentDate = readEncodedValue( actionString, PREFIX_DATE );
		
		String pIDs = readEncodedValue( actionString, PREFIX_PARTNER );
		partnerIDs = createIntArray( pIDs );
		
		String exclusive = readEncodedValue( actionString, PREFIX_EXCLUSIVE );
		if ( "true".equals( exclusive ) ) onlyExclusive = true;
		else onlyExclusive = false;

		String rating = readEncodedValue( actionString, PREFIX_RATING );
		if ( rating != null )
			minRating = Integer.parseInt( rating );
		
		String user = readEncodedValue( actionString, PREFIX_USER );
		if ( user != null )
			userID = Integer.parseInt( user );
	}
	
		
	public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
		{
			sb.append( PREFIX_GENRE );
			for ( int x=0; x < genreIDs.length; x++ ) {
				if ( x > 0 ) sb.append( ID_DELIMITER );
				sb.append( genreIDs[ x ] );
			}
			sb.append( DELIMITER );
		}
		
		if ( recentDate != null )
			sb.append( PREFIX_DATE + recentDate + DELIMITER );
		
		if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
		{
			sb.append( PREFIX_PARTNER );
			for ( int x=0; x < partnerIDs.length; x++ ) {
				if ( x > 0 ) sb.append( ID_DELIMITER );
				sb.append( partnerIDs[ x ] );
			}
			sb.append( DELIMITER );
		}
		
		if ( onlyExclusive ) 
			sb.append( PREFIX_EXCLUSIVE + "true" + DELIMITER );
		
		if ( minRating > 0 )
			sb.append( PREFIX_RATING + minRating + DELIMITER );
		
		//if ( userID > 0 )
		//	sb.append( PREFIX_USER + userID + DELIMITER );
		
		sb.append( super.getSortActionString() );
		
		return sb.toString();
	}
	
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int[] getGenreIDs() {
		return genreIDs;
	}
	public void setGenreIDs(int[] genreIDs) {
		this.genreIDs = genreIDs;
	}
	public String getRecentDate() {
		return recentDate;
	}
	public void setRecentDate(String recentDate) {
		this.recentDate = recentDate;
	}
	public int[] getPartnerIDs() {
		return partnerIDs;
	}
	public void setPartnerIDs(int[] partnerIDs) {
		this.partnerIDs = partnerIDs;
	}
	public boolean isOnlyExclusive() {
		return onlyExclusive;
	}
	public void setOnlyExclusive(boolean onlyExclusive) {
		this.onlyExclusive = onlyExclusive;
	}
	public int getMinRating() {
		return minRating;
	}
	public void setMinRating(int minRating) {
		this.minRating = minRating;
	}
	
	
	
	public String toString() 
	{
		StringBuffer sb = new StringBuffer();
		
		if ( genreIDs != null ) {
			sb.append( "genreIDs [" );
			for ( int x=0; x < genreIDs.length; x++ ) {
				if ( x > 0 ) sb.append( "," );
				sb.append( genreIDs[ x ] );
			}
			sb.append( "] " );
		}
		
		if ( partnerIDs != null ) {
			sb.append( "partnerIDs [" );
			for ( int x=0; x < partnerIDs.length; x++ ) {
				if ( x > 0 ) sb.append( "," );
				sb.append( partnerIDs[ x ] );
			}
			sb.append( "] " );
		}
		
		if ( recentDate != null )
			sb.append( "recentDate [" + recentDate + "] " );
		
		if ( onlyExclusive )
			sb.append( "onlyExclusive [true] " );
		
		if ( minRating > 0 )
			sb.append( "minRating [" + minRating + "] " );
		
		if ( userID > 0 )
			sb.append( "userID [" + userID + "] " );
		
		return sb.toString();
	}


	
	public String getIpAddress() {
		return this.ipAddress;
	}


	
	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}

}
