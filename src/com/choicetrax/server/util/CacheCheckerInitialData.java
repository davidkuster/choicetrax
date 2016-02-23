package com.choicetrax.server.util;

import org.apache.log4j.Logger;

import com.choicetrax.server.db.loaders.ChoicetraxInitialDataLoader;

public class CacheCheckerInitialData extends Thread 
{
	
	private static long delay = 600000;	// 10 minutes
	private boolean reap = true;
	
	private static Logger logger = Logger.getLogger( CacheCheckerInitialData.class );
	

	public CacheCheckerInitialData() {
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
				ChoicetraxInitialDataLoader.checkCachedData();
			}
			catch ( Exception e ) 
			{
				logger.error( "Initial Data cache checker exception", e );
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
