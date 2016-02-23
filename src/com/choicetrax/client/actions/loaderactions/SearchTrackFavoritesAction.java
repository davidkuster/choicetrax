package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.UserIpAction;
import com.choicetrax.client.data.User;


public class SearchTrackFavoritesAction 
	extends LoaderHistoryAction
	implements UserIpAction
{
	
	private boolean artists = false;
	private boolean labels = false;
	private int[] artistIDs = null;
	private int[] labelIDs = null;
	
	private int userID = -1;
	private String ipAddress = null;
	
	private static final String PREFIX_ARTISTS	= "artists(";
    private static final String PREFIX_LABELS	= "labels(";
    private static final String PREFIX_USER		= "i("; // i for id 
    														// misdirection
    
    
    public SearchTrackFavoritesAction() { 
    	super();
    }
    
    
    public SearchTrackFavoritesAction( User user, String actionString )
    {
    	super( actionString );
		
		if ( actionString == null ) return;
	
		String a = readEncodedValue( actionString, PREFIX_ARTISTS );
		if ( "true".equalsIgnoreCase( a ) )
			artists = true;
		
		String l = readEncodedValue( actionString, PREFIX_LABELS );
		if ( "true".equalsIgnoreCase( l ) )
			labels = true;
						
		String uID = readEncodedValue( actionString, PREFIX_USER );
		if ( uID != null )
			userID = Integer.parseInt( uID );
		
		if ( user != null ) {
			if ( artists ) 	artistIDs 	= user.getFavoriteArtists().getIdArray();
			if ( labels ) 	labelIDs 	= user.getFavoriteLabels().getIdArray();
		}
    }
	
    
    
    public String getLogString() 
	{
		StringBuffer log = new StringBuffer();
		if ( userID != -1 )
			log.append( "userID [" + userID + "] " );
		if ( artistIDs != null )
			log.append( "artistIDs [" + artistIDs + "] " );
		if ( labelIDs != null )
			log.append( "labelIDs [" + labelIDs + "] " );
		
		return "SearchTrackFavoritesAction: " + log.toString();
	}
    
    

    public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append( PREFIX_ARTISTS + artists + DELIMITER );
		
		sb.append( PREFIX_LABELS + labels + DELIMITER );
						
		//if ( userID > 0 )
		//	sb.append( PREFIX_USER + userID + DELIMITER );
		
		sb.append( super.getSortActionString() );
		
		return sb.toString();
	}

	
	public void setUserID( int userID ) {
		this.userID = userID;
	}
	
	public int getUserID() {
		return userID;
	}

	
	public boolean getArtists() {
		return this.artists;
	}
	
	public void setArtists( boolean artists ) {
		this.artists = artists;
	}


	public boolean getLabels() {
		return this.labels;
	}
	
	public void setLabels( boolean labels ) {
		this.labels = labels;
	}
	
	
	public int[] getArtistIDs() {
		return artistIDs;
	}
	
	public int[] getLabelIDs() {
		return labelIDs;
	}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
				
		sb.append( "artistIDs [" + artists + "] " );
		sb.append( "labelIDs [" + labels + "] " );
		
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
