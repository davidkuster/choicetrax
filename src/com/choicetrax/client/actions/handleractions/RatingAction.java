package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;

public class RatingAction 
	implements HandlerAction 
{
	
	private int userID;
	private int trackID;
	private int rating;
	private String actionType;
	
	
	public RatingAction() {
		super();
	}
	
	
	public String getLogString() {
		return "RatingAction: "
				+ "userID [" + userID + "] "
				+ "trackID [" + trackID + "] "
				+ "rating [" + rating + "] "
				+ "action [" + actionType + "]";
	}
	
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getTrackID() {
		return trackID;
	}
	public void setTrackID(int trackID) {
		this.trackID = trackID;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

}
