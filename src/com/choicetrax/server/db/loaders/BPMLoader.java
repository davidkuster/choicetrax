package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;

import com.echonest.api.v3.track.FloatWithConfidence;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.RequestBpmAction;
import com.choicetrax.client.actions.responses.BpmResponse;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.util.EchoNestApiManager;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class BPMLoader implements DataLoader
{
	
	
	public LoaderResponse loadData( LoaderAction action ) 
		throws ChoicetraxException 
	{
		RequestBpmAction requestObj = (RequestBpmAction) action;
		int trackID = requestObj.getTrackID();
		
		// check database first
		FloatWithConfidence fwc = loadBpmFromDatabase( trackID );
		
		float bpm = fwc.getValue();
		float confidence = fwc.getConfidence();
		
		if ( bpm == 0 || bpm == -0.5 ) 
		{
			// BPM is not in database, make Echo Nest API request
			EchoNestApiManager.queueBpmRequest( trackID, 
												requestObj.getAudioPreviewURL() );
			
			setTempBpmInDatabase( trackID );
		}
		
		return new BpmResponse( trackID, bpm, confidence );
	}
	
	
	private FloatWithConfidence loadBpmFromDatabase( int trackID )
		throws ChoicetraxException
	{
		String methodName = "loadBpmFromDatabase()";
		
		String sql = "select BPM, BPMConfidence "
					+ "from Tracks "
					+ "where TrackID = " + trackID;
		
		FloatWithConfidence fwc = null;
		
		DBResource dbHandle = null;
    	
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		if ( rs.next() )
    		{
    			float bpm = rs.getFloat( "BPM" );
    			float conf = rs.getFloat( "BPMConfidence" );
    			
    			fwc = new FloatWithConfidence( conf, bpm );
    		}
    		rs.close();
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error loading BPM from database: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}

		return fwc;
	}
	
	
	private void setTempBpmInDatabase( int trackID )
		throws ChoicetraxException
	{
		String methodName = "setTempBpmInDatabase()";
		
		String sql = "update Tracks "
					+ "set BPM = -0.5 "
					+ "where TrackID = " + trackID;
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			int rowCount = dbHandle.executeUpdate( sql );
			
			if ( rowCount == 0 )
				throw new ChoicetraxException( "Error setting temp BPM" );
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error setting temp BPM in database: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
	}
	
	
	/**
	 * Resets any temp BPMs to 0.0
	 * 
	 * @throws ChoicetraxException
	 */
	public static void resetTempBpmsInDatabase()
		throws ChoicetraxException
	{
		String methodName = "resetTempBpmsInDatabase()";
		
		String sql = "update Tracks "
					+ "set BPM = 0, BPMConfidence = 0 "
					+ "where BPM = -0.5";
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			dbHandle.executeUpdate( sql );
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error resetting temp BPMs in database: " + t,
											"BPMLoader." + methodName );
		}
		finally
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
	}
	
	
	/**
	 * stores BPM and BPM confidence value returned from Echo Nest API 
	 * in database
	 * 
	 * @param trackID
	 * @param fwc
	 * @throws ChoicetraxException
	 */
	public static void storeBpmInDatabase( int trackID, FloatWithConfidence fwc )
		throws ChoicetraxException
	{
		String methodName = "storeBpmInDatabase";
		
		String sql = "update Tracks "
					+ "set BPM = " + fwc.getValue() + ", "
					+	"BPMConfidence = " + fwc.getConfidence() + " "
					+ "where TrackID = " + trackID;
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			int rowCount = dbHandle.executeUpdate( sql );
			
			if ( rowCount == 0 )
				throw new ChoicetraxException( "No rows updated when attempting to set BPM "
												+ "for trackID [" + trackID + "]",
												"BPMLoader." + methodName );
		}
		catch ( ChoicetraxException cte ) 
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error occurred storing BPM for "
												+ "trackID [" + trackID + "]: " + t,
											"BPMLoader." + methodName );
		}
		finally 
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
	}
	

}
