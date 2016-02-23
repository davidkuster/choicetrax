package com.choicetrax.server.util.jdbc;

import java.util.*;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.*; 

import com.choicetrax.client.util.exception.ChoicetraxDatabaseException;
import com.choicetrax.client.util.exception.ChoicetraxException;


public class ResourceManager 
{
	
	private static DataSource ds = null;
	
	// sphinx configuration settings
	private static String sphinxUrl 	= null;
	private static int sphinxPort		= 0;
	
	private static int sphinxStartDate	= 0;
	private static int sphinxEndDate	= 0; 
	
	private static String sphinxIndexTracks		= null;
	private static String sphinxIndexDJCharts	= null;
	
	

	public static void closeResources() throws ChoicetraxException
	{
		try {
			DataSources.destroy( ds );
			ds = null;
		}
		catch ( Exception e ) {
			throw new ChoicetraxException( "Error closing ResourceManager resources: " + e );
		}
	}
	
	
	public static JDBCConnection getDBConnection() throws ChoicetraxDatabaseException
	{
		try
		{
			return new JDBCConnection( ds.getConnection() );
		}
		catch( Exception e ) {
			throw new ChoicetraxDatabaseException( 
					"Unable to get new DB connection: " + e.toString(),
					"com.choicetrax.server.util.jdbc.ResourceManager.getDBConnection()" );
		}
	}
	
	
	public static void initialize( String properties ) throws ChoicetraxException
	{
		try
		{
			ResourceBundle props = ResourceBundle.getBundle( properties );
			
			String driver 	= props.getString( "jdbc.driver" );
			String url		= props.getString( "jdbc.database.url" );
			String username	= props.getString( "jdbc.database.username" );
			String password = props.getString( "jdbc.database.password" );
			
			String initialPoolSize 	= props.getString( "pool.size.initial" );
			String minPoolSize		= props.getString( "pool.size.min" );
			String maxPoolSize		= props.getString( "pool.size.max" );
			String maxIdleTime		= props.getString( "pool.max.idle" );
			String poolTestPeriod	= props.getString( "pool.test.period" );
			String poolTestQuery	= props.getString( "pool.test.query" );
			String poolTestCheckout	= props.getString( "pool.test.checkout" );
			String poolTestCheckin	= props.getString( "pool.test.checkin" );
						
			Class.forName( driver );
			
			Map<String,String> overrides = new HashMap<String,String>(); 
			overrides.put( "initialPoolSize", initialPoolSize );
			overrides.put( "minPoolSize", minPoolSize );
			overrides.put( "maxPoolSize", maxPoolSize );
			overrides.put( "maxIdleTime", maxIdleTime );
			overrides.put( "idleConnectionTestPeriod", poolTestPeriod );
			overrides.put( "preferredTestQuery", poolTestQuery );
			overrides.put( "testConnectionOnCheckout", poolTestCheckout );
			overrides.put( "testConnectionOnCheckin", poolTestCheckin );
			  
			DataSource unpooled = DataSources.unpooledDataSource( url, username, password );
			ds = DataSources.pooledDataSource( unpooled, overrides );
			
			
			// Sphix config settings
			String sphinxURL		= props.getString( "sphinx.url" );
			String sphinxPort		= props.getString( "sphinx.port" );
			String sphinxStartDate	= props.getString( "sphinx.start.date" );
			String sphinxEndDate	= props.getString( "sphinx.end.date" );
			String tracksIndex		= props.getString( "sphinx.index.tracks" );
			String djChartsIndex	= props.getString( "sphinx.index.charts.dj" );
			
			setSphinxUrl( sphinxURL );
			setSphinxPort( Integer.parseInt( sphinxPort ) );
			setSphinxStartDate( Integer.parseInt( sphinxStartDate ) );
			setSphinxEndDate( Integer.parseInt( sphinxEndDate ) );
			setSphinxIndexTracks( tracksIndex );
			setSphinxIndexDJCharts( djChartsIndex );
			
		}
		catch ( Exception e ) {
			throw new ChoicetraxException( "Error initializing Resource Manager: " + e,
							"com.choicetrax.server.util.jdbc.ResourceManager.initialize()" );
		}
	}
	
	

	public static String getSphinxUrl() {
		return sphinxUrl;
	}

	public static void setSphinxUrl( String url ) {
		sphinxUrl = url;
	}

	public static int getSphinxPort() {
		return sphinxPort;
	}

	public static void setSphinxPort( int port ) {
		sphinxPort = port;
	}

	public static int getSphinxStartDate() {
		return sphinxStartDate;
	}
	
	public static void setSphinxStartDate( int startDate ) {
		sphinxStartDate = startDate;
	}

	public static int getSphinxEndDate() {
		return sphinxEndDate;
	}
	
	public static void setSphinxEndDate( int endDate ) {
		sphinxEndDate = endDate;
	}
	
	public static String getSphinxIndexTracks() {
		return sphinxIndexTracks;
	}
	
	public static void setSphinxIndexTracks( String sphinxIndexTracks ) {
		ResourceManager.sphinxIndexTracks = sphinxIndexTracks;
	}

	public static String getSphinxIndexDJCharts() {
		return sphinxIndexDJCharts;
	}
	
	public static void setSphinxIndexDJCharts( String sphinxIndexDJCharts ) {
		ResourceManager.sphinxIndexDJCharts = sphinxIndexDJCharts;
	}
	
}
