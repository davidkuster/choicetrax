package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.config.Countries;
import com.choicetrax.client.data.config.Country;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.util.jdbc.*;
import com.choicetrax.server.db.DataLoader;


public class CountryLoader 
	implements DataLoader
{

	public CountryLoader() {
		super();
	}
	
	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		return loadCountries();
	}
	
		
	private Countries loadCountries()
		throws ChoicetraxException
	{
		String methodName = "loadCountries()";
		
		String sql = "select CountryID, CountryCode, CountryName "
					+ "from Countries "
					+ "order by CountryName";
		
		DBResource dbHandle = null;
		
		Countries list = new Countries();
		
		try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		while ( rs.next() )
    		{
    			Country country = new Country();
		    	country.setCountryID( rs.getInt( "CountryID" ) );
		    	country.setCountryCode( rs.getString( "CountryCode" ) );
		    	country.setCountryName( rs.getString( "CountryName" ) );
		    	
		    	list.addCountry( country );
    		}
    		rs.close();
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error loading country list: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}

		return list;		
	}
				
}
