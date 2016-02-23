package com.choicetrax.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Partner 
	implements IsSerializable, MultiSelectItem
{
	
	private int partnerID;
	private String partnerName;
	private String partnerWebURL;
	private String partnerIconFilename;
	private String currency;
	
	private boolean selected = false;
	
	
	public Partner() {
		super();
	}
	
	
	public String getPartnerWebURL() {
		return partnerWebURL;
	}
	public void setPartnerWebURL(String partnerWebURL) {
		this.partnerWebURL = partnerWebURL;
	}
	public String getPartnerIconFilename() {
		return partnerIconFilename;
	}
	public void setPartnerIconFilename(String partnerIconFilename) {
		this.partnerIconFilename = partnerIconFilename;
	}
	public int getPartnerID() {
		return partnerID;
	}
	public void setPartnerID(int partnerID) {
		this.partnerID = partnerID;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency( String currency ) {
		this.currency = currency;
	}
	
	public int getID() {
		return partnerID;
	}
	public String getName() {
		return partnerName;
	}
	public String getShortName() {
		return null;
	}
	public String getIconURL() {
		return "img/buyicons/" + partnerIconFilename;
	}
	
	public boolean isSelected() {
		return selected;
	}	
	public void setSelected( boolean b ) {
		this.selected = b;
	}
	
	public MultiSelectItem clone()
	{
		Partner cloneObj = new Partner();
		cloneObj.setPartnerIconFilename( getPartnerIconFilename() );
		cloneObj.setPartnerID( getPartnerID() );
		cloneObj.setPartnerName( getPartnerName() );
		cloneObj.setPartnerWebURL( getPartnerWebURL() );
		cloneObj.setSelected( isSelected() );
		
		return cloneObj;
	}
	
	
	public String toString()
	{
		return "PartnerName [" + getPartnerName() + "] "
				+ "ID [" + getPartnerID() + "] ";
	}

}
