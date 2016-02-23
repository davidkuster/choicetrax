package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.LoadDJChartsAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.Artist;
import com.choicetrax.client.data.DJChartDetail;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class DJChartsLoader implements DataLoader
{
	
	private static SimpleDateFormat formatter	= new SimpleDateFormat( "MMM dd yyyy" );
	

	
	public LoaderResponse loadData( LoaderAction action ) 
		throws ChoicetraxException 
	{
		LoadDJChartsAction requestObj = (LoadDJChartsAction) action;
		
		// initialize cache
		int[] chartIDs = requestObj.getChartIDs();
		
		DJCharts charts = loadCharts( chartIDs );
		
        return charts;
	}
	
	
	protected DJCharts loadCharts( int[] chartIDs ) 
		throws ChoicetraxException
	{
		DJCharts chartsList = new DJCharts();
		
		String methodName = "loadCharts()";
		
		// if no chartIDs are passed in, skip this method
		if ( ( chartIDs == null ) || chartIDs.length == 0 ) 
			return chartsList;
		
		String ids = ServerConstants.convertIntArrayToString( chartIDs );
		String sql = "select ChartID, ChartName, ChartDate, PartnerID, ArtistID, "
					+ 	"ArtistName, ImageURL, ChartGenreIDs "
					+ "from ChartsDJs "
					+ "where ChartID in ( " + ids + " ) "; 
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
			
			ResultSet rs = dbHandle.executeQuery( sql );
			while ( rs.next() )
			{
				int chartID = rs.getInt( "ChartID" );
				String chartName = rs.getString( "ChartName" );
				int partnerID = rs.getInt( "PartnerID" ) ;
				int artistID = rs.getInt( "ArtistID" );
				String artistName = rs.getString( "ArtistName" );
				String imageURL = rs.getString( "ImageURL" );
				String genreIDs = rs.getString( "ChartGenreIDs" );
				
				String chartDate = null;
				java.sql.Date chartDT = rs.getDate( "ChartDate" );
				if ( chartDT != null )
					chartDate = formatter.format( chartDT );
				
				DJChartDetail chart = new DJChartDetail();
				chart.setChartID( chartID );
				chart.setChartName( chartName );
				chart.setChartDate( chartDate );
				chart.setImageURL( imageURL );
				chart.setPartnerID( partnerID );
				
				if ( artistID > 0 ) {
					Artist artist = new Artist();
					artist.setArtistID( artistID );
					artist.setArtistName( artistName );
					
					chart.setArtist( artist );
				}
				else if ( artistName != null ) 
					chart.setArtistName( artistName );
				
				if ( genreIDs != null ) 
					chart.setGenres( createGenreList( genreIDs ) );				
				
				chartsList.addChart( chart );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error executing charts load: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
	
		return chartsList;
	}
	
	
	private LinkedList<Genre> createGenreList( String genreIDs )
	{
		LinkedList<Genre> genreList = new LinkedList<Genre>();
		
		if ( genreIDs != null )
		{
			StringTokenizer ids = new StringTokenizer( genreIDs, "," );
			while ( ids.hasMoreTokens() )
			{
				Genre genre = new Genre();
				genre.setGenreID( Integer.parseInt( ids.nextToken() ) );
				
				genreList.add( genre );
			}
		}
		
		return genreList;
	}

}
