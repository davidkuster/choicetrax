package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.UserIpAction;


public class ViewDJChartAction 
	extends LoaderHistoryAction
	implements UserIpAction
{
	
	private int chartID = -1;
	private int userID = -1;
	private String ipAddress = null;
	
	private static final String PREFIX_CHART_ID		= "chart(";
	private static final String PREFIX_USER			= "i(";
	
	
	public ViewDJChartAction() {
		super();
	}
	
	public ViewDJChartAction( String actionString ) 
	{
		super( actionString );
		
		if ( actionString == null ) return;
		
		String id = readEncodedValue( actionString, PREFIX_CHART_ID );
		if ( id != null )
			chartID = Integer.parseInt( id );
		
		String user = readEncodedValue( actionString, PREFIX_USER );
		if ( user != null )
			userID = Integer.parseInt( user );
	}
	
	
	public String getLogString() 
	{
		return "ViewDJChartAction: " 
				+ "userID [" + userID + "]"
				+ "chartID [" + chartID + "] ";
	}
	
	
	public String getActionString()
	{
		StringBuffer sb = new StringBuffer();
		
		if ( chartID > 0 )
			sb.append( PREFIX_CHART_ID + chartID + DELIMITER );
						
		//if ( userID > 0 )
		//	sb.append( PREFIX_USER + userID + DELIMITER );
		
		sb.append( super.getSortActionString() );
		
		return sb.toString();
	}


	
	public int getChartID() {
		return this.chartID;
	}
	
	public void setChartID( int chartID ) {
		this.chartID = chartID;
	}



	public int getUserID() {
		return this.userID;
	}
	
	public void setUserID( int userID ) {
		this.userID = userID;
	}

	
	public String getIpAddress() {
		return this.ipAddress;
	}

	
	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}

}
