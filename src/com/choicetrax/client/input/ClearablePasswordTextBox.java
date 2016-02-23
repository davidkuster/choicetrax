package com.choicetrax.client.input;

import com.google.gwt.user.client.ui.PasswordTextBox;

import com.choicetrax.client.input.listeners.ClearableInputListener;


public class ClearablePasswordTextBox 
	extends PasswordTextBox
	implements ClearableInputBox
{
	
	private String initialText = null;
	
	
	public ClearablePasswordTextBox( String initialText ) {
		super();
		
		this.initialText = initialText;
		this.setText( initialText );
		
		ClearableInputListener handler = new ClearableInputListener();
		
		this.addFocusHandler( handler );
		this.addBlurHandler( handler );
	}
	
	
	public String getInitialText() {
		return initialText;
	}

}
