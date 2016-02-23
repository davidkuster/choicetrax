package com.choicetrax.client.input;

import com.google.gwt.user.client.ui.TextBox;

import com.choicetrax.client.input.listeners.ClearableInputListener;


public class ClearableTextBox 
	extends TextBox
	implements ClearableInputBox
{
	
	private String initialText = null;
	
	
	public ClearableTextBox( String initialText ) {
		super();
		
		this.initialText = initialText;
		this.setText( initialText );
		
		ClearableInputListener listener = new ClearableInputListener();
		
		this.addFocusHandler( listener );
		this.addBlurHandler( listener );
	}
	
	
	
	public String getInitialText() {
		return initialText;
	}

}
