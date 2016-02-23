package com.choicetrax.server.data.tracking;


public class TrackingIDs
{
	
	private int trackingID = 0;
	private String userTrackingID = null;
	
	
	public TrackingIDs( int trackingID, String userTrackingID ) {
		super();
		setTrackingID( trackingID );
		setUserTrackingID( userTrackingID );
	}


	
	public int getTrackingID() {
		return this.trackingID;
	}	
	public void setTrackingID( int trackingID ) {
		this.trackingID = trackingID;
	}


	public String getUserTrackingID() {
		return this.userTrackingID;
	}	
	public void setUserTrackingID( String userTrackingID ) {
		this.userTrackingID = userTrackingID;
	}

}
