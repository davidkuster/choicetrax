package com.choicetrax.client.data;

import com.google.gwt.user.client.ui.Panel;


public class AudioPlaylistItem 
{
	
	private ReleaseDetail rd = null;
	private int partnerID = -1;
	private Panel displayPanel = null;

	
	@SuppressWarnings("unused")
	private AudioPlaylistItem() {
		super();
	}
	
	
	public AudioPlaylistItem( ReleaseDetail rd, int pID )
	{
		this.rd = rd;
		this.partnerID = pID;
	}
	
	
	public ReleaseDetail getReleaseDetail() {
		return rd;
	}
	
	public int getPartnerID() {
		return partnerID;
	}
	
	public void setDisplayPanel( Panel p ) {
		this.displayPanel = p;
	}
	
	public Panel getDisplayPanel() {
		return displayPanel;
	}
		
	
	public String toString() 
	{
		return "ReleaseDetail [" + rd + "] "
				+ "PartnerID [" + partnerID + "] ";
	}
	
}
