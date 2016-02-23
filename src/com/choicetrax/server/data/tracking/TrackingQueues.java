package com.choicetrax.server.data.tracking;

import java.util.HashMap;
import java.util.Iterator;


public class TrackingQueues
{

	private static HashMap<String,TrackingQueue> queues =
		new HashMap<String,TrackingQueue>();
	
	
	
	public static void add( String queueType, TrackingSet trackingSet )
	{
		TrackingQueue queue = queues.get( queueType );
		if ( queue == null )
			queue = new TrackingQueue();
		
		queue.add( trackingSet );
		
		queues.put( queueType, queue );
	}
	
	
	public static Iterator<String> getQueueNames() {
		return queues.keySet().iterator();
	}
	
	
	public static TrackingQueue getQueue( String queueType )
	{
		return queues.get( queueType );
	}
	
}
