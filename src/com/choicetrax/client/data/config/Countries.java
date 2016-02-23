package com.choicetrax.client.data.config;

import java.util.Iterator;
import java.util.LinkedList;

import com.choicetrax.client.actions.responses.LoaderResponse;


public class Countries 
	implements LoaderResponse
{
	
	private LinkedList<Country> list = null;
	
	
	public Countries() {
		super();
		list = new LinkedList<Country>();
	}
	
	public void addCountry( Country country ) {
		list.add( country );
	}
	
	public LinkedList<Country> getCountryList() {
		return list;
	}
	
	public int size() {
		return list.size();
	}
	
	
	public String getCountryCode( String countryName ) 
	{
		if ( countryName != null ) 
		{
			Iterator<Country> i = list.iterator();
			while ( i.hasNext() ) 
			{
				Country country = i.next();
				
				if ( country.getCountryName().equals( countryName ) )
					return country.getCountryCode();
			}
		}
		return null;
	}
	
	
	public int getCountryID( String countryCode )
	{
		if ( countryCode != null )
		{
			Iterator<Country> i = list.iterator();
			while ( i.hasNext() )
			{
				Country country = i.next();
				
				if ( country.getCountryCode().equals( countryCode ) )
					return country.getCountryID();
			}
		}
		return 0;
	}
	
	
	public String getCountryName( String countryCode ) 
	{
		if ( countryCode != null ) 
		{
			Iterator<Country> i = list.iterator();
			while ( i.hasNext() ) 
			{
				Country country = i.next();
				
				if ( country.getCountryCode().equals( countryCode ) )
					return country.getCountryName();
			}
		}
		return null;
	}

}
