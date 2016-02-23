package com.choicetrax.server.util;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.echonest.api.v3.EchoNestException;
import com.echonest.api.v3.track.FloatWithConfidence;
import com.echonest.api.v3.track.TrackAPI;
import com.echonest.api.v3.track.TrackAPI.AnalysisStatus;

import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.data.EchoNestPendingObj;
import com.choicetrax.server.data.EchoNestRequestObj;
import com.choicetrax.server.db.loaders.BPMLoader;


public class EchoNestApiManager extends Thread
{
	
	private static Logger logger = Logger.getLogger( EchoNestApiManager.class );
		
	private static boolean apiProcessRunning = false;
	private static LinkedList<EchoNestRequestObj> requestQueue = 
													new LinkedList<EchoNestRequestObj>();
	private static LinkedList<EchoNestPendingObj> pendingQueue = 
													new LinkedList<EchoNestPendingObj>();
	
	//private static HashSet<Integer> requestIDs = new HashSet<Integer>();
	//private static HashSet<Integer> pendingIDs = new HashSet<Integer>();
	private static LinkedHashSet<Integer> requestedIDs = new LinkedHashSet<Integer>();
	
	private static EchoNestApiManager apiManager = null;
	
	private boolean process = true;
	private int apiRequestCount = 0;
	private long apiStartTime = 0;
	private boolean initialized = false;
	
	private static final int API_REQUEST_LIMIT = 120;
	private static final int API_REQUEST_LIMIT_TIME_MS = 60000;
	
		
	
	private EchoNestApiManager() {
		super();
	}
	
	
	public static EchoNestApiManager getInstance() 
	{
		if ( apiManager == null )
			apiManager = new EchoNestApiManager();
		
		return apiManager;
	}
	
	
	
	public void run() 
	{
		while( process ) 
		{
			try 
			{
				sleep( 600000 );
			} 
			catch( InterruptedException ie ) {}

			try 
			{
				processRequests();
			}
			catch ( Exception e ) 
			{
				logger.error( "Tracking cache checker exception", e );
			}
		}
	}
	
	
	public boolean isInitialized() {
		return this.initialized;
	}
	public void setInitialized( boolean b ) {
		this.initialized = b;
	}
	
	
	public void initialize()
	{
		// on first request, reset any pending BPMs (-0.5) back to 0
		if ( ! initialized ) 
		{
			try
			{
				BPMLoader.resetTempBpmsInDatabase();
				logger.info( "initialized Echo Nest API manager" );
			}
			catch ( Exception e ) {
				logger.error( "Unable to initialize EchoNestApiManager", e );
			}
			
			setInitialized( true );
		}
	}
	
	
	public void stopManager()
	{
		if ( this.isAlive() )
		{
			process = false;
			this.interrupt();
		}
	}
	
	
	public static void queueBpmRequest( int trackID, String audioPreviewURL )
	{
		getInstance().initialize();
		
		if ( ! requestedIDs.contains( trackID ) )
		{
			EchoNestRequestObj requestObj = new EchoNestRequestObj( trackID, audioPreviewURL );
			
			// remove items from requestedIDs when it grows too large
			if ( requestedIDs.size() > 1000 ) {
				Iterator<Integer> i = requestedIDs.iterator();
				for ( int x=0; x < 100; x++ ) {
					synchronized ( requestedIDs ) {
						requestedIDs.remove( i.next() );
					}
				}
			}
			
			requestedIDs.add( trackID );
			requestQueue.add( requestObj );
		}
		
		if ( ! apiProcessRunning ) 
		{
			apiProcessRunning = true;
			getInstance().interrupt();
		}
	}
	
	
	
