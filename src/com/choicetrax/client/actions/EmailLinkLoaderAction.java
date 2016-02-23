package com.choicetrax.client.actions;


public interface EmailLinkLoaderAction extends LoaderAction
{
	
	public void setEmailAddress( String emailAddr );
	
	public String getEmailAddress();
	
	
	public void setTimestamp( String timestamp );
	
	public String getTimestamp();

}
