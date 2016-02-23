package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.data.TrackComponent;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class FavoritesAction 
	implements HandlerAction
{
	
    private int userID;
    private TrackComponent favorite;
    private String actionType;
    // action types = Constants.ACTION_ADD, Constants.ACTION_REMOVE
    
    
    public FavoritesAction() {
    	super();
    }
    
    
    public String getLogString() 
    {
    	String favName = null;
    	if ( favorite != null )
    		favName = favorite.getName();
    	
		return "FavoritesAction: "
				+ "userID [" + userID + "] "
				+ "fav name [" + favName + "] "
				+ "action [" + actionType + "]";
	}
    
    
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	public void setFavorite( TrackComponent favoriteObj ) {
		this.favorite = favoriteObj;
	}
	public TrackComponent getFavorite() {
		return favorite;
	}

}