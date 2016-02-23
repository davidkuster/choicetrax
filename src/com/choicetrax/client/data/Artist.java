package com.choicetrax.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Artist 
	implements IsSerializable, TrackComponent 
{
	
	private int artistID;
	private String artistName;
	private String legitName;
	
	
	public Artist() { 
		super();
	}
	
	
	public int getArtistID() {
		return artistID;
	}
	public void setArtistID(int artistID) {
		this.artistID = artistID;
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}
	public String getLegitName() {
		return legitName;
	}
	public void setLegitName( String legit ) {
		this.legitName = legit;
	}
	
	
	// TrackComponent interface methods
	public int getID() {
		return getArtistID();
	}
	
	public String getName() {
		return getArtistName();
	}
	
	
	public String toString() 
	{
		return "Artist [" + getArtistName() + "] "
				+ "ID [" + getArtistID() + "] ";
	}

}
