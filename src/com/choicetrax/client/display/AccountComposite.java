package com.choicetrax.client.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.*;

import com.choicetrax.client.Choicetrax;
import com.choicetrax.client.data.User;
import com.choicetrax.client.data.VirtualCarts;
import com.choicetrax.client.display.panels.PasswordResetPopupPanel;
import com.choicetrax.client.display.panels.decorators.BoxTabPanel;
import com.choicetrax.client.actions.loaderactions.LoadUserPurchaseHistoryAction;
import com.choicetrax.client.actions.loaderactions.UserAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.input.ClearablePasswordTextBox;
import com.choicetrax.client.input.ClearableTextBox;
import com.choicetrax.client.input.ClickableImage;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.*;
import com.choicetrax.client.logic.callbacks.UserAutoLoginController;
import com.choicetrax.client.logic.callbacks.UserLoginController;
import com.choicetrax.client.logic.callbacks.PurchaseHistoryLoadController;


public class AccountComposite 
	extends Composite 
	implements ChoicetraxViewComponent
{
	
	private TextBox usernameBox = new ClearableTextBox( "Username" );
	private PasswordTextBox passwordBox = new ClearablePasswordTextBox( "Password" );
	private ClickableImage loginButton = new ClickableImage( "img/layout/btnLogin.gif" );
	
	private BoxTabPanel panel = null;
	
	private ChoicetraxViewManager viewManager = null; 
	
	
	public AccountComposite( ChoicetraxViewManager manager )
	{
		viewManager = manager;
		
		panel = new BoxTabPanel();	
		initWidget( panel );
		
		usernameBox.addKeyPressHandler( new EnterListener() );
		passwordBox.addKeyPressHandler( new EnterListener() );
		loginButton.addClickHandler( new LoginListener() );
		
		panel.add( createLoginPanel(), new Image( Constants.IMAGE_BUNDLE_TABS.login_on() ) );
		panel.selectTab( 0 );		
		panel.setWidth( "100%" );
		panel.setHeight( "100px" );
				
		String sessionID = Choicetrax.getCookie( Constants.COOKIE_SESSIONID );
		String userID = Choicetrax.getCookie( Constants.COOKIE_USERID );
		if ( ( sessionID != null ) && ( userID != null ) )
		{
			// check with server that sessionID is still valid
			User userObj = new User();
			userObj.setSessionID( sessionID );
			userObj.setUserID( Integer.parseInt( userID ) );
			
			UserAction requestObj = new UserAction();
			requestObj.setActionType( Constants.USER_AUTOLOGIN );
			requestObj.setUserObj( userObj );
			
			viewManager.executeAction( requestObj, new UserAutoLoginController( viewManager ) );
		}
	}
	
	
	public void updateDisplay( LoaderResponse response )
	{
		panel.remove( 0 );
		User user = (User) response;
		
		if ( user == null )
			panel.add( createLoginPanel(), 
						new Image( Constants.IMAGE_BUNDLE_TABS.login_on() ) );
		else
			panel.add( createAccountDetailPanel( user ), 
						new Image( Constants.IMAGE_BUNDLE_TABS.account_on() ) );
		
		panel.selectTab( 0 );
	}
	
	
	public void setWaitingState( boolean waiting )
	{
		if ( waiting )
		{
			//loginButton.setEnabled( false );
			usernameBox.setEnabled( false );
			passwordBox.setEnabled( false );
		}
		else
		{
			//loginButton.setEnabled( true );
			usernameBox.setEnabled( true );
			passwordBox.setEnabled( true );
		}
	}
	
	
	private Panel createLoginPanel()
	{
		/*Label rememberLabel = new Label( "Remember" );
		CheckBox rememberBox = new CheckBox();
		rememberBox.setChecked( true );
		
		HorizontalPanel rememberPanel = new HorizontalPanel();
		rememberPanel.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
		rememberPanel.add( rememberLabel );
		rememberPanel.add( rememberBox );*/		
		
		Label newAccount = new HyperlinkLabel( "New User?", new AccountCreateConfigListener() );
		Label forgotPass = new HyperlinkLabel( "Forgot Pass?", new ForgotPasswordListener() );
		
		newAccount.setWordWrap( false );
		forgotPass.setWordWrap( false );
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( newAccount );
		vp.add( forgotPass );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
		hp.add( vp );
		hp.add( new HTML( " &nbsp; " ) );
		hp.add( loginButton );
	
		VerticalPanel content = new VerticalPanel();
		content.setWidth( "100%" );
		content.setHeight( "100px" );
		content.add( usernameBox );
		content.add( passwordBox );
		content.add( hp );
		
		return content;
	}
	
		
	/*
	 * view wishlist option
	 * view purchase history option
	 * logoff option
	 * put recommended tracks option here ???
	 */
	private Panel createAccountDetailPanel( User user )
	{
		Label accountConfigLink = new HyperlinkLabel( "Account",
									new AccountCreateConfigListener() );
		
		VirtualCarts carts = user.getVirtualCarts();
		
		int numCarts = carts.getNumCarts();
		String cartText = "(" + numCarts + " cart";
		if ( numCarts > 1 ) cartText += "s";
		
		Label cartsLink = new HyperlinkLabel( "Virtual Carts", 
							new ViewCartsListener() );
		Label cartTracksNum = new Label( cartText + ", " + carts.size() + " trax)" );
		cartTracksNum.setStyleName( "smallerText" );
		
		VerticalPanel cartsPanel = new VerticalPanel();
		//HorizontalPanel cartsPanel = new HorizontalPanel();
		cartsPanel.add( cartsLink );
		cartsPanel.add( cartTracksNum );
		cartsPanel.setStyleName( "loggedinVerticalPanels" );
		
		Label wishlistLink = new HyperlinkLabel( "Wishlist",
								new ViewWishlistListener() );
		Label wishlistNum = new Label( " (" + user.getWishlist().size() + " trax)" );
		wishlistNum.setStyleName( "smallerText" );
		
		VerticalPanel wishlistPanel = new VerticalPanel();
		wishlistPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		
		wishlistPanel.add( wishlistLink );
		wishlistPanel.add( wishlistNum );
		wishlistPanel.setStyleName( "loggedinVerticalPanels" );
		
		Label purchaseHistoryLink = new HyperlinkLabel( "Purch History",
										new ViewPurchaseHistoryListener() );
		
		Label logoffLink = new HyperlinkLabel( "Logoff", new LogoffListener() );
		
		cartsLink.setWordWrap( false );
		//purchaseHistoryLink.setWordWrap( false );
		
		Label usernameLabel = new Label();
		
		String username = user.getUserName();
		if ( username.length() > 13 ) {
			usernameLabel.setTitle( username );
			username = username.substring( 0, 12 ) + "...";
		}
		usernameLabel.setText( username );
		
		Grid g = new Grid( 3, 2 );
		g.setWidget( 0, 0, usernameLabel );
		g.setWidget( 0, 1, logoffLink );
		
		g.setWidget( 1, 0, accountConfigLink );
		g.setWidget( 1, 1, purchaseHistoryLink );
		
		g.setWidget( 2, 0, cartsPanel );
		g.setWidget( 2, 1, wishlistPanel );
		
		/*FlexTable g = new FlexTable();
		g.setWidget( 0, 0, new Label( user.getUserName() ) );
		g.setWidget( 0, 1, logoffLink );
		
		g.setWidget( 1, 0, accountConfigLink );
		g.setWidget( 1, 1, purchaseHistoryLink );
		
		g.setWidget( 2, 0, cartsPanel );
		g.getFlexCellFormatter().setColSpan( 2, 0, 2 );
		g.setWidget( 3, 0, wishlistPanel );
		g.getFlexCellFormatter().setColSpan( 3, 0, 2 );
		
		/*VerticalPanel g = new VerticalPanel();
		g.add( new Label( user.getUserName() ) );
		g.add( accountConfigLink );
		g.add( cartsPanel );
		g.add( wishlistPanel );
		g.add( purchaseHistoryLink );
		g.add( logoffLink );*/
		
		//g.setStyleName( "accountPanelGrid" );
		g.setStylePrimaryName( "loggedin" );
				
		return g;
	}
	
	
	private void login()
	{
		String username = usernameBox.getText();
		String password = passwordBox.getText();
		
		usernameBox.setFocus( false );
		passwordBox.setFocus( false );
		
		if ( username.length() == 0 || password.length() == 0 )
		{
			Window.alert( "Please fill in both login fields." );
		}
		else
		{
			setWaitingState( true );
			
			User userObj = new User();
			userObj.setUserName( username );
			userObj.setUserPass( password );
			
			UserAction requestObj = new UserAction();
			requestObj.setActionType( Constants.USER_LOGIN );
			requestObj.setUserObj( userObj );
			
			viewManager.executeAction( requestObj, new UserLoginController( viewManager ) );
		}
	}
	
	public void clearDisplay()
	{
		usernameBox.setText( "Username" );
		passwordBox.setText( "Password" );
	}

	
	private class AccountCreateConfigListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{
			clearDisplay();
			
			viewManager.showCenterPanel( Constants.PANEL_ACCOUNT_CONFIG );
		}
	}
	
	private class ViewWishlistListener 
		implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{
			viewManager.updateHistoryDisplay( Constants.HISTORY_WISHLIST );
		}
	}
	
	private class ViewCartsListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			viewManager.updateHistoryDisplay( Constants.HISTORY_VIRTUAL_CARTS );
		}
	}
	
	
	private class ViewPurchaseHistoryListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			User user = viewManager.getCurrentUser();
			if ( user.getPurchaseHistory() != null )
			{
				// if we've already loaded the purchase history,
				// simply display it
				
				viewManager.updateHistoryDisplay( Constants.HISTORY_PURCHASE_HISTORY );
			}
			else
			{
				// else load it from server
				// display will be called from callback
				LoadUserPurchaseHistoryAction action = new LoadUserPurchaseHistoryAction();
				action.setUserID( user.getUserID() );
				
				viewManager.setWaitingState( Constants.PANEL_RELEASES, true );
				viewManager.deferAction( action, new PurchaseHistoryLoadController( viewManager ) );
			}
		}
	}
	
	
	private class LogoffListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event ) 
		{
			updateDisplay( null );
			
			viewManager.logoffUser();
		}
	}
	
	
	private class LoginListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event ) {
			login();
		}
	}
	
	private class EnterListener
		implements KeyPressHandler
	{
		public void onKeyPress( KeyPressEvent event ) 
		//Widget sender, char keyCode, int modifiers )
		{
			if ( event.getCharCode() == KeyCodes.KEY_ENTER )
				login();
		}
	}
	
	
	private class ForgotPasswordListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			//Window.alert( "Automated password reset is not yet implemented.\n"
			//			+ "Email dave@choicetrax.com for assistance." );
			
			
			new PasswordResetPopupPanel( viewManager, event );
			
			// send user an email with a link to click on
			// which will then allow them to enter a new password.
			// since the passwords are encrypted i don't know them
			// and can't just send out the password.
			
			// the link sent should include the sessionID for that
			// user as a URL parameter.  (maybe always create a new 
			// sessionID before sending?)  then if a user hits
			// that URL, just look for the sessionID that matches
			// and let them input a new password.
			
			// i think...
		}
	}
	
}
