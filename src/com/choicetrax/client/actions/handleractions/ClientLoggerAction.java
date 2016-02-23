package com.choicetrax.client.actions.handleractions;

import java.util.LinkedList;

import com.choicetrax.client.actions.HandlerAction;


public class ClientLoggerAction 
	implements HandlerAction
{
	
	private String browserName = null;
	private LinkedList<String> actions = null;
	private String exceptionText = null;
	private int userID = -1;
	private String username = null;
	private String ipAddress = null;
	
	
	
	public ClientLoggerAction() {
		super();
	}
	
	
	public String getLogString() {
		return "ClientLoggerAction";
	}

		
	public String getActionType() {
		return null;
	}
	
	
	public void setBrowserName( String browser ) {
		this.browserName = browser;
	}
	
	public String getBrowserName() {
		return browserName;
	}	
	
	public void setExceptionText( String text ) {
		this.exceptionText = text;
	}
	
	public String getExceptionText() {
		return exceptionText;
	}
	
	public void setUserID( int userID ) {
		this.userID = userID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUsername( String username ) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}


	
	public LinkedList<String> getActions() {
		return this.actions;
	}


	
	public void setActions( LinkedList<String> actions ) {
		this.actions = actions;
	}


	
	public String getIpAddress() {
		return this.ipAddress;
	}


	
	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}

}
