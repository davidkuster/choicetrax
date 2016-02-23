package com.choicetrax.client.input.listeners;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;

import com.choicetrax.client.input.ClearableInputBox;


public class ClearableInputListener implements FocusHandler, BlurHandler
{
	
	public void onFocus( FocusEvent event ) 
	{
		ClearableInputBox inputBox = (ClearableInputBox) event.getSource();
		String text = inputBox.getText();
		if ( inputBox.getInitialText().equals( text ) ) {
			inputBox.setText( "" );
		}
	}
	
	public void onBlur( BlurEvent event ) 
	{ 
		ClearableInputBox inputBox = (ClearableInputBox) event.getSource();
		String text = inputBox.getText();
		if ( text.trim().equals( "" ) ) 
			inputBox.setText( inputBox.getInitialText() );
	}
	
}
