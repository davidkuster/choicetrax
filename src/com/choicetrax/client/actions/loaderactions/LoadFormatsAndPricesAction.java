package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;


public class LoadFormatsAndPricesAction 
	implements LoaderAction 
{
	
	private int trackID = -1;
	private int partnerID = -1;
	private String partnerTrackID = null;
	
	
	public LoadFormatsAndPricesAction() { 
		super();
	}
	
	
	public String getLogString() {
		return "LoadFormatsAndPricesAction: "
				+ "trackID [" + trackID + "] "
				+ "partnerID [" + partnerID + "] "
				+ "partnerTrackID [" + partnerTrackID + "]";
	}


	public int getPartnerID() {
		return partnerID;
	}


	public void setPartnerID(int partnerID) {
		this.partnerID = partnerID;
	}


	public String getPartnerTrackID() {
		return partnerTrackID;
	}


	public void setPartnerTrackID(String partnerTrackID) {
		this.partnerTrackID = partnerTrackID;
	}
	

	public int getTrackID() {
		return trackID;
	}
	
	public void setTrackID( int id ) {
		this.trackID = id;
	}
	
}
