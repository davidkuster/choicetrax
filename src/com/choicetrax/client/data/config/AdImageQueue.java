package com.choicetrax.client.data.config;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AdImageQueue implements IsSerializable
{
	
	private LinkedList<AdImage> adList = new LinkedList<AdImage>();
	private int numAdsShown = 0;
	
	
	public AdImageQueue() {
		super();
	}
	
	
	public void add( AdImage adImage ) {
		this.adList.add( adImage );
	}
	
	protected void setNumAdsShown( int count ) {
		this.numAdsShown = count;
	}
	
	
	public int size() {
		return adList.size();
	}

	
	/**
	 * Returns the next AdImage in the list.
	 * Moves the returned image to the end of the queue after returning it.
	 * 
	 * @return AdImage
	 */
	public AdImage getNextAd() 
	{
		AdImage adImage = adList.poll(); // remove first element in the list
		adList.addLast( adImage ); // move element to end of the list
		
		numAdsShown++;
		
		return adImage;
	}
		
	
	/**
	 * Returns the next AdImage in the ad list but does not advance the queue.
	 * @return AdImage
	 */
	public AdImage peekNextAd() {
		return adList.getFirst();
	}
	
	/**
	 * Returns the AdImage at the input index in the ad list,
	 * but does not advance the queue.
	 * 
	 * @param index
	 * @return AdImage
	 */
	public AdImage peekAd( int index ) {
		return adList.get( index );
	}
		
	
	public boolean moveAdToEndOfQueue( AdImage adImage ) 
	{
		boolean b = adList.remove( adImage );
		if ( b ) { 
			adList.addLast( adImage );
			numAdsShown++;
		}
		return b;
	}
	
	
	public boolean allAdsShown() {
		return ( numAdsShown >= adList.size() );
	}
	
	
	
	/*public AdImageQueue clone() 
	{
		AdImageQueue copy = new AdImageQueue();
		
		Iterator<AdImage> i = adList.iterator();
		while ( i.hasNext() ) 
			copy.add( i.next() );
		
		copy.setNumAdsShown( numAdsShown );
		
		return copy;
	}*/

}
