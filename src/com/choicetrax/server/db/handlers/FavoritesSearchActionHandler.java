package com.choicetrax.server.db.handlers;

import java.util.Calendar;

import com.choicetrax.client.actions.*;
import com.choicetrax.client.actions.handleractions.FavoritesSearchAction;
import com.choicetrax.client.actions.loaderactions.*;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.data.DJChartsSearchActionXML;
import com.choicetrax.server.data.ReleasesBrowseActionXML;
import com.choicetrax.server.data.ReleasesSearchActionXML;
import com.choicetrax.server.db.handlers.BaseActionHandler;
import com.choicetrax.server.util.jaxb.JAXBHelper;


public class FavoritesSearchActionHandler
	extends BaseActionHandler
{
	
	private FavoritesSearchAction action = null;
	
	
	public FavoritesSearchActionHandler( HandlerAction handlerAction )
	{
		super( handlerAction );
		
		this.action = (FavoritesSearchAction) handlerAction;
	}
	
		
	protected String createRemoveSQL()
	{
		String sql = "update UserFavoriteSearches "
					+ "set DateRemoved = sysdate() "
					+ "where UserID = " + this.action.getUserID() + " "
					+	"and SearchName = '" + ServerConstants.escapeChars( this.action.getSearchName() ) + "' ";
		
		return sql;
	}
	
		
	protected String createAddSQL()
	{
		String searchXML = null;
		
		try
		{
			LoaderAction actionObj = action.getActionObj();
			if ( actionObj instanceof ReleasesSearchAdvancedAction )
			{
				searchXML = createSearchActionXML( (ReleasesSearchAdvancedAction) actionObj );
			}
			else if ( actionObj instanceof ReleasesBrowseAction )
			{
				searchXML = createBrowseActionXML( (ReleasesBrowseAction) actionObj );
			}
			else if ( actionObj instanceof DJChartsSearchAction )
			{
				searchXML = createDJChartsSearchActionXML( (DJChartsSearchAction) actionObj );
			}
		}
		catch ( Exception e )
		{
			response.setResponseCode( Constants.ACTION_RESPONSE_ERROR );
			response.setResponseText( "FavoritesSearchAction error creating XML: " + e );
		}
		
		String sql = "insert into UserFavoriteSearches "
					+ 	"( UserID, SearchName, SearchXML, DateAdded, DateRemoved ) "
					+ "values ( " 
					+ 	this.action.getUserID() + ", " 
					+ 	" '" + ServerConstants.escapeChars( this.action.getSearchName() ) + "', "
					+	" '" + searchXML + "', "
					+	"sysdate(), "
					+	"null ) " 
					+ "on duplicate key update "
					+	"DateRemoved = null, "
					+	"SearchXML = '" + searchXML + "' ";
		
		return sql;
	}
	
	
	private String createSearchActionXML( ReleasesSearchAdvancedAction searchAction )
		throws ChoicetraxException
	{
		ReleasesSearchActionXML actionXML = JAXBHelper.jaxbFactory.createReleasesSearchActionXML();
		
		actionXML.setUserID( searchAction.getUserID() );
		
		String artistName = ServerConstants.escapeChars( searchAction.getArtistName() );
		if ( artistName != null )
			actionXML.setArtistName( artistName	);
		
		String labelName = ServerConstants.escapeChars( searchAction.getLabelName() );
		if ( labelName != null )
			actionXML.setLabelName( labelName );
		
		String trackName = ServerConstants.escapeChars( searchAction.getTrackName() );
		if ( trackName != null )
			actionXML.setTrackName( trackName );
		
		if ( searchAction.getGenreIDs() != null )
			actionXML.setGenreIDs( 	ServerConstants.convertIntArrayToString( searchAction.getGenreIDs() ) );
		
		if ( searchAction.getPartnerIDs() != null )
			actionXML.setPartnerIDs( ServerConstants.convertIntArrayToString( searchAction.getPartnerIDs() ) );
		
		if ( searchAction.getReleaseDateBegin() != null )
		{
			Calendar beginDT = Calendar.getInstance();
			beginDT.setTime( searchAction.getReleaseDateBegin() );
			actionXML.setReleaseDateBegin( beginDT );
		}
		
		if ( searchAction.getReleaseDateEnd() != null )
		{
			Calendar endDT = Calendar.getInstance();
			endDT.setTime( searchAction.getReleaseDateEnd() );
			actionXML.setReleaseDateEnd( endDT );
		}
		
		return JAXBHelper.convertToXML( actionXML );
	}
	
	
	private String createBrowseActionXML( ReleasesBrowseAction browseAction )
		throws ChoicetraxException
	{
		ReleasesBrowseActionXML actionXML = JAXBHelper.jaxbFactory.createReleasesBrowseActionXML();
		
		actionXML.setUserID( browseAction.getUserID() );
		actionXML.setOnlyExclusive( browseAction.isOnlyExclusive() );
		
		if ( browseAction.getRecentDate() != null )
			actionXML.setRecentDate( browseAction.getRecentDate() );
		
		if ( browseAction.getGenreIDs() != null )
			actionXML.setGenreIDs( ServerConstants.convertIntArrayToString( browseAction.getGenreIDs() ) );
		
		if ( browseAction.getPartnerIDs() != null )
			actionXML.setPartnerIDs( ServerConstants.convertIntArrayToString( browseAction.getPartnerIDs() ) );
				
		return JAXBHelper.convertToXML( actionXML );
	}
	
	
	private String createDJChartsSearchActionXML( DJChartsSearchAction chartAction )
		throws ChoicetraxException
	{
		DJChartsSearchActionXML actionXML = JAXBHelper.jaxbFactory.createDJChartsSearchActionXML();
		
		actionXML.setUserID( chartAction.getUserID() );
		
		if ( chartAction.getSearchTerms() != null )
			actionXML.setSearchTerms( chartAction.getSearchTerms() );
		
		if ( chartAction.getRecentDate() != null ) 
			actionXML.setDate( chartAction.getRecentDate() );
		
		if ( chartAction.getGenreIDs() != null )
			actionXML.setGenreIDs( ServerConstants.convertIntArrayToString( chartAction.getGenreIDs() ) );
		
		if ( chartAction.getPartnerIDs() != null )
			actionXML.setPartnerIDs( ServerConstants.convertIntArrayToString( chartAction.getPartnerIDs() ) );
		
		return JAXBHelper.convertToXML( actionXML );
	}
	
}