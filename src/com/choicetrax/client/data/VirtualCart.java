package com.choicetrax.client.data;

import java.util.HashMap;
import java.util.Iterator;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.cache.ReleasesCache;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.IsSerializable;


public class VirtualCart 
	implements IsSerializable, LoaderResponse
{
	
	private String partnerName = null;
	
	// cache type = Constants.RELEASES_CACHE_VIRTUAL_CART
	private ReleasesCache cache = null;
	
	// contains a lookup of Integer( trackID ) to Format objects
	private HashMap<Integer,Format> selectedFormats = new HashMap<Integer,Format>();
	
	
	
	// cannot instantiate no-arg constructor - must pass in partnerName
	@SuppressWarnings("unused")
	private VirtualCart() {
		super();
	}
	
	
	public VirtualCart( String partnerName ) {
		super();
		this.partnerName = partnerName;
		this.cache = new ReleasesCache( Constants.CACHE_RELEASES_VIRTUAL_CART );
	}
	
	public VirtualCart( String partnerName, ReleasesCache cache ) {
		super();
		this.partnerName = partnerName;
		this.cache = cache;
	}
	
	
	public String getPartnerName() {
		return partnerName;
	}
	
	
	public ReleasesCache getReleasesCache() {
		return cache;
	}
	
	public HashMap<Integer,Format> getSelectedFormats() {
		return selectedFormats;
	}
	
	public void setSelectedFormats( HashMap<Integer,Format> trackIDToFormatMap )
	{
		this.selectedFormats = trackIDToFormatMap;
	}
	
	public Format getSelectedFormat( int trackID )
	{
		return (Format) selectedFormats.get( new Integer( trackID ) );
	}
	
	
	public String getCartTotalPrice() 
	{
		//String prefix = null;
		float total = 0;
		
		Iterator<Format> i = selectedFormats.values().iterator();
		while ( i.hasNext() ) {
			Format format = i.next();
			String price = format.getPrice();
			
			try {
				if ( price.startsWith( "£" ) || price.startsWith( "$" ) ) {
					total += Float.parseFloat( price.substring( 1 ) );
					//if ( prefix == null ) prefix = price.substring( 0, 1 );
				}
				else if ( price.startsWith ( "&#36;" ) ) {
					total += Float.parseFloat( price.substring( 5 ) );
					//if ( prefix == null ) prefix = price.substring( 0, 5 );
				}
				else {
					total += Float.parseFloat( price );
					//if ( prefix == null ) prefix = "";
				}
			} 
			catch ( Exception e ) { }
		}
		
		return NumberFormat.getFormat( "0.00" ).format( total );
	}
	

	public boolean addRelease( ReleaseDetail rd, Format format )
	{
		if ( getSelectedFormat( rd.getTrackID() ) == null )
		{
			cache.addReleaseDetail( rd );
			
			selectedFormats.put( new Integer( rd.getTrackID() ), format );
			return true;
		}
		else
			return false;
	}
	
	public boolean removeRelease( ReleaseDetail rd )
	{
		if ( cache.containsTrackID( rd.getTrackID() ) )
		{
			cache.removeReleaseDetail( rd );
		
			selectedFormats.remove( new Integer( rd.getTrackID() ) );
			
			return true;
		}
		else
			return false;
	}
	
	
	public int size() {
		return cache.size();
	}
	
	
	public Iterator<ReleaseDetail> getReleaseDetailIterator() {
		return getReleasesCache().getReleaseDetailIterator();
	}
	
	
	public void clear() 
	{
		this.cache = new ReleasesCache( Constants.CACHE_RELEASES_VIRTUAL_CART );
		this.selectedFormats.clear();
	}
	
}
