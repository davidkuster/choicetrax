package com.choicetrax.client.data;

import java.util.LinkedList;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.choicetrax.client.data.Format;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class PartnerInventory implements IsSerializable
{
    private int partnerID;
    private String partnerTrackID;
    private String imageURL;
    private String audioPreviewURL;
    private LinkedList<Format> formatAndPrices;
    private String territoryRestrictions;
    private int releaseID;
    private int releaseTrackID;
    
    
    public PartnerInventory() {
    	super();
    }
    
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append( "PartnerInventory: " );
    	
    	if ( partnerID > 0 ) 			sb.append( "partnerID [" + partnerID + "] " );
    	if ( partnerTrackID != null )	sb.append( "partnerTrackID [" + partnerTrackID + "] " );
    	if ( formatAndPrices != null )	sb.append( "formatAndPrices [" + formatAndPrices + "] " );
    	
    	return sb.toString();
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
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public List<Format> getFormatAndPrices() {
		return formatAndPrices;
	}
	
	public void setFormatAndPrices(LinkedList<Format> formatAndPrices) {
		this.formatAndPrices = formatAndPrices;
	}

	public String getAudioPreviewURL() {
		return audioPreviewURL;
	}

	public void setAudioPreviewURL(String audioPreviewURL) {
		this.audioPreviewURL = audioPreviewURL;
	}
	
	public String getTerritoryRestrictions() {
		return territoryRestrictions;
	}
	
	public void setTerritoryRestrictions( String territoryRestrictions ) {
		this.territoryRestrictions = territoryRestrictions;
	}


	
	public int getReleaseID() {
		return this.releaseID;
	}


	
	public void setReleaseID( int releaseID ) {
		this.releaseID = releaseID;
	}


	
	public int getReleaseTrackID() {
		return this.releaseTrackID;
	}


	
	public void setReleaseTrackID( int releaseTrackID ) {
		this.releaseTrackID = releaseTrackID;
	}

}