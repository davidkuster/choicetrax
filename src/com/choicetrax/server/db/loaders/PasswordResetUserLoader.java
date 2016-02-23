package com.choicetrax.server.db.loaders;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.UserAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.User;

import com.choicetrax.server.db.DataLoader;


public class PasswordResetUserLoader 
	extends AbstractEmailLinkLoader
{
	
	
	
	public PasswordResetUserLoader() {
		super();
	}
	


	@Override
	protected LoaderAction createResponseAction( int id ) 
	{
		User user = new User();
		user.setUserID( id );
		user.setSessionID( getSessionID() );
		
		UserAction action = new UserAction();
		action.setActionType( Constants.USER_AUTOLOGIN );
		action.setUserObj( user );
		
		return action;
	}



	@Override
	protected DataLoader createResponseDataLoader() {
		return new UserLoader();
	}



	@Override
	protected String createTimestampSQL( String emailAddr ) 
	{
		return "select UserID as ID, "
				+	"SessionID, "
				+	"( SIDTimestamp + 0 ) as Timestamp1, "
				+	"( LastLoginTimestamp + 0 ) as Timestamp2, "
				+	"NumLogins "
				+ "from Users "
				+ "where EmailAddress = '" + emailAddr + "' ";
	}



	@Override
	protected String getIdNotFoundExceptionText() 
	{
		return "Unable to change password. "
				+ "\n\n"
				+ "It appears the password reset link is no longer valid. \n"
				+ "For security reasons please request a new reset link \n"
				+ "using the \"Forgot Pass?\" link on the left.";
	}
	
	
	@Override
	protected void doSuccessOperation( int id )
	{
		// no-op, nothing needs to be done on password reset
	}
	

}
