package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.util.Calendar;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.loaderactions.DJChartsSearchAction;
import com.choicetrax.client.actions.loaderactions.ReleasesBrowseAction;
import com.choicetrax.client.actions.loaderactions.ReleasesSearchAdvancedAction;
import com.choicetrax.client.actions.loaderactions.UserAction;
import com.choicetrax.client.data.*;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.util.jdbc.*;
import com.choicetrax.server.util.jaxb.JAXBHelper;
import com.choicetrax.server.data.DJChartsSearchActionXML;
import com.choicetrax.server.data.ReleasesBrowseActionXML;
import com.choicetrax.server.data.ReleasesSearchActionXML;
import com.choicetrax.server.constants.ServerConstants;


public class UserFavoritesLoader
{
	
	/**
	 * gets favorites releases by type - artist, label, genre, etc
	 * 
	 * @param requestObj
	 * @return
	 * @throws ChoicetraxException
	 */
    public void loadData( LoaderAction loaderAction ) 
    	throws ChoicetraxException
    {
    	UserAction userAction = (UserAction) loaderAction;
    	User user = userAction.getUserObj();
    	
    	if ( user != null )
    	{    	
	    	loadFavorites( user );
	    	
	    	loadFavoriteSearches( user );
    	}
    }
    
    
    private void loadFavorites( User user )
    	throws ChoicetraxException
    {
    	String methodName = "loadFavorites()";
    	
    	String sql = createSQL( user.getUserID() );
    	
    	DBResource dbHandle = null;

    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		while ( rs.next() )
    		{
				int id 		= rs.getInt( 	"ID" 	);
				String name = rs.getString( "Name" 	);
				String type = rs.getString( "Type" 	);
				
				if ( "Artist".equals( type ) )
				{
					Artist favArtist = new Artist();
					favArtist.setArtistID( id );
					favArtist.setArtistName( name );
					
					user.addFavorite( favArtist, false );
				}
				else if ( "Label".equals( type ) )
				{
					RecordLabel favLabel = new RecordLabel();
					favLabel.setLabelID( id );
					favLabel.setLabelName( name );
					
					user.addFavorite( favLabel, false );
				}
				else if ( "Genre".equals( type ) )
				{
					Genre favGenre = new Genre();
					favGenre.setGenreID( id );
					favGenre.setGenreName( name );
					
					user.addFavorite( favGenre, false );
				}
    		}
    		    		
    		rs.close();
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error loading user favorites [Artist/Label/Genre]: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}
    }
    
    
    private void loadFavoriteSearches( User user )
    	throws ChoicetraxException
    {
    	String methodName = "loadFavoriteSearches()";
    	
    	String sql = "select SearchName, SearchXML "
    				+ "from UserFavoriteSearches "
    				+ "where UserID = " + user.getUserID() + " "
    				+	"and DateRemoved is null";
    	
    	DBResource dbHandle = null;

    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		while ( rs.next() )
    		{
				String searchName 	= rs.getString( "SearchName" );
				String searchXML	= rs.getString( "SearchXML"	 ); 
				
				LoaderHistoryAction loaderAction = null;
				
				// would it be better performance to load all the XML
				// from the database and then convert it to java objects ???
				//
				// would probably keep the DB connection open for a
				// shorter period of time...
				
				Object xmlObj = JAXBHelper.convertFromXML( searchXML );
				if ( xmlObj instanceof ReleasesSearchActionXML )
				{
					loaderAction = createSearchAction( (ReleasesSearchActionXML) xmlObj );
				}
				else if ( xmlObj instanceof ReleasesBrowseActionXML )
				{
					loaderAction = createBrowseAction( (ReleasesBrowseActionXML) xmlObj );
				}
				else if ( xmlObj instanceof DJChartsSearchActionXML )
				{
					loaderAction = createDJChartsAction( (DJChartsSearchActionXML) xmlObj );
				}
				
				user.addFavoriteSearch( searchName, loaderAction );
    		}    		    		
    		rs.close();
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error loading user favorites [searches]: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}
    }
    
    
    /*
     * creates SQL to do searching.  includes logic on how searches are done,
     * with like/% or = or whatever.
     */
    protected String createSQL( int userID )
    {
    	String sql = "select fa.ArtistID as ID, a.ArtistName as Name, 'Artist' as Type "
					+ "from UserFavoriteArtists fa, Artists a "
					+ "where fa.ArtistID = a.ArtistID "
					+ 	"and fa.DateRemoved is null "
					+	"and fa.UserID = " + userID + " "
					
    	 			+ "union "
    	 			
    				+ "select fg.GenreID as ID, g.GenreName as Name, 'Genre' as Type "
	    			+ "from UserFavoriteGenres fg, Genres g "
	    			+ "where fg.GenreID = g.GenreID "
	    			+	"and fg.DateRemoved is null "
	    			+	"and fg.UserID = " + userID + " "
	    			
	    			+ "union "
	    			
	    			+ "select fl.LabelID as ID, l.LabelName as Name, 'Label' as Type "
	    			+ "from UserFavoriteLabels fl, Labels l "
	    			+ "where fl.LabelID = l.LabelID "
	    			+	"and fl.DateRemoved is null "
	    			+	"and fl.UserID = " + userID + " "
	    			
	    			+ "order by Name ";
    	
       	return sql;
    }
    
    
    private LoaderHistoryAction createSearchAction( ReleasesSearchActionXML actionXML )
    {
    	ReleasesSearchAdvancedAction action = new ReleasesSearchAdvancedAction();
    	
    	action.setUserID(		actionXML.getUserID()		);
    	action.setArtistName( 	actionXML.getArtistName()	);
    	action.setLabelName( 	actionXML.getLabelName() 	);
    	action.setTrackName( 	actionXML.getTrackName() 	);
    	
    	String genreIDs = actionXML.getGenreIDs();
    	if ( genreIDs != null )
    		action.setGenreIDs( ServerConstants.convertStringToIntArray( genreIDs ) );
    	
    	String partnerIDs = actionXML.getPartnerIDs();
    	if ( partnerIDs != null )
    		action.setPartnerIDs( ServerConstants.convertStringToIntArray( partnerIDs ) );
    	
    	Calendar beginDT = actionXML.getReleaseDateBegin();
    	if ( beginDT != null )
    		action.setReleaseDateBegin( beginDT.getTime() );
    	
    	Calendar endDT = actionXML.getReleaseDateEnd();
    	if ( endDT != null )
    		action.setReleaseDateEnd( endDT.getTime() );
    	
    	return action;
    }
    
    private LoaderHistoryAction createBrowseAction( ReleasesBrowseActionXML actionXML )
    {
    	ReleasesBrowseAction action = new ReleasesBrowseAction();
    	
    	action.setUserID(		actionXML.getUserID()		);
    	action.setRecentDate(	actionXML.getRecentDate() 	);
    	
    	String genreIDs = actionXML.getGenreIDs();
    	if ( genreIDs != null )
    		action.setGenreIDs( ServerConstants.convertStringToIntArray( genreIDs ) );
    	
    	Boolean exclusive = actionXML.isOnlyExclusive();
    	if ( exclusive != null )
    		action.setOnlyExclusive( exclusive.booleanValue() );
    	
    	String partnerIDs = actionXML.getPartnerIDs();
    	if ( partnerIDs != null )
    		action.setPartnerIDs( ServerConstants.convertStringToIntArray( partnerIDs ) );
    	
    	return action;
    }
    
    private LoaderHistoryAction createDJChartsAction( DJChartsSearchActionXML actionXML )
    {
    	DJChartsSearchAction action = new DJChartsSearchAction();
    	
    	action.setUserID(		actionXML.getUserID()	);
    	action.setRecentDate(	actionXML.getDate() 	);
    	
    	String genreIDs = actionXML.getGenreIDs();
    	if ( genreIDs != null )
    		action.setGenreIDs( ServerConstants.convertStringToIntArray( genreIDs ) );
    	
    	String partnerIDs = actionXML.getPartnerIDs();
    	if ( partnerIDs != null )
    		action.setPartnerIDs( ServerConstants.convertStringToIntArray( partnerIDs ) );
    	
    	return action;
    }

}