package com.choicetrax.client.logic.handlers.buy;


public interface BuyHandler 
{
		
	public void login();
	
	public void transfer();
	
	public void transferComplete();
	
	
	public String getCartURL();
	
	public String getPartnerWindowName();

}
