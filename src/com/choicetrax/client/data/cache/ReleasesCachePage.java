package com.choicetrax.client.data.cache;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.Releases;


public class ReleasesCachePage 
	implements LoaderResponse 
{

	private LinkedHashMap<Integer,ReleaseDetail> cache 
		= new LinkedHashMap<Integer,ReleaseDetail>();

	
	public ReleasesCachePage() {
		super();
	}
	
	
	public void addReleaseDetail( ReleaseDetail rd )
	{
		if ( rd != null )
			cache.put( new Integer( rd.getTrackID() ), rd );
	}
	
	public void addTrackID( int trackID )
	{
		cache.put( new Integer( trackID ), null );
	}
	
	public void addReleases( Releases releases )
	{
		if ( releases != null )
		{
			Iterator<ReleaseDetail> i = releases.getReleasesList().iterator();
			while ( i.hasNext() )
			{
				this.addReleaseDetail( i.next() );
			}
		}
	}
	
	public ReleaseDetail getReleaseDetail( int trackID )
	{
		return cache.get( new Integer( trackID ) );
	}
	
	public void remove( int trackID )
	{
		Integer tID = new Integer( trackID );
		
		cache.remove( tID );
		cache.keySet().remove( tID );
	}
	
	public boolean containsTrackID( int trackID )
	{
		return cache.containsKey( new Integer( trackID ) );
	}
	
	public Releases getReleases()
	{
		Releases releases = new Releases();
		Iterator<ReleaseDetail> i = cache.values().iterator();
		while ( i.hasNext() )
		{
			ReleaseDetail rd = i.next();
			if ( rd != null )
				releases.addRelease( rd );
		}
		return releases;
	}
	
	public int[] getTrackIDs()
	{
		if ( this.size() > 0 )
		{
			LinkedList<Integer> idList = new LinkedList<Integer>();
			Iterator<Integer> i = cache.keySet().iterator();
			while ( i.hasNext() ) {
				idList.add( i.next() );
			}
			
			int size = idList.size();
			int[] trackIDs = new int[ size ];
			for ( int x=0; x < size; x++ ) {
				trackIDs[ x ] = idList.get( x );
			}
			
			return trackIDs;
		}
		else return new int[ 0 ];
	}
	
	
	public int size() {
		return cache.size();
	}
	
	
	public void clearReleases() 
	{
		Iterator<Integer> i = cache.keySet().iterator();
		while ( i.hasNext() ) {
			cache.put( i.next(), null );
		}
	}

}
