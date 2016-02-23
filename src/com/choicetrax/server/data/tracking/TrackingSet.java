package com.choicetrax.server.data.tracking;

import java.util.HashMap;
import java.util.Iterator;


public class TrackingSet 
{

	private HashMap<Integer,TrackingIDs> idSet = 
		new HashMap<Integer,TrackingIDs>();  

	
	public TrackingSet() {
		super();
	}
	
	
	public void add( int trackingID, String userTrackingID ) 
	{
		idSet.put( trackingID, 
					new TrackingIDs( trackingID, userTrackingID ) );
	}
	
	
	public Iterator<TrackingIDs> iterator() {
		return idSet.values().iterator();
	}
	
	
}
