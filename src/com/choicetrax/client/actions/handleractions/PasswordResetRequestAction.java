package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.EmailLinkHandlerAction;


public class PasswordResetRequestAction implements EmailLinkHandlerAction
{
	
	private String actionType	= null;
	
	private String emailAddress = null;
	private String timestamp	= null;
	
	
	public PasswordResetRequestAction() {
		super();
	}
	
	
	public String getActionType() {
		return actionType;
	}
	public void setActionType( String type ) {
		this.actionType = type;
	}
	

	public String getLogString() {
		return "PasswordResetAction: "
				+ "email addr [" + emailAddress + "]";
	}
	
	
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress( String emailAddr ) {
		this.emailAddress = emailAddr;
	}

	
	public String getTimestamp() {
		return this.timestamp;
	}
	public void setTimestamp( String timestamp ) {
		this.timestamp = timestamp;
	}

}
