package com.choicetrax.client.data.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AdImageQueueHash implements IsSerializable
{
	
	private HashMap<Integer,AdImageQueue> hash = new HashMap<Integer,AdImageQueue>();
	
	
	public AdImageQueueHash() {
		super();
	}

	
	
	public void addQueue( int genreID, AdImageQueue queue ) {
		hash.put( genreID, queue );
	}
	
	public AdImageQueue lookup( int genreID ) {
		return hash.get( genreID );
	}
	
	public int size() {
		return hash.size();
	}
	
	public Iterator<Integer> keyIterator() {
		return hash.keySet().iterator();
	}
	
	public List<Integer> keys() {
		return new ArrayList<Integer>( hash.keySet() );
	}
	
	
	/*public AdImageQueueHash clone() 
	{
		AdImageQueueHash copy = new AdImageQueueHash();
		
		Iterator<Integer> i = keyIterator();
		while ( i.hasNext() ) {
			int key = i.next();
			AdImageQueue queue = lookup( key );
			copy.addQueue( key, queue.clone() );
		}
		
		return copy;
	}*/
	
}