	public void processRequests()
	{
		try
		{
			TrackAPI trackAPI = new TrackAPI( "LYJ0QPVJZO4XMJ3DB" );
			
			while ( ( ! requestQueue.isEmpty() ) || ( ! pendingQueue.isEmpty() ) )
			{
				if ( ! requestQueue.isEmpty() )
				{
					EchoNestRequestObj requestObj = requestQueue.removeFirst();
					int trackID = requestObj.getTrackID();
					String audioPreviewURL = requestObj.getAudioPreviewURL();
					
					logger.info( "requesting BPM for trackID [" + trackID + "], "
								+ "clip [" + audioPreviewURL + "], "
								+ "request queue size [" + requestQueue.size() + "], "
								+ "pending queue size [" + pendingQueue.size() + "]" );
					
					URL mp3Url = new URL( audioPreviewURL );
					
					checkNumRequests();
					String id = trackAPI.uploadTrack( mp3Url, false );
					
					EchoNestPendingObj pendingObj = new EchoNestPendingObj( id, trackID );
					pendingQueue.add( pendingObj );
				}
								
				if ( ! pendingQueue.isEmpty() )
				{
					EchoNestPendingObj pendingObj = pendingQueue.removeFirst();
					String apiID = pendingObj.getApiID();
					int trackID = pendingObj.getTrackID();
					
					checkNumRequests();
					AnalysisStatus status = trackAPI.getAnalysisStatus( apiID );
				    
				    if ( status == AnalysisStatus.COMPLETE ) 
				    {
				    	checkNumRequests();
				    	FloatWithConfidence bpm = trackAPI.getTempo( apiID );
				    	
				    	logger.info( "analysis complete for trackID [" + trackID + "], "
									+ "BPM [" + bpm.getValue() + "], "
									+ "confidence [" + bpm.getConfidence() + "], "
									+ "request queue size [" + requestQueue.size() + "], "
									+ "pending queue size [" + pendingQueue.size() + "]" );
				    	
				    	BPMLoader.storeBpmInDatabase( trackID, bpm );
				    } 
				    else if ( status == AnalysisStatus.ERROR )
				    {
				    	logger.warn( "EchoNest API returned status of ERROR for "
				    				+ "API ID [" + apiID + "], trackID [" + trackID + "]" );
				    	
				    	// TODO: check for other audio clips for this track and try again
				    	// with a different store's audio clip?

				    	// set temp BPM back to -1, indicating an error
				    	FloatWithConfidence bpm = new FloatWithConfidence( 0, -1 );
				    	BPMLoader.storeBpmInDatabase( trackID, bpm );
				    }
				    else
				    {
				    	logger.info( "analysis status = " + status + ", "
				    				+ "putting obj w/trackID [" + trackID + "] "
				    				+ "back on pendingQueue" );
				    	pendingQueue.add( pendingObj );
				    }
				}
			}
		}
		catch ( EchoNestException ene )
		{
			logger.error( "EchoNestException processing EchoNest API requests", ene );
		}
		catch ( ChoicetraxException cte ) 
		{
			logger.error( "Choicetrax exception in EchoNestApiManager", cte );
		}
		catch ( Throwable t )
		{
			logger.error( "Unknown error processing EchoNest API requests", t );
		}
		
		apiProcessRunning = false;
	}
	
	
	private void checkNumRequests()
	{
		apiRequestCount++;
		
		if ( apiStartTime == 0 )
			apiStartTime = System.currentTimeMillis();
		
		if ( apiRequestCount >= API_REQUEST_LIMIT )
		{
			long elapsedTime = System.currentTimeMillis() - apiStartTime;
			if ( elapsedTime < API_REQUEST_LIMIT_TIME_MS )
			{
				long wait = API_REQUEST_LIMIT_TIME_MS - elapsedTime;
				
				logger.info( "made " + apiRequestCount + " requests to Echo Nest API "
						+ "in " + elapsedTime + " milliseconds - sleeping " + wait );
				
				try {
					sleep( wait );
				} catch ( InterruptedException ie ) { }
			}
			
			apiRequestCount = 0;
			apiStartTime = 0;
		}
	}
	
	
	/*private FloatWithConfidence echoNestRequest( int trackID, String mp3URL )
		throws ChoicetraxException
	{
		FloatWithConfidence bpm = null;
		
		try
		{
			TrackAPI trackAPI = new TrackAPI( "LYJ0QPVJZO4XMJ3DB" );
	
			long time = System.currentTimeMillis();
		    String id = trackAPI.uploadTrack( new URL( mp3URL ), false );
		    long diff = System.currentTimeMillis() - time;
		    		    
		    AnalysisStatus status = trackAPI.waitForAnalysis( id, 60000 );
		    
		    if ( status == AnalysisStatus.COMPLETE ) 
		    {
		    	long time2 = System.currentTimeMillis();
		    	bpm = trackAPI.getTempo( id );
		    	long diff2 = System.currentTimeMillis() - time2;
		    	
		    	storeBpmInDatabase( trackID, bpm );
		    } 
		    else
		    	System.out.println( "AnalysisStatus = " + status );
		} 
		catch ( EchoNestException ene )
		{
			System.out.println( "EchoNestException: " + ene );
		}
		catch ( Exception e )
		{
			System.out.println( "General exception: " + e );
		}
		
		return bpm;
	}*/
	
	
}
