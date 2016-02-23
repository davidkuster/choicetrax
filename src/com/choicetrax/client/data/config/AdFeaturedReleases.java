package com.choicetrax.client.data.config;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AdFeaturedReleases 
	implements IsSerializable 
{
	
	// maps genreID (Integer) to list of featured releases for that genre
	HashMap<Integer,List<AdFeaturedRelease>> featuredReleasesMap = null;
	
	
	public AdFeaturedReleases() {
		super();
		featuredReleasesMap = new HashMap<Integer,List<AdFeaturedRelease>>();
	}
	
	
	public AdFeaturedReleases( HashMap<Integer,List<AdFeaturedRelease>> featuredMap ) {
		super();
		featuredReleasesMap = featuredMap;
	}
	
	
	public void addRelease( int genreID, AdFeaturedRelease featuredRelease ) 
	{
		Integer gID = new Integer( genreID );
		
		List<AdFeaturedRelease> list = featuredReleasesMap.get( gID );
		if ( list == null ) list = new ArrayList<AdFeaturedRelease>();
		
		list.add( featuredRelease );
		featuredReleasesMap.put( gID, list );
	}
	
	
	public List<AdFeaturedRelease> getReleasesForGenre( int genreID ) {
		return featuredReleasesMap.get( new Integer( genreID ) );
	}

}
