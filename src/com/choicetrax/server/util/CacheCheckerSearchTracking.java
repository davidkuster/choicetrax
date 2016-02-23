package com.choicetrax.server.util;

import org.apache.log4j.Logger;

import com.choicetrax.server.db.handlers.TrackingSearchDataHandler;


public class CacheCheckerSearchTracking extends Thread 
{
	
	private static long delay = 300000;	// 5 minutes
	//private static long delay = 60000; // 1 minute for testing
	
	private boolean reap = true;
	
	private static Logger logger = Logger.getLogger( CacheCheckerSearchTracking.class );
	

	public CacheCheckerSearchTracking() {
		super();
	}
	

	public void run() 
	{
		while( reap ) 
		{
			try 
			{
				sleep( delay );
			} 
			catch( InterruptedException ie ) {}

			try 
			{
				TrackingSearchDataHandler.loadQueues();
			}
			catch ( Exception e ) 
			{
				logger.error( "Tracking cache checker exception", e );
			}
		}
	}
	
	public static void setDelay( long newDelay )
	{
		delay = newDelay;
	}
	
	public void stopChecker()
	{
		if ( this.isAlive() )
		{
			reap = false;
			this.interrupt();
		}
	}
	
}
