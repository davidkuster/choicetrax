package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.UserIpAction;
import com.choicetrax.client.constants.Constants;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class ReleasesSearchAdvancedAction 
	extends LoaderHistoryAction
	implements UserIpAction
{
	
    private String artistName = null;
    private String trackName = null;
    private String labelName = null;
    private int[] genreIDs = null;
    private java.util.Date releaseDateBegin = null;
    private java.util.Date releaseDateEnd = null;
    private int[] partnerIDs = null;
    
    private int userID = 0;
    private String ipAddress = null;
         
    
    private static final String PREFIX_ARTIST		= "artist(";
    private static final String PREFIX_TRACK		= "track(";
    private static final String PREFIX_LABEL		= "label(";
    private static final String PREFIX_GENRE 		= "genres(";
	private static final String PREFIX_PARTNER		= "partners(";
	private static final String PREFIX_DATE_BEGIN	= "beginDate(";
	private static final String PREFIX_DATE_END		= "endDate(";
	private static final String PREFIX_USER			= "i("; // i for id	
	
	
	
    public ReleasesSearchAdvancedAction() { 
    	super();
    }   
    
    
    public ReleasesSearchAdvancedAction( String actionString )
    {
		super( actionString );
		
		if ( actionString == null ) return;
		
		artistName 	= readEncodedValue( actionString, PREFIX_ARTIST );
		trackName	= readEncodedValue( actionString, PREFIX_TRACK );
		labelName	= readEncodedValue( actionString, PREFIX_LABEL );
		
		String gIDs = readEncodedValue( actionString, PREFIX_GENRE );
		genreIDs = createIntArray( gIDs );
		
		String beginDate = readEncodedValue( actionString, PREFIX_DATE_BEGIN );
		if ( beginDate != null )
			releaseDateBegin = Constants.DATE_FORMATTER_USA.parse( beginDate );
		
		String endDate = readEncodedValue( actionString, PREFIX_DATE_END );
		if ( endDate != null )
			releaseDateEnd = Constants.DATE_FORMATTER_USA.parse( endDate );
		
		String pIDs = readEncodedValue( actionString, PREFIX_PARTNER );
		partnerIDs = createIntArray( pIDs );
				
		String user = readEncodedValue( actionString, PREFIX_USER );
		if ( user != null )
			userID = Integer.parseInt( user );
	}

    
    
    public String getLogString() 
	{
		StringBuffer log = new StringBuffer();
		if ( userID != 0 )
			log.append( "userID [" + userID + "] " );
		if ( artistName != null )
			log.append( "artist [" + artistName + "] " );
		if ( trackName != null )
			log.append( "track [" + trackName + "] " );
		if ( labelName != null ) 
			log.append( "label [" + labelName + "] " );
		if ( genreIDs != null )
			log.append( "genreIDs [" + genreIDs + "] " );
		if ( partnerIDs != null )
			log.append( "partnerIDs [" + partnerIDs + "] " );
		if ( releaseDateBegin != null )
			log.append( "beginDT [" + releaseDateBegin + "] " );
		if ( releaseDateEnd != null )
			log.append( "endDT [" + releaseDateEnd + "] " );
		
		return "ReleasesSearchAdvancedAction: " + log.toString();
	}
    

	public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( artistName != null )
			sb.append( PREFIX_ARTIST + artistName + DELIMITER );
		
		if ( trackName != null )
			sb.append( PREFIX_TRACK + trackName + DELIMITER );
		
		if ( labelName != null )
			sb.append( PREFIX_LABEL + labelName + DELIMITER );
		
		if ( releaseDateBegin != null )
			sb.append( PREFIX_DATE_BEGIN
						+ Constants.DATE_FORMATTER_USA.format( releaseDateBegin )
						+ DELIMITER );
		
		if ( releaseDateEnd != null )
			sb.append( PREFIX_DATE_END 
						+ Constants.DATE_FORMATTER_USA.format( releaseDateEnd )
						+ DELIMITER );
		
		if ( ( genreIDs != null ) && ( genreIDs.length > 0 ) )
		{
			sb.append( PREFIX_GENRE );
			for ( int x=0; x < genreIDs.length; x++ ) {
				if ( x > 0 ) sb.append( ID_DELIMITER );
				sb.append( genreIDs[ x ] );
			}
			sb.append( DELIMITER );
		}
				
		if ( ( partnerIDs != null ) && ( partnerIDs.length > 0 ) )
		{
			sb.append( PREFIX_PARTNER );
			for ( int x=0; x < partnerIDs.length; x++ ) {
				if ( x > 0 ) sb.append( ID_DELIMITER );
				sb.append( partnerIDs[ x ] );
			}
			sb.append( DELIMITER );
		}
		
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
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public int[] getGenreIDs() {
		return genreIDs;
	}
	public void setGenreIDs(int[] genreIDs) {
		this.genreIDs = genreIDs;
	}
	public java.util.Date getReleaseDateBegin() {
		return releaseDateBegin;
	}
	public void setReleaseDateBegin(java.util.Date releaseDateBegin) {
		this.releaseDateBegin = releaseDateBegin;
	}
	public java.util.Date getReleaseDateEnd() {
		return releaseDateEnd;
	}
	public void setReleaseDateEnd(java.util.Date releaseDateEnd) {
		this.releaseDateEnd = releaseDateEnd;
	}
	public int[] getPartnerIDs() {
		return partnerIDs;
	}
	public void setPartnerIDs(int[] partnerIDs) {
		this.partnerIDs = partnerIDs;
	}

	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( artistName != null )
			sb.append( "artistName [" + artistName + "] " );
		
		if ( trackName != null )
			sb.append( "trackName [" + trackName + "] " );
		
		if ( labelName != null )
			sb.append( "labelName [" + labelName + "] " );
		
		if ( releaseDateBegin != null )
			sb.append( "releaseDateBegin [" 
						+ Constants.DATE_FORMATTER_USA.format( releaseDateBegin )
						+ "] " );
		
		if ( releaseDateEnd != null )
			sb.append( "releaseDateEnd ["
						+ Constants.DATE_FORMATTER_USA.format( releaseDateEnd )
						+ "] " );
		
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