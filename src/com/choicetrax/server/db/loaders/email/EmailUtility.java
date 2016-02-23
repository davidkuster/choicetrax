package com.choicetrax.server.db.loaders.email;

import java.util.Iterator;
import java.util.LinkedList;

import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.loaders.ChoicetraxConfigDataLoader;


public class EmailUtility
{
	
	private static ChoicetraxConfigData configData = null;
	private static boolean initialized = false;
	private static long initTimestamp = -1;
	
	
	private static void initialize() 
		throws ChoicetraxException
	{
		if ( ( configData == null )
				// over 10 hours old
			|| ( initTimestamp > System.currentTimeMillis() - 36000000 ) )
		{
			ChoicetraxConfigDataLoader loader = new ChoicetraxConfigDataLoader();
			configData = loader.loadConfigData();
			
			initialized = true;
			initTimestamp = System.currentTimeMillis();
		}
	}
	
	
	public static String getPartnerName( int partnerID ) throws ChoicetraxException {
		if ( ! initialized ) initialize();
		return configData.lookupPartnerName( partnerID );
	}
	
	public static String getGenreName( int genreID ) throws ChoicetraxException {
		if ( ! initialized ) initialize();
		return configData.getGenre( genreID ).getGenreName();
	}
	
	public static String getPartnerIcon( String partnerID ) throws ChoicetraxException {
		if ( ! initialized ) initialize();
		return configData.lookupPartnerIconFilename( Integer.parseInt( partnerID ) );
	}
	
	
	public static String getGenreShortNames( LinkedList<Genre> genres ) 
		throws ChoicetraxException 
	{
		if ( ! initialized ) initialize();
		
		StringBuffer sb = new StringBuffer();
		
		Iterator<Genre> i = genres.iterator();
		while ( i.hasNext() ) 
		{
			Genre genre = i.next();
			Genre g = configData.getGenre( genre.getGenreID() );
			
			if ( sb.length() > 0 ) sb.append( ", " );
			sb.append( g.getGenreShortName() );
		}
		
		return sb.toString();
	}
	
	
	public static String getPartnerIDs( LinkedList<PartnerInventory> availability )
		throws ChoicetraxException
	{
		if ( ! initialized ) initialize();
		
		StringBuffer sb = new StringBuffer();
		
		Iterator<PartnerInventory> i = availability.iterator();
		while ( i.hasNext() ) 
		{
			PartnerInventory pi = i.next();
			
			if ( sb.length() > 0 ) sb.append( ", " );
			sb.append( pi.getPartnerID() );
		}
		
		return sb.toString();		
	}
	

}
