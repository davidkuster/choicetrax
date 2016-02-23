package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;

public class BetaUserLoginAction 
	implements HandlerAction 
{

	private String actionType = null;
	private String username = null;
	private String password = null;
	
	
	public BetaUserLoginAction() {
		super();
	}
	
	
	public String getLogString() {
		return "BetaUserLoginAction: "
				+ "username [" + username + "]";
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getActionType() {
		return actionType;
	}

}
