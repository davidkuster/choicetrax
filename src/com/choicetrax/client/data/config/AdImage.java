package com.choicetrax.client.data.config;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AdImage 
	implements IsSerializable 
{
	
	private int adID = -1;
	private String imageURL = null;
	private String linkURL = null;
	private String adType = null;
	
	
	public AdImage() { }
	
	
	public int getAdID() {
		return adID;
	}
	public void setAdID( int id ) {
		this.adID = id;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getLinkURL() {
		return linkURL;
	}
	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}
	public String getAdType() {
		return adType;
	}
	public void setAdType( String type ) {
		this.adType = type;
	}
	

}
