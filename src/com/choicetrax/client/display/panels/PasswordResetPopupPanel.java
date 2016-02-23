package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.handleractions.PasswordResetRequestAction;
import com.choicetrax.client.display.panels.decorators.PopupDecoratorPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.WindowAlertOnResponseController;
import com.choicetrax.client.input.ClearableTextBox;


public class PasswordResetPopupPanel extends PopupPanel 
{
	
	private ChoicetraxViewManager viewManager = null;
		
	private ClearableTextBox emailTextBox = new ClearableTextBox( "Email Address" );
	
	
	
	public PasswordResetPopupPanel( ChoicetraxViewManager manager, 
									ClickEvent event )
	{
		super( true );
		this.setAnimationEnabled( true );
		
		this.viewManager = manager;
		
		emailTextBox.addKeyPressHandler( new EnterListener() );
		emailTextBox.setWidth( "200px" );
		
		this.add( new PopupDecoratorPanel( createDisplayPanel() ) );
		//this.setStyleName( "popup" );
		
		Widget widgetSender = (Widget) event.getSource();
		
		this.setPopupPosition( widgetSender.getAbsoluteLeft(), 
								widgetSender.getAbsoluteTop() + widgetSender.getOffsetHeight() );
		this.show();
	}		
	
	
	private Widget createDisplayPanel() 
	{
		HTML instructionHTML = new HTML( "Enter the email address you used <br>"
										+ "to create your Choicetrax account: " );

		HTML tipHTML = new HTML( "Hit Enter and a password reset link <br>"
								+ "will be sent to this email address." );
		tipHTML.setStylePrimaryName( "smallItalicText" );
		
		VerticalPanel vp = new VerticalPanel();	
		vp.setSpacing( 2 );
		
		vp.add( instructionHTML );
		vp.add( emailTextBox );
		vp.add( tipHTML );

		return vp;
	}
	
	
	private void submit() 
	{
		hide();
		
		String emailAddr = emailTextBox.getText();
		
		if ( ( emailAddr != null )
			&& ( ! emailAddr.trim().equals( "" ) )
			&& ( ! emailAddr.equals( emailTextBox.getInitialText() ) ) )
		{
			PasswordResetRequestAction action = new PasswordResetRequestAction();
			action.setEmailAddress( emailAddr );
			
			viewManager.deferAction( action, new WindowAlertOnResponseController( viewManager ) );
		}
	}
	
	
	
	private class EnterListener
		implements KeyPressHandler
	{
		public void onKeyPress( KeyPressEvent event )
		//Widget sender, char keyCode, int modifiers )
		{
			//if ( keyCode == KEY_ENTER )
			if ( event.getCharCode() == KeyCodes.KEY_ENTER )
				submit();
		}
	}

}
