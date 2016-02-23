package com.choicetrax.client.actions;


public interface EmailLinkHandlerAction extends HandlerAction
{
	
	public void setEmailAddress( String emailAddr );
	
	public String getEmailAddress();
	
	
	//public void setUniqueHash( String hash );
	
	//public String getUniqueHash();

}
