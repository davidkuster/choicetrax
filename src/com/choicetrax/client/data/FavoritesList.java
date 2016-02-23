package com.choicetrax.client.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.user.client.rpc.IsSerializable;


public class FavoritesList 
	extends LinkedList<TrackComponent>
	implements IsSerializable
{
	
	private static final long	serialVersionUID	= 1L;
	
	private ArrayList<Integer> idSet = new ArrayList<Integer>();
	

	public FavoritesList() { 
		super();
	}
	
	
	public boolean add( TrackComponent tc ) {
		boolean result = super.add( tc );
		if ( result ) idSet.add( (Integer) tc.getID() );
		return result;
	}
	
	public boolean remove( TrackComponent tc ) {
		boolean result = super.remove( tc );
		if ( result ) idSet.remove( (Integer) tc.getID() );
		return result;
	}
	
	
	public int[] getIdArray() 
	{
		int[] ids = new int[ idSet.size() ];
		
		Iterator<Integer> i = idSet.iterator();
		for ( int x=0; i.hasNext(); x++ ) {
			ids[ x ] = i.next();
		}
		
		return ids;
	}
	
	
	public boolean containsTrackID( int id ) {
		return idSet.contains( (Integer) id );
	}
	
	
}
