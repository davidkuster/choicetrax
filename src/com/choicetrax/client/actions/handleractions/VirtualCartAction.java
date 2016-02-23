package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.data.VirtualCart;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class VirtualCartAction
	implements HandlerAction
{
	
    private int userID;
    private int partnerID;
    private VirtualCart cart;
    private String actionType;
    // action types = Constants.ACTION_ADD, Constants.ACTION_REMOVE
    
    
    public VirtualCartAction() {
    	super();
    }
    
    
    public String getLogString() 
    {
    	String partner = null;
    	if ( cart != null )
    		partner = cart.getPartnerName();
    	else
    		partner = partnerID + "";
    	
    	String trackIDs = null;
    	try {
    		trackIDs = cart.getReleasesCache().getTrackIDsList() + "";
    	} catch ( Exception e ) { }
    	
		return "VirtualCartAction: "
				+ "userID [" + userID + "] "
				+ "partnerID [" + partner + "] "
				+ "trackIDs [" + trackIDs + "] "
				+ "action [" + actionType + "]";
	}
    
    
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public VirtualCart getVirtualCart() {
		return this.cart;
	}
	
	public void setVirtualCart( VirtualCart virtualCart ) {
		this.cart = virtualCart;
	}
	
	
	public int getPartnerID() {
		return partnerID;
	}
	
	public void setPartnerID(int partnerID) {
		this.partnerID = partnerID;
	}
	
	public String getActionType() {
		return actionType;
	}
	
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

}