package com.choicetrax.server.db.loaders;

import com.choicetrax.client.actions.loaderactions.ViewAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.constants.ServerConstants;


public class RatedReleasesLoader extends BaseReleasesLoader 
{
	
	private ViewAction requestObj = null;
	
	public RatedReleasesLoader() {
		super();
	}
	
	
	public Releases getRated( ViewAction requestObj ) throws ChoicetraxException
	{
		this.requestObj = requestObj;
		
		return loadReleases(getTrackIDs() );
	}

	/*
     * creates SQL to do searching.  includes logic on how searches are done,
     * with like/% or = or whatever.
     */
    protected String createTrackIdSQL()
    {
    	int genreID					= requestObj.getGenreID(); 
    	java.util.Date beginDate	= requestObj.getDateRangeBegin(); 
    	java.util.Date endDate		= requestObj.getDateRangeEnd(); 
    	int partnerID				= requestObj.getPartnerID();
    	int beginIndex				= requestObj.getBeginIndex();
    	int userID					= requestObj.getUserID();
    	
    	
    	String subquerySelect 		= "select distinct t1.TrackID ";
    	StringBuffer subqueryFrom 	= new StringBuffer();
    	StringBuffer subqueryWhere	= new StringBuffer();
    	
    	subqueryFrom.append( "from Tracks t1, "
    						+ "UserRatedTracks ut1 " );
    	
		subqueryWhere.append( "where t1.TrackID = ut1.TrackID " 
							+ 	"and ut1.UserID = " + userID + " " );
		
		if ( beginDate != null )
    	{
    		subqueryWhere.append( "and t1.ReleaseDate >= '" + ServerConstants.DATE_DB_FORMAT.format( beginDate ) + "' " ); 
    	}
    	
    	if ( endDate != null )
    	{
    		subqueryWhere.append( "and t1.ReleaseDate <= '" + ServerConstants.DATE_DB_FORMAT.format( endDate ) + "' " );
    	}
    	
    	if ( genreID > 0 )
    	{
    		subqueryFrom.append( ", TrackGenres tg1 " );
    		subqueryWhere.append( "and t1.TrackID = tg1.TrackID " );
    		subqueryWhere.append( "and tg1.GenreID = " + genreID + " " );
    	}
    	
    	// need to implement release dates
    	
    	if ( partnerID > 0 )
    	{
    		subqueryFrom.append( ", PartnerInventory pi1 ");
    		subqueryWhere.append( "and t1.TrackID = pi1.TrackID " );
    		subqueryWhere.append( "and pi1.PartnerID = " + partnerID + " " );
    	}
    	
    	// add limit
    	subqueryWhere.append( "limit " + beginIndex + ", " + Constants.NUM_RELEASES_PER_PAGE );
    	
    	
    	String subquery = subquerySelect + subqueryFrom + subqueryWhere;
    	
    	return subquery;
    }

}
