package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.util.LinkedList;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.Partner;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.data.config.Countries;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class ChoicetraxConfigDataLoader 
{

	
	
	public ChoicetraxConfigData loadConfigData()
		throws ChoicetraxException 
	{
		ChoicetraxConfigData configData = new ChoicetraxConfigData();
		
		configData.setChoicetraxGenres( 	loadGenreData() 	);
		configData.setChoicetraxPartners( 	loadPartnerData() 	);
		configData.setCountries( 			loadCountriesData()	);
		
		return configData;
	}
	
	
	private Countries loadCountriesData() 
		throws ChoicetraxException
	{
		CountryLoader loader = new CountryLoader();
		return (Countries) loader.loadData( null );
	}
	
	
	/*
	 * loads genre data
	 */
	private LinkedList<Genre> loadGenreData() 
		throws ChoicetraxException
	{
		String methodName = "loadGenreData()";
		
		String genreSQL = "select * from Genres "
						+ "order by GenreName";
		
		LinkedList<Genre> genreList = new LinkedList<Genre>();
		
		DBResource dbHandle = null;
    	
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( genreSQL );
    		while ( rs.next() )
    		{
    			Genre genre = new Genre();
    			
    			int genreID 	 = rs.getInt( 	 "GenreID" 			);
    			String genreName = rs.getString( "GenreName" 		);
    			String shortName = rs.getString( "GenreShortName" 	);
    			
    			genre.setGenreID( genreID );
    			genre.setGenreName( genreName );
    			genre.setGenreShortName( shortName );
    			
    			genreList.add( genre );
    		}
    		rs.close();
    		
    		//Genre emptyGenre = new Genre( -1, "" );
    		//genreList.add( 0, emptyGenre );
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error getting genre config data: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}

		return genreList;
	}
	
	
	/*
	 * loads partner data
	 */
	private LinkedList<Partner> loadPartnerData() 
		throws ChoicetraxException
	{
		String methodName = "loadPartnerData()";
		
		String partnerSQL = "select PartnerID, PartnerName, "
							+ 	"PartnerWebURL, IconFilename, "
							+ 	"DefaultCurrency "
							+ "from PartnerSites "
							+ "where PartnerOrdering > 0 "
							+ "order by PartnerOrdering";
		
		LinkedList<Partner> partnerList = new LinkedList<Partner>();
		
		DBResource dbHandle = null;
    	
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( partnerSQL );
    		while ( rs.next() )
    		{
    			Partner partner = new Partner();
    			
    			int partnerID 		= rs.getInt( 	"PartnerID" 	);
    			String partnerName 	= rs.getString( "PartnerName" 	);
    			String webURL		= rs.getString(	"PartnerWebURL"	);
    			String iconFilename	= rs.getString(	"IconFilename"	);
    			String defCurrency	= rs.getString( "DefaultCurrency" );
    			
    			String currency = null;
    			if ( "US_Dollar".equals( defCurrency ) )
    				currency = Constants.CURRENCY_US;
    			else if ( "British_Pound".equals( defCurrency ) )
    				currency = Constants.CURRENCY_BRITISH;
    			
    			partner.setPartnerID( partnerID );
    			partner.setPartnerName( partnerName );
    			partner.setPartnerWebURL( webURL );
    			partner.setPartnerIconFilename( iconFilename );
    			partner.setCurrency( currency );
    			
    			partnerList.add( partner );
    		}
    		rs.close();
    		
    		/*Partner emptyPartner = new Partner();
    		emptyPartner.setPartnerID( -1 );
    		emptyPartner.setPartnerName( "" );
    		partnerList.add( 0, emptyPartner );*/
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error getting partner config data: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}

		return partnerList;
	}

}
