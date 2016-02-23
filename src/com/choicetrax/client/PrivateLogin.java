package com.choicetrax.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.handleractions.BetaUserLoginAction;
import com.choicetrax.client.actions.responses.ActionHandlerMessage;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.ChoicetraxActionResponseCallback;


public class PrivateLogin 
{
	
	private ChoicetraxViewManager viewManager = null;
	
	private TextBox username 			= null;
	private PasswordTextBox password	= null;
	private Label statusLabel			= null;
	
	
	
	public PrivateLogin( ChoicetraxViewManager manager ) 
	{
		super();
		
		viewManager = manager;
		
		privateLogin();
	}
	
	
	
	private void privateLogin() 
	{
		username = new TextBox();
		password = new PasswordTextBox();
		statusLabel = new Label( "Please log in." );
		
		Button loginButton = new Button( "login" );
		loginButton.addClickHandler( new LoginListener() );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setWidth( "900px" );
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.add( new HTML( "<p><br>We are currently in private beta. <p>" ) );
		vp.add( username );
		vp.add( password );
		vp.add( loginButton );
		vp.add( new HTML( "<p><br>" ) );
		vp.add( statusLabel );
		vp.add( new HTML( "<p><br><p><br>" ) );
		vp.add( new HTML( "<i>If you've forgotten your login or are interested in <br>"
							+ "obtaining one please email dave@choicetrax.com.</i>" ) );
		vp.setSpacing( 3 );
				
		RootPanel.get( "results" ).add( vp );
	}
	
	
	
	private class LoginListener implements ClickHandler
	{
		public void onClick( ClickEvent sender )
		{
			statusLabel.setText( "logging in..." );
			
			String user = username.getText();
			String pass = password.getText();
			
			BetaUserLoginAction action = new BetaUserLoginAction();
			action.setUsername( user );
			action.setPassword( pass );
			
			viewManager.executeAction( action, new LoginCallback() );
		}
	}
	
	
	private class LoginCallback implements ChoicetraxActionResponseCallback
	{
		public void onFailure( Throwable t ) 
		{
			viewManager.handleError( t, this.getClass().getName() + ".onFailure()" );
		}

		public void onSuccess( ActionResponse result)
		{
			ActionHandlerMessage msg = (ActionHandlerMessage) result;
			
			if ( msg.getResponseCode() == Constants.ACTION_RESPONSE_NORMAL )
			{
				viewManager = null;
				
				RootPanel.get( "results" ).clear();
				Choicetrax.initialize();
			}
			else
			{
				statusLabel.setText( msg.getResponseText() );
			}
		}
	}

}
