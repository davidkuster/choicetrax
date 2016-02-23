package com.choicetrax.client.actions.responses;

public class ActionHandlerMessage implements ActionResponse
{
	private int responseCode;
    private String responseText;
    
    
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

}
