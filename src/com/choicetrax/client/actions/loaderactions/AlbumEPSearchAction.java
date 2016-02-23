package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.UserIpAction;


public class AlbumEPSearchAction 
	implements LoaderAction, UserIpAction
{
	
	private int trackID = -1;
	private int userID = -1;
	private String ipAddress = null;
	
	
	public AlbumEPSearchAction() {
		super();
	}
	
	
	public String getLogString() {
		return "AlbumEPSearchAction: "
				+ "trackID [" + trackID + "]";
	}

	

	public int getTrackID() {
		return trackID;
	}

	public void setTrackID(int trackID) {
		this.trackID = trackID;
	}


	
	public int getUserID() {
		return this.userID;
	}


	
	public void setUserID( int userID ) {
		this.userID = userID;
	}


	
	public String getIpAddress() {
		return this.ipAddress;
	}


	
	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}
	

}
