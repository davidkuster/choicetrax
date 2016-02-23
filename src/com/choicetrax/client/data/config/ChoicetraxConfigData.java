package com.choicetrax.client.data.config;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.Partner;


public class ChoicetraxConfigData 
	implements IsSerializable 
{
	
    private List<Genre> choicetraxGenres = null;
	private LinkedHashMap<Integer, Genre> genreMap = null;
    private List<Partner> choicetraxPartners = null;
    private Countries countries = null;
	
    
    public ChoicetraxConfigData() { }
    

    
    public Countries getCountries() {
    	return countries;
    }
    
    public void setCountries( Countries countries ) {
    	this.countries = countries;
    }
    
	public List<Genre> getChoicetraxGenres() {
		return choicetraxGenres;
	}

	public void setChoicetraxGenres(List<Genre> choicetraxGenres) 
	{
		this.choicetraxGenres = choicetraxGenres;
		genreMap = new LinkedHashMap<Integer, Genre>();
		
		Iterator<Genre> i = choicetraxGenres.iterator();
		while ( i.hasNext() )
		{
			Genre genre = i.next();
			genreMap.put( new Integer( genre.getGenreID()), genre );
		}
	}

	public List<Partner> getChoicetraxPartners() {
		return choicetraxPartners;
	}

	public void setChoicetraxPartners(List<Partner> choicetraxPartners) {
		this.choicetraxPartners = choicetraxPartners;
	}
	
	
	public Genre getGenre( int genreID )
	{
		if ( genreMap != null )
		{
			return genreMap.get( new Integer( genreID ) );
		}
		return null;
	}
	
	public int getGenreID( String genreName ) 
	{
		if ( ( genreName != null ) && ( choicetraxGenres != null ) )
		{
			Iterator<Genre> i = choicetraxGenres.iterator();
			while ( i.hasNext() ) {
				Genre genre = i.next();
				if ( genreName.equals( genre.getGenreName() ) 
						|| genreName.equals( genre.getGenreShortName() ) )
					return genre.getGenreID();
			}
		}
		return -1;
	}
	
	/*private Genre getGenre( String genreName )
	{
		if ( choicetraxGenres != null )
		{
			Iterator<Genre> i = choicetraxGenres.iterator();
			while ( i.hasNext() )
			{
				Genre genre = i.next();
				if ( genreName.equals( genre.getGenreName() ) )
					return genre;
			}
		}
		return null;
	}*/
	
	public Partner getPartner( int partnerID )
	{
		if ( choicetraxPartners != null )
		{
			Iterator<Partner> i = choicetraxPartners.iterator();
			while ( i.hasNext() ) 
			{
				Partner partner = i.next();
				if ( partner.getPartnerID() == partnerID )
					return partner;
			}
		}
		return null;
	}
	
	public Partner getPartner( String partnerName )
	{
		if ( choicetraxPartners != null )
		{
			Iterator<Partner> i = choicetraxPartners.iterator();
			while ( i.hasNext() )
			{
				Partner partner = i.next();
				if ( partnerName.equals( partner.getPartnerName() ) )
					return partner;
			}
		}
		return null;
	}
	
	/*public String lookupGenreName( int genreID )
	{
		Genre genre = getGenre( genreID );
		
		if ( genre == null ) 
		{
			if ( choicetraxGenres == null )
				return "genre list not populated";
			else 
				return "genre ID [" + genreID + "] not found";
		}
		else
			return genre.getGenreName();
	}*/
	
	/*public int lookupGenreID( String genreName )
	{
		Genre genre = getGenre( genreName );
		
		if ( genre == null ) 
			return -1;
		else
			return genre.getGenreID();
	}*/
    
    public String lookupPartnerName( int partnerID )
    {
    	Partner partner = getPartner( partnerID );
    	
    	if ( partner == null )
    	{
    		if ( choicetraxPartners == null )
    			return "partners list not populated";
    		else
    			return "partner ID [" + partnerID + "] not found";
    	}
    	else
    		return partner.getPartnerName();
    }
    
    public int lookupPartnerID( String partnerName )
    {
    	Partner partner = getPartner( partnerName );
    	
    	if ( partner == null )	
    		return -1;
    	else
    		return partner.getPartnerID();
    }
    
    public String lookupPartnerIconFilename( int partnerID )
    {
    	Partner partner = getPartner( partnerID );
    	
    	if ( partner == null )
    	{
    		if ( choicetraxPartners == null )
    			return "partners list not populated";
    		else
    			return "partner ID [" + partnerID + "] not found";
    	}
    	else
    		return partner.getPartnerIconFilename();
    }
    
    public String lookupPartnerWebURL( int partnerID )
    {
    	Partner partner = getPartner( partnerID );
    	
    	if ( partner == null )
    	{
    		if ( choicetraxPartners == null )
    			return "partners list not populated";
    		else
    			return "partner ID [" + partnerID + "] not found";
    	}
    	else
    		return partner.getPartnerWebURL();
    }

}
