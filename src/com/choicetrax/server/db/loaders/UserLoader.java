package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.UserAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.*;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.client.util.exception.FailedAuthenticationException;

import com.choicetrax.server.util.jdbc.*;
import com.choicetrax.server.util.bcrypt.BCrypt;
import com.choicetrax.server.constants.ServerConstants;
import com.choicetrax.server.db.DataLoader;


public class UserLoader 
	implements DataLoader
{

	public UserLoader()
	{
		super();
	}
	
	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		User userObj = null;
		
		UserAction actionObj = (UserAction) action;
		String actionType = actionObj.getActionType();
		
		if ( Constants.USER_AUTOLOGIN.equals( actionType ) )
		{
			userObj = autoLoginUser( actionObj.getUserObj() );
		}
		else if ( Constants.USER_LOGIN.equals( actionType ) )
		{
			userObj = loginUser( actionObj.getUserObj() );
		}
		else if ( Constants.USER_CREATE.equals( actionType ) )
		{
			userObj = createUser( actionObj.getUserObj() );
		}
		else if ( Constants.USER_CONFIG.equals( actionType ) )
		{
			// may want to re-examine how this is being done
			// - on update, full favorites will get reloaded
			// - and all user data sent back to client...
			userObj = updateUser( actionObj.getUserObj() );
		}
		else
			throw new ChoicetraxException( "Unsupported action type: " + actionType,
											this.getClass().getName() + ".loadUserData()" );
		
		loadFavorites( userObj );
		loadWishlist( userObj );
		loadVirtualCarts( userObj );
		//loadPurchaseHistory( userObj );
		
		// don't load purchase history on startup
		// wait until user clicks on that link so we're not
		// potentially loading a ton of data they may not
		// view or care about
		
		return userObj;
	}
	
	
	public void changeUserPassword( int userID, String password ) 
		throws ChoicetraxException
	{
		User user = new User();
		user.setUserID( userID );
		user.setUserPass( password );
		
		updateUser( user );
	}
	
	
	private void loadWishlist( User userObj )
		throws ChoicetraxException
	{
		UserAction action = new UserAction();
		action.setUserObj( userObj );
		
		UserWishlistLoader loader = new UserWishlistLoader();
		ReleasesCache cache = (ReleasesCache) loader.loadData( action );
		
		userObj.setWishlist( cache );
	}
	
	
	/*private void loadPurchaseHistory( User userObj )
		throws ChoicetraxException
	{
		UserAction action = new UserAction();
		action.setUserObj( userObj );
		action.setActionType( Constants.LOAD_PURCHASE_HISTORY );
		
		UserPurchaseHistoryLoader loader = new UserPurchaseHistoryLoader();
		ReleasesCache cache = (ReleasesCache) loader.loadData( action );
		
		userObj.setPurchaseHistory( cache );
	}*/
	
	
	private void loadFavorites( User userObj )
		throws ChoicetraxException
	{
		UserAction action = new UserAction();
		action.setUserObj( userObj );
		action.setActionType( Constants.LOAD_FAVORITES );
		
		UserFavoritesLoader loader = new UserFavoritesLoader();
		loader.loadData( action );
	}
	
	
	private void loadVirtualCarts( User userObj )
		throws ChoicetraxException
	{
		UserAction action = new UserAction();
		action.setUserObj( userObj );
		
		UserVirtualCartsLoader loader = new UserVirtualCartsLoader();
		VirtualCarts carts = (VirtualCarts) loader.loadData( action );
		
		userObj.setVirtualCarts( carts );
	}
		
	
	private User autoLoginUser( User userObj )
		throws ChoicetraxException
	{
		String methodName = "autoLoginUser()";
		
		String sessionID = userObj.getSessionID();
		int userID = userObj.getUserID();
		
		String sql = "select UserName, UserFirstName, UserLastName, "
					+	"EmailAddress, SIDTimestamp, NumLogins, "
					+	"UserCity, UserState, UserZip, UserCountryCode, "
					+	"UserCountryID "
					+ "from Users "
					+ "where SessionID = '" + sessionID + "' "
					+	"and UserID = " + userID + " ";
		
		DBResource dbHandle = null;
		
		int numLogins = 0;
		
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		if ( rs.next() )
    		{
    			java.sql.Timestamp sidTS = rs.getTimestamp( 	"SIDTimestamp" );

				Calendar c = Calendar.getInstance();
				c.add( Calendar.HOUR, 24 );
				
			    if ( sidTS.getTime() < c.getTimeInMillis() )
			    {
			    	numLogins = rs.getInt( "NumLogins" );
			    	
			    	userObj = new User();
			    	userObj.setUserID( 			userID );
			    	userObj.setSessionID( 		sessionID );
			    	userObj.setUserName( 		rs.getString( "UserName" ) );
			    	userObj.setUserFirstName( 	rs.getString( "UserFirstName" ) );
			    	userObj.setUserLastName( 	rs.getString( "UserLastName" ) );
			    	userObj.setEmailAddress( 	rs.getString( "EmailAddress" ) );
			    	userObj.setCity( 			rs.getString( "UserCity" ) );
			    	userObj.setState( 			rs.getString( "UserState" ) );
			    	userObj.setZipCode( 		rs.getString( "UserZip" ) );
			    	userObj.setCountryCode( 	rs.getString( "UserCountryCode" ) );
			    	userObj.setCountryID( 		rs.getInt( "UserCountryID" ) );
			    }
			    else 
			    	throw new FailedAuthenticationException( "Expired SessionID" );
    		}
    		else
    		{
    			throw new FailedAuthenticationException( "SessionID not found." );
    		}
    		
    		rs.close();
    		
    		
    		String sql2 = "update Users "
				+ "set NumLogins = " + ( numLogins + 1 ) + ", "
				+	"LastLoginTimestamp = sysdate() "
				+ "where UserID = " + userID + " ";
    		
    		dbHandle.executeUpdate( sql2 );
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing auto-login with sessionID ["
    										+ userObj.getSessionID() + "]: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}

		return userObj;		
	}
	
			
	private User loginUser( User userObj )
		throws ChoicetraxException
	{
		String methodName = "loginUser()";
		
		String userName = ServerConstants.escapeChars( userObj.getUserName() );
		String password = userObj.getUserPass();	
		
		// probably need to generate sessionID & store in DB
		// after successful login !!!
		
		String sql = "select UserID, UserFirstName, UserLastName, "
					+	"UserPassHash, EmailAddress, NumLogins, "
					+	"UserCity, UserState, UserZip, UserCountryCode, "
					+	"UserCountryID "
					+ "from Users "
					+ "where UserName = '" + userName + "' ";
		
		DBResource dbHandle = null;
		ResultSet rs = null;
		
		String sessionID = null;
		int numLogins = 0;
		int userID = 0;
		
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		rs = dbHandle.executeQuery( sql );
    		if ( rs.next() )
    		{
				String userPassHash 	= rs.getString( "UserPassHash" );
				numLogins				= rs.getInt( "NumLogins" );
				
				boolean valid = BCrypt.checkpw( password, userPassHash );
			    if ( valid )
			    {
			    	sessionID 	= getNewSessionID();
			    	userID 		= rs.getInt( "UserID" );
			    	
			    	userObj = new User();
			    	userObj.setUserID( 			userID );
			    	userObj.setSessionID( 		sessionID );
			    	userObj.setUserName( 		userName );
			    	userObj.setUserFirstName( 	rs.getString( "UserFirstName" ) );
			    	userObj.setUserLastName( 	rs.getString( "UserLastName" ) );
			    	userObj.setEmailAddress( 	rs.getString( "EmailAddress" ) );
			    	userObj.setCity( 			rs.getString( "UserCity" ) );
			    	userObj.setState( 			rs.getString( "UserState" ) );
			    	userObj.setZipCode( 		rs.getString( "UserZip" ) );
			    	userObj.setCountryCode( 	rs.getString( "UserCountryCode" ) );
			    	userObj.setCountryID( 		rs.getInt( "UserCountryID" ) );
			    }
			    else 
			    	throw new FailedAuthenticationException( "Wrong Username or Password." );
    		}
    		else
    		{
    			throw new FailedAuthenticationException( "Wrong Username or Password." );
    		}
    		
    		
    		// store sessionID & timestamp, increase login count
    		
    		String sql2 = "update Users "
				+ "set SessionID = '" + sessionID + "', "
				+	"NumLogins = " + ( numLogins + 1 ) + ", "
				+	"SIDTimestamp = sysdate(), "
				+	"LastLoginTimestamp = sysdate() "
				+ "where UserID = " + userID + " ";
    		
    		dbHandle.executeUpdate( sql2 );
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing login with username [" 
    										+ userObj.getUserName() + "]: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( rs != null ) {
    			try { 
    				rs.close();
    			} catch ( Exception sqle ) { }
    		}
    		
    		if ( dbHandle != null )
    			dbHandle.close();
    	}

		return userObj;
	}
	
	
	private User createUser( User userObj )
		throws ChoicetraxException
	{
		String methodName = "createUser()";
		
		String password = userObj.getUserPass();
		String passHash	= BCrypt.hashpw(password, BCrypt.gensalt());
		
		String username 		= ServerConstants.escapeChars( userObj.getUserName() );
		String userFirstName 	= ServerConstants.escapeChars( userObj.getUserFirstName() );
		String userLastName 	= ServerConstants.escapeChars( userObj.getUserLastName() );
		String emailAddress 	= ServerConstants.escapeChars( userObj.getEmailAddress() );
		String userCity 		= ServerConstants.escapeChars( userObj.getCity() );
		String userState 		= ServerConstants.escapeChars( userObj.getState() );
		String userCountryCode	= ServerConstants.escapeChars( userObj.getCountryCode() );
		int userCountryID		= userObj.getCountryID();
		String sessionID		= getNewSessionID();
		
		String sql = "insert into Users "
					+ "( UserName, UserPassHash, UserFirstName, UserLastName, "
					+ 	"EmailAddress, UserCity, UserState, UserCountryCode, UserCountryID, "
					+	"NumLogins, SessionID, CreateTimestamp, SIDTimestamp, "
					+	"LastLoginTimestamp ) "
					+ "values "
					+ "( '" + username + "', "
					+ 	"'" + passHash + "', "
					+	"'" + userFirstName + "', "
					+	"'" + userLastName + "', "
					+	"'" + emailAddress + "', "
					+	"'" + userCity + "', "
					+	"'" + userState + "', "
					+	"'" + userCountryCode + "', "
					+	userCountryID + ", "
					+	"1, "
					+ 	"'" + sessionID + "', "
					+	"sysdate(), "
					+	"sysdate(), "
					+ 	"sysdate() )";
		
		DBResource dbHandle = null;
		
		int userID = -1;
		
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
      		int rowCount = dbHandle.executeUpdate( sql, Statement.RETURN_GENERATED_KEYS );
    		
    		ResultSet rs = dbHandle.getGeneratedKeys();
    		if ( rs.next() )
    		{
    			userID = rs.getInt( 1 );
    		}
    		rs.close();
    		
    		if ( rowCount == 0 )
    		{
    			// user not created
    			throw new ChoicetraxException( "Error creating user [" + userObj.getUserName() 
    											+ "], rowCount = 0",
    											this.getClass().getName() + "." + methodName );
    		}
    		else if ( rowCount > 1 ) 
    		{
    			// more than one UserID has been found for this name
    			// so we have duplicates in the database
    			throw new ChoicetraxException( "Multiple rows affected when creating new user [" 
    											+ userObj.getUserName() + "], "
    											+ "rowCount = " + rowCount,
    											this.getClass().getName() + "." + methodName );
    		}
    		else if ( userID == -1 )
    		{
    			// throw exception ???
    		}
    	}
    	catch ( ChoicetraxException cte )
    	{
    		if ( cte.getErrorMsg().indexOf( "Duplicate" ) != -1 )
    			throw new FailedAuthenticationException( "Sorry, that username or email address "
    													+ "is already in use." );
    		else
    			throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing user creation with username [" 
    										+ userObj.getUserName() + "]: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}
    	
    	
    	User user = new User();
    	user.setUserID( userID );
    	user.setSessionID( sessionID );
    	user.setUserName( username );
    	user.setUserFirstName( userFirstName );
    	user.setUserLastName( userLastName );
    	user.setEmailAddress( emailAddress );
    	user.setCity( userCity );
    	user.setState( userState );
    	user.setCountryCode( userCountryCode );
    	
		return user;
	}
	
	
	private User updateUser( User userObj )
		throws ChoicetraxException
	{
		String methodName = "updateUser()";
		
		StringBuffer sql = new StringBuffer();
		
		String password = userObj.getUserPass();
		if ( ( password != null ) && ( ! "".equals( password ) ) )
		{
			String passHash	= BCrypt.hashpw(password, BCrypt.gensalt());
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "UserPassHash = '" + passHash + "' " );
		}
		
		String username = ServerConstants.escapeChars( userObj.getUserName() );
		if ( ! "".equals( username ) )
		{
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "UserName = '" + username + "' " );
		}
		
		String userFirstName = ServerConstants.escapeChars( userObj.getUserFirstName() );
		if ( ! "".equals( userFirstName ) )
		{
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "UserFirstName = '" + userFirstName + "' " );
		}
		
		String userLastName = ServerConstants.escapeChars( userObj.getUserLastName() );
		if ( ! "".equals( userLastName ) )
		{
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "UserLastName = '" + userLastName + "' " );
		}
		
		String emailAddress = ServerConstants.escapeChars( userObj.getEmailAddress() );
		if ( ! "".equals( emailAddress ) )
		{
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "EmailAddress = '" + emailAddress + "' " );
		}
		
		String userCity = ServerConstants.escapeChars( userObj.getCity() );
		if ( ! "".equals( userCity ) )
		{
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "UserCity = '" + userCity + "' " );
		}
		
		String userState = ServerConstants.escapeChars( userObj.getState() );
		if ( ! "".equals( userState ) )
		{
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "UserState = '" + userState + "' " );
		}
		
		String userCountryCode = ServerConstants.escapeChars( userObj.getCountryCode() );
		if ( ! "".equals( userCountryCode ) )
		{
			if ( sql.length() > 0 ) sql.append( ", " );
			sql.append( "UserCountryCode = '" + userCountryCode + "', "
						+ "UserCountryID = " + userObj.getCountryID() + " " );
		}
		
		if ( sql.length() > 0 )
		{
			sql.insert( 0, "update Users set " );
			sql.append( "where UserID = " + userObj.getUserID() );
			
			
			DBResource dbHandle = null;
			
	    	try
	    	{
	    		dbHandle = ResourceManager.getDBConnection();
	    		
	      		int rowCount = dbHandle.executeUpdate( sql.toString() );
	    		
	    		if ( rowCount == 0 )
	    		{
	    			// user not updated
	    			throw new ChoicetraxException( "Error updating user [" + userObj.getUserName() 
	    											+ "], rowCount = 0",
	    											this.getClass().getName() + "." + methodName );
	    		}
	    		else if ( rowCount > 1 ) 
	    		{
	    			// more than one UserID has been found for this name
	    			// so we have duplicates in the database
	    			throw new ChoicetraxException( "Multiple rows affected when updating user [" 
	    											+ userObj.getUserName() + "], "
	    											+ "rowCount = " + rowCount,
	    											this.getClass().getName() + "." + methodName );
	    		}
	    	}
	    	catch ( ChoicetraxException cte )
	    	{
	    		throw cte;
	    	}
	    	catch ( Throwable t )
	    	{
	    		throw new ChoicetraxException( "Error executing user update with username [" 
	    										+ userObj.getUserName() + "]: " + t,
	    										this.getClass().getName() + "." + methodName );
	    	}
	    	finally
	    	{
	    		if ( dbHandle != null )
	    			dbHandle.close();
	    	}
		}
    	
    	
    	User user = new User();
    	user.setUserID( userObj.getUserID() );
    	user.setSessionID( userObj.getSessionID() );
    	user.setUserName( username );
    	user.setUserFirstName( userFirstName );
    	user.setUserLastName( userLastName );
    	user.setEmailAddress( emailAddress );
    	user.setCity( userCity );
    	user.setState( userState );
    	user.setCountryCode( userCountryCode );
    	
		return user;
	}
	
		
	private String getNewSessionID()
	{
		return new java.rmi.server.UID().toString();
	}
			
}
