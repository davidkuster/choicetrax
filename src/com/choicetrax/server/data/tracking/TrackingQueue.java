package com.choicetrax.server.data.tracking;

import java.util.LinkedList;
import java.util.Queue;


public class TrackingQueue
{

	private Queue<TrackingSet> queue = 
		new LinkedList<TrackingSet>();

	
	public TrackingQueue() {
		super();
	}
	

	public void add( TrackingSet trackingObj ) {
		queue.add( trackingObj );
	}
	
	public TrackingSet poll() {
		return queue.poll();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}

}
