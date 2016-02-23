package com.choicetrax.client.data;

import java.util.*;

import com.choicetrax.client.actions.responses.LoaderResponse;


public class VirtualCarts 
	implements LoaderResponse 
{
	
	private HashMap<String,VirtualCart> carts = new HashMap<String,VirtualCart>();
	private String currentCartName = null;
	
	
	
	public VirtualCarts() {
		super();
	}
	
	
	public HashMap<String,VirtualCart> getCarts() {
		return carts;
	}
	
		
	public VirtualCart getCart( String partnerName )
	{
		VirtualCart cart = (VirtualCart) carts.get( partnerName );
		if ( cart == null )
		{
			cart = new VirtualCart( partnerName );
			addCart( partnerName, cart );
		}
		return cart;
	}
	
	
	
	public void addCart( String partnerName, VirtualCart cart )
	{
		carts.put( partnerName, cart );
	}
	
	public void removeCart( String partnerName )
	{
		carts.remove( partnerName );
	}
	
	
	public void addToCart( String partnerName, 
							ReleaseDetail rd, 
							Format format )
	{
		VirtualCart cart = (VirtualCart) carts.get( partnerName );
		
		if ( cart != null )
			cart.addRelease( rd, format );
	}
	
	public void removeFromCart( String partnerName,
								ReleaseDetail rd )
	{
		VirtualCart cart = (VirtualCart) carts.get( partnerName );
		
		if ( cart != null )
		{
			cart.removeRelease( rd );
			
			if ( cart.size() == 0 )
				removeCart( partnerName );
		}
	}

	
	/**
	 * returns the number of virtual carts
	 * @return
	 */
	public int getNumCarts()
	{
		return carts.size();
	}
	
	/**
	 * returns the total number of tracks in all the virtual carts
	 * @return
	 */
	public int size()
	{
		int numTotal = 0;
		
		Iterator<VirtualCart> i = carts.values().iterator();
		while ( i.hasNext() )
		{
			VirtualCart cart = i.next();
			numTotal += cart.size();
		}
		
		return numTotal;
	}
	
	
	public VirtualCart getCurrentCart() {
		return carts.get( currentCartName );
	}
	
	public void setCurrentCart( String partnerName ) {
		this.currentCartName = partnerName;
	}
	
	
	public String getCurrentCartName() {
		return currentCartName;
	}
	
}
