package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.data.VirtualCart;


public class PurchaseHistoryAction 
	implements HandlerAction 
{

	private String actionType = null;
	private int userID = -1;
	private int partnerID = -1;
	private VirtualCart cart = null;
	
	// virtual cart is now used instead of trackID.  
	// can add or remove one track one or multiple tracks per call.
	
	
	public PurchaseHistoryAction() {
		super();
	}
	
	
	public String getLogString() 
	{
		String partner = null;
		if ( cart != null )
			partner = cart.getPartnerName();
		else 
			partner = partnerID + "";
		
		return "PurchaseHistoryAction: "
				+ "userID [" + userID + "] "
				+ "partner [" + partner + "] "
				+ "action [" + actionType + "]";
	}
	
		
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
		
	public void setVirtualCart( VirtualCart virtualCart ) {
		this.cart = virtualCart;
	}
	
	public VirtualCart getVirtualCart() {
		return this.cart;
	}
	
	public void setUserID( int id ) {
		this.userID = id;
	}
	
	public int getUserID() {
		return this.userID;
	}
	
	public void setPartnerID( int id ) {
		this.partnerID = id;
	}
	
	public int getPartnerID() {
		return this.partnerID;
	}
	
}
