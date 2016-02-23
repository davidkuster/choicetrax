package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.UserIpAction;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class ReleasesSearchQuickAction 
	extends LoaderHistoryAction
	implements UserIpAction
{
	
    private String searchTerms = null;
    private int[] genreIDs = null;
    private int[] partnerIDs = null;
    private int userID = 0;
    private String ipAddress = null;
         
    
    private static final String PREFIX_QUICK		= "all(";
    private static final String PREFIX_GENRE 		= "genres(";
	private static final String PREFIX_PARTNER		= "partners(";
	private static final String PREFIX_USER			= "i("; // i for id
															// misdirection	
    
    
    public ReleasesSearchQuickAction() {
    	super();
    }
    
    
    
    public ReleasesSearchQuickAction( String actionString )
    {
		super( actionString );
		
		if ( actionString == null ) return;
		
		searchTerms	= readEncodedValue( actionString, PREFIX_QUICK );
		
		String gIDs = readEncodedValue( actionString, PREFIX_GENRE );
		genreIDs = createIntArray( gIDs );
				
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
		if ( searchTerms != null )
			log.append( "searchTerms [" + searchTerms + "] " );
		if ( genreIDs != null )
			log.append( "genreIDs [" + genreIDs + "] " );
		if ( partnerIDs != null )
			log.append( "partnerIDs [" + partnerIDs + "] " );
		
		return "ReleasesSearchQuickAction: " + log.toString();
	}

    

	public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( searchTerms != null )
			sb.append( PREFIX_QUICK + searchTerms + DELIMITER );
		
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
	public String getSearchTerms() {
		return searchTerms;
	}
	public void setSearchTerms(String terms) {
		this.searchTerms = terms;
	}
	public int[] getGenreIDs() {
		return genreIDs;
	}
	public void setGenreIDs(int[] genreIDs) {
		this.genreIDs = genreIDs;
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
		
		if ( searchTerms != null )
			sb.append( "searchTerms [" + searchTerms + "] " );

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
	public void setIpAddress( String ipAddr ) {
		this.ipAddress = ipAddr;
	}

}