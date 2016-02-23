package com.choicetrax.server.db.handlers;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.data.tracking.TrackingIDs;
import com.choicetrax.server.data.tracking.TrackingQueue;
import com.choicetrax.server.data.tracking.TrackingQueues;
import com.choicetrax.server.data.tracking.TrackingSet;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class TrackingSearchDataHandler
{
	
	private static boolean processing = false;
	
	private static Logger logger = Logger.getLogger( TrackingSearchDataHandler.class );

	
	public static void loadQueues()
	{
		if ( processing ) 
		{
			// if this class is still processing when the
			// cache checker thread starts again, let the
			// current processing finish
			return;
		}
		else
		{
			processing = true;
			
			TrackingSearchDataHandler loader = new TrackingSearchDataHandler();
			loader.processQueues();
			
			processing = false;
		}
	}
	
	
	public TrackingSearchDataHandler() {
		super();
	}
	
	
	public void processQueues() 
	{
		Iterator<String> i = TrackingQueues.getQueueNames();
		while ( i.hasNext() ) 
		{
			String queueName = i.next();
			
			TrackingQueue queue = TrackingQueues.getQueue( queueName );
			
			try
			{
				processQueue( queueName, queue );
			} 
			catch ( Exception e )
			{
				logger.error( "ChoicetraxTrackingDataLoader exception "
								+ "with queue name [" + queueName + "]", 
							e );
			}
		}
	}
	
	
	private void processQueue( String queueName, TrackingQueue queue )
		throws ChoicetraxException
	{
		if ( queue.isEmpty() ) return;
		
		String sql = readQueueAndCreateSQL( queueName, queue );
		
		DBResource dbHandle = null;
		
		try 
		{	
			dbHandle = ResourceManager.getDBConnection();
			
			dbHandle.executeUpdate( sql );
			
			// process next 100 items if necessary
			if ( ! queue.isEmpty() ) 
				processQueue( queueName, queue );
		}
		finally 
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
	}
	

	private String readQueueAndCreateSQL( String queueName, TrackingQueue queue )
	{
		StringBuffer values = new StringBuffer();
		
		for ( int x=0; x < 100; x++ )
		{
			TrackingSet set = queue.poll();
			if ( set == null ) break;
			
			Iterator<TrackingIDs> i = set.iterator();
			while ( i.hasNext() ) 
			{
				TrackingIDs ids = i.next();
				
				if ( values.length() > 0 ) 
					values.append( ", " );
								
				values.append( "( "
								+ "'" + queueName + "', "
								+ ids.getTrackingID() + ", "
								+ ids.getUserTrackingID() + ", "
								+ "current_date() + 0, "
								+ "1 "
								+ ")" );
			}
		}
		
		String sql = "insert into SearchTracking "
					+ "( TrackingType, TrackingID, UserID, SearchDate, NumSearches ) "
					+ "values "
					+ values.toString()
					+ "on duplicate key update "
					+	"NumSearches = NumSearches + 1";
	
		return sql;
	}
	
}
