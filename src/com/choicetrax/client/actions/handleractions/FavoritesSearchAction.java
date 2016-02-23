package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.LoaderAction;


public class FavoritesSearchAction 
	implements HandlerAction 
{
	
	private int userID;
	private String searchName;
	
	// ClientConstants.ACTION_ADD, ACTION_REMOVE
	private String actionType; 
	
	// ReleasesSearchAction, ReleasesBrowseAction, etc
	private LoaderAction actionObj;
	
	
	
	public FavoritesSearchAction() {
		super();
	}
	
	
	public String getLogString() {
		return "FavoritesSearchAction: "
				+ "userID [" + userID + "] "
				+ "search name [" + searchName + "] "
				+ "action [" + actionType + "]";
	}
	
	
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getSearchName() {
		return searchName;
	}
	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public LoaderAction getActionObj() {
		return actionObj;
	}
	public void setActionObj(LoaderAction actionObj) {
		this.actionObj = actionObj;
	} 

}
