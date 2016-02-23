package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.UserIpAction;


public class FeedbackAction 
	implements HandlerAction, UserIpAction
{
	
	private int userID = -1;
	private String ipAddress = null;
	
	private String name = null;
	private String emailAddress = null;
	private String feedbackSubject = null;
	private String feedbackText = null;
	
	private String actionType = null;
	
	

	public FeedbackAction() { 
		super();
	}
	
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType( String actionType ) {
		this.actionType = actionType;
	}

	
	public String getLogString() {
		return "FeedbackAction: "
				+ "name [" + name + "] "
				+ "email [" + emailAddress + "] "
				+ "subject [" + feedbackSubject + "] "
				+ "text [" + feedbackText + "] ";
	}


	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress( String ipAddr ) {
		this.ipAddress = ipAddr;
	}
	public int getUserID() {
		return this.userID;
	}
	public void setUserID( int userID ) {
		this.userID = userID;
	}


	
	public String getName() {
		return this.name;
	}


	
	public void setName( String name ) {
		this.name = name;
	}


	
	public String getEmailAddress() {
		return this.emailAddress;
	}


	
	public void setEmailAddress( String emailAddress ) {
		this.emailAddress = emailAddress;
	}


	
	public String getFeedbackSubject() {
		return this.feedbackSubject;
	}


	
	public void setFeedbackSubject( String feedbackSubject ) {
		this.feedbackSubject = feedbackSubject;
	}


	
	public String getFeedbackText() {
		return this.feedbackText;
	}


	
	public void setFeedbackText( String feedbackText ) {
		this.feedbackText = feedbackText;
	}

}
