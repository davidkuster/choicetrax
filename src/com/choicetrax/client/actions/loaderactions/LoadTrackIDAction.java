package com.choicetrax.client.actions.loaderactions;

import com.google.gwt.core.client.GWT;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.UserIpAction;


public class LoadTrackIDAction 
	extends LoaderHistoryAction
	implements UserIpAction
{
	
	private int trackID = -1;
	private int userID = 0;
	private String ipAddress = null;

	
	
	public LoadTrackIDAction() {
		super();
	}
	
	public LoadTrackIDAction( String actionString ) 
	{
		super();

		if ( actionString == null ) return;
		
		try {
			this.trackID = Integer.parseInt( actionString );
		}
		catch ( Exception e ) {
			GWT.log( "Error parsing actionString [" + actionString + "] into trackID", e );
		}
	}
	
	
	public String getLogString() {
		return "LoadTrackIDAction: "
				+ "trackID [" + trackID + "] "
				+ "userID [" + userID + "]";
	}
	
	
	
	public String getActionString() {
		return trackID + "";
	}

	
	public void setUserID( int userID ) {
		this.userID = userID;
	}
	public int getUserID() {
		return userID;
	}
	
	public void setTrackID( int trackID ) {
		this.trackID = trackID;
	}
	public int getTrackID() {
		return trackID;
	}

	
	public String getIpAddress() {
		return this.ipAddress;
	}

	
	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}

}
