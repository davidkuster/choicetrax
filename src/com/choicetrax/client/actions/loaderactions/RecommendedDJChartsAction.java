package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderHistoryAction;


public class RecommendedDJChartsAction extends LoaderHistoryAction
{
	
	private String recommendationType = null;
	private int userID = -1;
	
	private static final String PREFIX_RECOMMENDED	= "recommended(";
    //private static final String PREFIX_USER		= "i("; // i for id 
    														// misdirection
    
    
    public RecommendedDJChartsAction() { 
    	super();
    }
    
    
    public RecommendedDJChartsAction( String actionString )
    {
    	super( actionString );
		
		if ( actionString == null ) return;
	
		this.recommendationType = readEncodedValue( actionString, PREFIX_RECOMMENDED );

		//String uID = readEncodedValue( actionString, PREFIX_USER );
		//if ( uID != null )
		//	this.userID = Integer.parseInt( uID );
    }
	
    
    
    public String getLogString() 
	{
		StringBuffer log = new StringBuffer();
		if ( userID != -1 )
			log.append( "userID [" + userID + "] " );
		
		return "RecommendedDJChartsAction: " + log.toString();
	}
    
    

    public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append( PREFIX_RECOMMENDED + recommendationType + DELIMITER );
		
		//if ( userID > 0 )
		//	sb.append( PREFIX_USER + userID + DELIMITER );
		
		sb.append( super.getSortActionString() );
		
		return sb.toString();
	}

	
	public void setUserID( int userID ) {
		this.userID = userID;
	}
	
	public int getUserID() {
		return userID;
	}

	
	public String getRecommendationType() {
		return this.recommendationType;
	}
	
	public void setRecommendationType( String type ) {
		this.recommendationType = type;
	}
	
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
				
		sb.append( "type [" + recommendationType + "] " );
		
		if ( userID > 0 )
			sb.append( "userID [" + userID + "] " );
		
		return sb.toString();
	}

}
