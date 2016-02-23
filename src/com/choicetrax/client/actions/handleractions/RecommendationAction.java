package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class RecommendationAction 
	implements HandlerAction
{
	
    private int userID;
    private String actionType;
    private int trackID;
    
    
    public RecommendationAction() {
    	super();
    }
    
    
    public String getLogString() {
		return "RecommendationAction: "
				+ "userID [" + userID + "] "
				+ "trackID [" + trackID + "] "
				+ "action [" + actionType + "]";
	}
    
    
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public int getTrackID() {
		return trackID;
	}
	public void setTrackID(int trackID) {
		this.trackID = trackID;
	}

}