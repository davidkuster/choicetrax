package com.choicetrax.client.actions.handleractions;

import java.util.List;
import java.util.ArrayList;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.UserIpAction;


public class AdImpressionAction 
	implements HandlerAction, UserIpAction 
{
	
	private String actionType = null;
	private List<Integer> adIdList = null;
	private int userID = -1;
	private String ipAddress =null;
	
	
	
	public AdImpressionAction() {
		super();
		adIdList = new ArrayList<Integer>();
	}
	
	public AdImpressionAction( List<Integer> adIdList ) {
		super();
		this.adIdList = adIdList;
	}
	
	
	public String getLogString() {
		return "AdImpressionAction: " 
				+ "adIdList [" + adIdList + "]";
	}

	
	public String getActionType() {
		return actionType;
	}
	
	public void setActionType( String actionType ) {
		this.actionType = actionType;
	}
	
	public void addAdID( int adID ) {
		adIdList.add( new Integer( adID ) );
	}
	
	public List<Integer> getAdIdList() {
		return adIdList;
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
