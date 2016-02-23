package com.choicetrax.client.display;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.loaderactions.UserAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.User;
import com.choicetrax.client.data.config.Countries;
import com.choicetrax.client.data.config.Country;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.AccountConfigController;


public class AccountConfigComposite 
	extends Composite
	implements ChoicetraxViewComponent
{
	
	private ChoicetraxViewManager viewManager = null;
	
	private VerticalPanel vp = null;
	
	private TextBox username = new TextBox();
	private PasswordTextBox password1 = new PasswordTextBox();
	private PasswordTextBox password2 = new PasswordTextBox();
	private TextBox firstName = new TextBox();
	private TextBox lastName = new TextBox();
	private TextBox emailAddress = new TextBox();
	private TextBox city = new TextBox();
	private TextBox state = new TextBox();
	private ListBox countryListBox = new ListBox(); 
	private Button button = null;
	
	private String actionType = null;
	
	private Countries countries = null;
	
		
	
	public AccountConfigComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		vp = new VerticalPanel();
		vp.setSpacing( 10 );
		initWidget( vp );
		
		updateDisplay( viewManager.getCurrentUser() );
	}
	
	
	public void initialize()
	{
		countries = viewManager.getConfigData().getCountries();
		
		populateCountryList();
		
		User user = viewManager.getCurrentUser();
		if ( user != null )
			setUserCountry( user.getCountryCode() );
	}
	
	
	public void updateDisplay( LoaderResponse response )
	{
		vp.clear();
		this.clearInputFields();
		
		HTML title = null;
		User user = (User) response;
				
		if ( user != null )
		{
			// if user is logged in, config account
			this.actionType = Constants.USER_CONFIG;
			
			title = new HTML( "<b>Config Account</b>" );
			button = new Button( "Save" );
			button.addClickHandler( new AccountConfigListener( actionType ) );
			
			username.setText( user.getUserName() );
			firstName.setText( user.getUserFirstName() );
			lastName.setText( user.getUserLastName() );
			city.setText( user.getCity() );
			state.setText( user.getState() );
			emailAddress.setText( user.getEmailAddress() );
			
			if ( countries != null ) {
				setUserCountry( user.getCountryCode() );
			}
		}
		else
		{
			// if not logged in, create account
			this.actionType = Constants.USER_CREATE;
			
			title = new HTML( "<b>Create Account</b>" );
			button = new Button( "Create" );
			button.addClickHandler( new AccountConfigListener( actionType ) );
		}
		
		vp.add( title );
		vp.setCellHorizontalAlignment( title, HasHorizontalAlignment.ALIGN_CENTER );
		
		FlexTable table = createConfigTable();
		vp.add( table );
		vp.setCellHorizontalAlignment( table, HasHorizontalAlignment.ALIGN_CENTER );
	}
	
	
	private FlexTable createConfigTable()
	{
		username.setStyleName( "accountTextField" );
		password1.setStyleName( "accountTextField" );
		password2.setStyleName( "accountTextField" );
		firstName.setStyleName( "accountTextField" );
		lastName.setStyleName( "accountTextField" );
		emailAddress.setStyleName( "accountTextField" );
		city.setStyleName( "accountTextField" );
		state.setStyleName( "accountTextField" );
		
		FlexTable table = new FlexTable();
		
		int row = 0;
		
		table.setWidget( row, 0, new Label( "Username: " ) );
		table.setWidget( row++, 1, username );
		
		table.setWidget( row, 0, new Label( "Password: " ) );
		table.setWidget( row++, 1, password1 );
		
		table.setWidget( row, 0, new Label( "Confirm Password: " ) );
		table.setWidget( row++, 1, password2 );
		
		table.setWidget( row, 0, new Label( "Email Address: " ) );
		table.setWidget( row++, 1, emailAddress );
		
		table.setWidget( row, 0, new Label( "First Name: " ) );
		table.setWidget( row++, 1, firstName );
		
		table.setWidget( row, 0, new Label( "Last Name: " ) );
		table.setWidget( row++, 1, lastName );
		
		table.setWidget( row, 0, new Label( "City: " ) );
		table.setWidget( row++, 1, city );
		
		table.setWidget( row, 0, new Label( "State: " ) );
		table.setWidget( row++, 1, state );
		
		table.setWidget( row, 0, new Label( "Country: " ) );
		table.setWidget( row++, 1, countryListBox );
		
		
		// add options to specify favorites genres (to be used in
		// generating recommendations) and also which genres
		// they want to receive emails about.  have the ability to
		// specify an email frequency - daily, weekly, etc.
		
		table.setWidget( row, 0, button );
		table.getFlexCellFormatter().setColSpan( row, 0, 2 );
		table.getCellFormatter().setAlignment( row, 0, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE );
				
		return table;
	}
	
	
	private void clearInputFields()
	{
		username.setText( "" );
		password1.setText( "" );
		password2.setText( "" );
		firstName.setText( "" );
		lastName.setText( "" );
		emailAddress.setText( "" );
		city.setText( "" );
		state.setText( "" );
		countryListBox.setSelectedIndex( 0 ); 
	}
	
	
	public void clearDisplay()
	{
		updateDisplay( null );
	}
	
	public void setWaitingState( boolean waiting )
	{
		if ( waiting )
			button.setEnabled( false );
		else
			button.setEnabled( true );
	}
	
	/*
	 * validate user input.  username, password, confirm password,
	 * and emailaddress must all be populated.  as well, password
	 * and confirm password must match.
	 */
	private boolean validateInput()
	{
		String userText = username.getText().trim();
		String pass1Text = password1.getText().trim();
		String pass2Text = password2.getText().trim();
		String emailText = emailAddress.getText().trim();
		
		if ( "".equals( userText ) )
		{
			Window.alert( "Please enter a username." );
			username.setFocus( true );
			return false;
		}
		else if ( userText.length() > 20 )
		{
			Window.alert( "Username must be 20 characters or less." );
			username.setFocus( true );
			return false;
		}
		else if ( ! Constants.USER_CONFIG.equals( actionType ) ) 
		{
			if ( "".equals( pass1Text ) )
			{
				Window.alert( "Please enter a password." );
				password1.setFocus( true );
				return false;
			}
			else if ( "".equals( pass2Text ) )
			{
				Window.alert( "Please confirm your password." );
				password2.setFocus( true );
				return false;
			}
			else if ( ! pass1Text.equals( pass2Text ) ) 
			{
				Window.alert( "Confirm Password does not match Password." );
				password2.setFocus( true );
				return false;
			}
		}
		else if ( ! pass1Text.equals( pass2Text ) )
		{
			Window.alert( "Confirm Password does not match Password." );
			password2.setFocus( true );
			return false;
		}
		else if ( "".equals( emailText ) )
		{
			Window.alert( "Please enter your email address." );
			emailAddress.setFocus( true );
			return false;
		}
		
		username.setText( Constants.escapeChars( userText ) );
		password1.setText( Constants.escapeChars( pass1Text ) );
		password2.setText( Constants.escapeChars( pass2Text ) );
		emailAddress.setText( Constants.escapeChars( emailText ) );
		firstName.setText( Constants.escapeChars( firstName.getText() ) );
		lastName.setText( Constants.escapeChars( lastName.getText() ) );
		city.setText( Constants.escapeChars( city.getText() ) );
		
		return true;
	}
	
		
	
	private void populateCountryList()
	{
		countryListBox.addItem( "" );
		
		Iterator<Country> i = countries.getCountryList().iterator();
		while ( i.hasNext() ) {
			Country country = i.next();
			countryListBox.addItem( country.getCountryName() );
		}
	}
	
	
	private void setUserCountry( String userCountryCode )
	{
		if ( ( userCountryCode == null ) || ( "".equals( userCountryCode ) ) )
			return;
		
		// most people will be in the US, so sort backwards
		String userCountryName = countries.getCountryName( userCountryCode );
		
		for ( int x = countryListBox.getItemCount() - 1; x >= 0; x-- )
		{
			if ( countryListBox.getItemText( x ).equals( userCountryName ) )
			{
				countryListBox.setSelectedIndex( x );
				break;
			}
		}
	}
	
	
	private class AccountConfigListener implements ClickHandler
	{
		String actionType = null;
		
		public AccountConfigListener( String type )
		{
			this.actionType = type;
		}
		
		public void onClick( ClickEvent event )
		{
			if ( validateInput() )
			{
				String countryName = countryListBox.getItemText(  
										countryListBox.getSelectedIndex() );
				String countryCode = countries.getCountryCode( countryName );
				int countryID = countries.getCountryID( countryCode );
				
				User userObj = new User();
				userObj.setUserName( username.getText() );
				userObj.setUserPass( password1.getText() );
				userObj.setEmailAddress( emailAddress.getText() );
				userObj.setUserFirstName( firstName.getText() );
				userObj.setUserLastName( lastName.getText() );
				userObj.setCity( city.getText() );
				userObj.setState( state.getText() );
				userObj.setCountryCode( countryCode );
				userObj.setCountryID( countryID );

				User currentUser = viewManager.getCurrentUser();
				if ( currentUser != null )
				{
					userObj.setUserID( currentUser.getUserID() );
					userObj.setSessionID( currentUser.getSessionID() );
				}
				
				UserAction requestObj = new UserAction();
				requestObj.setActionType( actionType );
				requestObj.setUserObj( userObj );
				
				viewManager.executeAction( requestObj, 
								new AccountConfigController( viewManager, actionType ) );
			}
		}
	}
	
}
