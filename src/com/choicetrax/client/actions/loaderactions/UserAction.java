package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.data.User;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class UserAction 
	implements LoaderAction
{
	
    private String actionType;
    private User userObj;
    
    
    public UserAction() {
    	super();
    }
    
    
    public String getLogString() 
	{
		StringBuffer log = new StringBuffer();
		if ( userObj != null )
			log.append( userObj.toString() );
		if ( actionType != null )
			log.append( "action [" + actionType + "] " );
		
		return "UserAction: " + log.toString();
	}
    
    
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public User getUserObj() {
		return userObj;
	}
	public void setUserObj(User userObj) {
		this.userObj = userObj;
	}

}