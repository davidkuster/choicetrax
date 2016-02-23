package com.choicetrax.server.data;


public class EchoNestPendingObj
{
	
	private String apiID = null;
	private int trackID = 0;
	
	
	public EchoNestPendingObj( String apiID, int trackID ) {
		this.apiID = apiID;
		this.trackID = trackID;
	}
	
	
	public String getApiID() {
		return apiID;
	}
	
	public int getTrackID() {
		return trackID;
	}
	
	
	public boolean equals( int id ) 
	{
		if ( this.trackID == id )
			return true;
		else
			return false;
	}
	
	public boolean equals( String id )
	{
		if ( this.apiID.equals( id ) )
			return true;
		else
			return false;
	}

}
