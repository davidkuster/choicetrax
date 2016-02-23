package com.choicetrax.server.db.loaders;

import java.util.List;

import org.apache.log4j.Logger;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.config.AdFeaturedReleases;
import com.choicetrax.client.data.config.AdImages;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.data.config.ChoicetraxInitialData;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.db.DataLoader;



/**
 * loads UI configuration information at client launch
 * @author David Kuster
 *
 */
public class ChoicetraxInitialDataLoader 
	implements DataLoader
{
	
	private static long timeout = 1800000;  // 30 minutes
	private static long lastRefresh = -1;
	
	// cache initial data
	private static ChoicetraxInitialData cachedData = null; 
	
	private static Logger logger = Logger.getLogger( ChoicetraxInitialDataLoader.class );
	
	
	public ChoicetraxInitialDataLoader() {
		super();
	}
	
	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		checkCachedData();
		
		return cachedData;		
	}
	
	
	
	public static void checkCachedData() 
		throws ChoicetraxException
	{
		long stale = System.currentTimeMillis() - timeout;
		
		if ( ( stale > lastRefresh ) || ( cachedData == null ) )
		{
			if ( cachedData == null )
				logger.info( "loading initial data" );
			
			cachedData = loadInitialData();
			lastRefresh = System.currentTimeMillis();
		}
	}
	
	
	private static ChoicetraxInitialData loadInitialData()
		throws ChoicetraxException
	{
		ChoicetraxConfigData configData = loadConfigData();
		AdFeaturedReleases featuredReleases = loadFeaturedReleases();
		AdImages imgLoad = loadAdImages( configData.getChoicetraxGenres() );
		
		ChoicetraxInitialData initialData = new ChoicetraxInitialData();
		initialData.setConfigData( configData );
		initialData.setFeaturedReleases( featuredReleases );
		initialData.setImgLoad( imgLoad );
		
		return initialData;
	}
	
	
	
	private static ChoicetraxConfigData loadConfigData() 
		throws ChoicetraxException
	{
		return (new ChoicetraxConfigDataLoader()).loadConfigData();
	}
	
	
	private static AdFeaturedReleases loadFeaturedReleases() 
		throws ChoicetraxException
	{
		return (new AdFeaturedReleasesLoader()).loadFeaturedReleasesData();
	}
	
	
	private static AdImages loadAdImages( List<Genre> genreList )
		throws ChoicetraxException
	{
		return (new AdImagesLoader()).loadAdImagesData( genreList );
	}

}
