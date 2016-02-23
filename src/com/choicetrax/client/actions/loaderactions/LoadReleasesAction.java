package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.UserIpAction;


public class LoadReleasesAction 
	implements LoaderAction, UserIpAction
{
	
	private int userID = 0;
	private String ipAddress = null;
	
	private int[] trackIDs = null;
	private boolean ordering = true;
	
	
	public LoadReleasesAction() {
		super();
	}
	
	public LoadReleasesAction( int[] ids ) {
		super();
		this.trackIDs = ids;
	}
	
	
	
	public String getLogString() 
	{
		int length = 0;
		if ( trackIDs != null )
			length = trackIDs.length;
		
		return "LoadReleasesAction: "
				+ "userID [" + userID + "] "
				+ "trackID count [" + length + "] "
				+ "ordering [" + ordering + "]";
	}
		
	
	
	public int[] getTrackIDs() {
		return trackIDs;
	}
	
	public void setUserID( int id ) {
		this.userID = id;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setOrdering( boolean useOrdering ) {
		this.ordering = useOrdering;
	}
	
	public boolean getOrdering() {
		return ordering;
	}

	
	public String getIpAddress() {
		return this.ipAddress;
	}

	
	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}
	
	
}
