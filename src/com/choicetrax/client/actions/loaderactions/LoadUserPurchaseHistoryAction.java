package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;


public class LoadUserPurchaseHistoryAction 
	implements LoaderAction 
{
	
	private int userID = -1;
	
	
	public LoadUserPurchaseHistoryAction() {
		super();
	}
	
	
	public String getLogString() {
		return "LoadUserPurchaseHistoryAction: "
				+ "userID [" + userID + "]";
	}


	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
