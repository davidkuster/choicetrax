package com.choicetrax.client.actions.handleractions;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.UserIpAction;


public class PartnerTransferAction 
	implements HandlerAction, UserIpAction
{
	
	private int userID = -1;
	private String ipAddress = null;
	
	private int partnerID = -1;
	private int[] trackIDs = null;
	

	public PartnerTransferAction() {
		super();
	}

	
	public String getActionType() {
		return null;
	}

	
	public String getLogString() 
	{
		StringBuffer sb = new StringBuffer();
		
		if ( trackIDs != null ) {
			for ( int x=0; x < trackIDs.length; x++ )
			{
				if ( sb.length() > 0 )
					sb.append( "," );
				sb.append( trackIDs[ x ] );
			}
		}
		
		return "PartnerTransferAction: "
				+ "userID [" + userID + "], "
				+ "partnerID [" + partnerID + "], "
				+ "trackIDs [" + sb.toString() + "]";
	}

	public String getIpAddress() {
		return this.ipAddress;
	}
	public void setIpAddress( String ipAddr ) {
		this.ipAddress = ipAddr;
	}

	public int getUserID() {
		return this.userID;
	}
	public void setUserID( int userID ) {
		this.userID = userID;
	}


	
	public int getPartnerID() {
		return this.partnerID;
	}


	
	public void setPartnerID( int partnerID ) {
		this.partnerID = partnerID;
	}


	
	public int[] getTrackIDs() {
		return this.trackIDs;
	}


	
	public void setTrackIDs( int[] trackIDs ) {
		this.trackIDs = trackIDs;
	}

}
