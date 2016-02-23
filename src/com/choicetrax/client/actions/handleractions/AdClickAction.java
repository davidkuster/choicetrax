package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.UserIpAction;

public class AdClickAction 
	implements HandlerAction, UserIpAction
{
	
	private String actionType = null;
	private int userID = -1;
	private int adID = -1;
	private String ipAddress = null;
	
	
	
	public AdClickAction() {
		super();
	}

	
	public String getLogString() {
		return "AdClickAction: "
				+ "userID [" + userID + "] "
				+ "adID [" + adID + "]";
	}
	
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getAdID() {
		return adID;
	}

	public void setAdID(int adID) {
		this.adID = adID;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}


	
	public String getIpAddress() {
		return this.ipAddress;
	}


	
	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}

}
