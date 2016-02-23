package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderHistoryAction;


public class DJChartsSearchAction 
	extends LoaderHistoryAction 
{
	
	private String searchTerms = null;
	private int[] genreIDs = null;
	private String recentDate = null;
	private int[] partnerIDs = null;
	private int userID = 0;
	
	
	private static final String PREFIX_SEARCH		= "search(";
	private static final String PREFIX_GENRE 		= "chartgenres(";
	private static final String PREFIX_PARTNER		= "chartpartners(";
	private static final String PREFIX_DATE			= "chartdate(";
	private static final String PREFIX_USER			= "ci(";
	
	
	public DJChartsSearchAction() {
		super();
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
		if ( recentDate != null )
			log.append( "recentDate [" + recentDate + "]" );
		
		return "DJChartsSearchAction: " + log.toString();
	}
	
	
	
	public DJChartsSearchAction( String actionString ) 
	{
		super( actionString );
		
		if ( actionString == null ) return;
		
		searchTerms = readEncodedValue( actionString, PREFIX_SEARCH );
		
		String gIDs = readEncodedValue( actionString, PREFIX_GENRE );
		genreIDs = createIntArray( gIDs );
		
		recentDate = readEncodedValue( actionString, PREFIX_DATE );
		
		String pIDs = readEncodedValue( actionString, PREFIX_PARTNER );
		partnerIDs = createIntArray( pIDs );
		
		String user = readEncodedValue( actionString, PREFIX_USER );
		if ( user != null )
			userID = Integer.parseInt( user );
	}
	
		
	public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( searchTerms != null )
			sb.append( PREFIX_SEARCH + searchTerms + DELIMITER );
		
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
	
	
	
	public String getSearchTerms() {
		return this.searchTerms;
	}



	
	public void setSearchTerms( String searchTerms ) {
		this.searchTerms = searchTerms;
	}



	/*public String toString() 
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
		
		if ( recentDate != null )
			sb.append( "recentDate [" + recentDate + "] " );
		
		if ( userID > 0 )
			sb.append( "userID [" + userID + "] " );
		
		return sb.toString();
	}*/

}
