package com.choicetrax.client.data.config;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Country 
	implements IsSerializable
{
	
	private int countryID = -1;
	private String countryCode = null;
	private String countryName = null;
	
	
	public Country() {
		super();
	}

	
	public int getCountryID() {
		return this.countryID;
	}
	
	public void setCountryID( int countryID ) {
		this.countryID = countryID;
	}
	
	public String getCountryCode() {
		return this.countryCode;
	}
	
	public void setCountryCode( String countryCode ) {
		this.countryCode = countryCode;
	}
	
	public String getCountryName() {
		return this.countryName;
	}
	
	public void setCountryName( String countryName ) {
		this.countryName = countryName;
	}
	
}
