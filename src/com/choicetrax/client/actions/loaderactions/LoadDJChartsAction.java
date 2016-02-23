package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;


public class LoadDJChartsAction 
	implements LoaderAction
{
	
	private int userID = 0;
	private int[] chartIDs = null;
	
	
	public LoadDJChartsAction() {
		super();
	}
	
	
	public String getLogString() {
		return "LoadDJChartsAction: "
				+ "userID [" + userID + "] "
				+ "chartIDs [" + chartIDs + "]";
	}
		
	
	public LoadDJChartsAction( int[] ids )
	{
		this.chartIDs = ids;
	}
	
	
	public int[] getChartIDs()
	{
		return chartIDs;
	}
	
	public void setUserID( int id ) {
		this.userID = id;
	}
	
	public int getUserID() {
		return userID;
	}
	
	
}
