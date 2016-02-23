package com.choicetrax.client.data;

import java.util.LinkedList;
import java.util.Iterator;

import com.choicetrax.client.data.cache.CacheObject;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class ReleaseDetail 
	implements CacheObject
{
	
    private Artist artist;
    private int trackID;
    private String trackName;
    private String albumName;
    private String mixName;
    private Artist remixer;
    private Artist vocalist;
    private RecordLabel label;
    private String labelCatalogNum;
    private String timeLength;
    private LinkedList<Genre> genres;
    private String description;
    private float trackRating;
    private int numTrackRatings;
    private int userRating;
    private LinkedList<PartnerInventory> partnerAvailability;
    private java.util.Date releaseDate;
    private String bpm;
    private String bpmConfidence;
    
    
    public ReleaseDetail() { 
    	super();
    }
    
    
    public String toString()
    {
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append( "ReleaseDetail: " );
    	
    	if ( artist != null )			sb.append( "artist [" + artist.getArtistName() + "] " );
    	if ( trackName != null )		sb.append( "track [" + trackName + "] " );
    	if ( mixName != null )			sb.append( "mix [" + mixName + "] " );
    	if ( remixer != null && ( remixer.getArtistName() != null ) )			
    									sb.append( "remixer [" + remixer.getArtistName() + "] " );
     	if ( albumName != null )		sb.append( "album [" + albumName + "] " );
    	if ( label != null )			sb.append( "label [" + label.getLabelName() + "] " );
    	if ( labelCatalogNum != null )	sb.append( "labelCatNum [" + labelCatalogNum + "] " );
    	if ( description != null )		sb.append( "description [" + description + "] " );
       	if ( releaseDate != null )		sb.append( "releaseDate [" + releaseDate + "] " );
    	if ( timeLength != null )		sb.append( "time [" + timeLength + "] " );
    	if ( partnerAvailability != null )	sb.append( "inventory [" + partnerAvailability + "] " );
    	
    	sb.append( "trackID [" + this.getTrackID() + "]" );
    	
    	return sb.toString();
    }
    
    
    public String getTitle()
    {
    	StringBuffer sb = new StringBuffer();
    	
    	if ( artist != null )			sb.append( artist.getArtistName() );
    	if ( trackName != null )		sb.append( " - " + trackName );
    	if ( mixName != null )			sb.append( " (" + mixName + ") " );
    	if ( label != null )			sb.append( " - " + label.getLabelName() );
    	
    	return sb.toString();
    }
    
    /**
     * get the first cover art URL found.  if none found, return null.
     * @return
     */
    public String getCoverArtURL()
    {
    	if ( partnerAvailability != null )
    	{
    		Iterator<PartnerInventory> i = partnerAvailability.iterator();
    		while ( i.hasNext() )
    		{
    			PartnerInventory pi = i.next();
    			if ( pi.getImageURL() != null )
    				return pi.getImageURL();
    		}
    	}
    
    	return null;
    }
    
    
    public PartnerInventory getPartnerInventory( int partnerID )
    {
    	if ( partnerAvailability == null ) return null;
    	
    	Iterator<PartnerInventory> i = partnerAvailability.iterator();
    	while ( i.hasNext() )
    	{
    		PartnerInventory pi = i.next();
    		if ( pi.getPartnerID() == partnerID )
    			return pi;
    	}
    	
    	return null;
    }
    
	public Artist getArtist() {
		return artist;
	}
	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	public int getTrackID() {
		return trackID;
	}
	public void setTrackID(int trackID) {
		this.trackID = trackID;
	}
	public String getTrackName() {
		return trackName;
	}
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	public String getMixName() {
		return mixName;
	}
	public void setMixName(String mixName) {
		this.mixName = mixName;
	}
	public Artist getRemixer() {
		return remixer;
	}
	public void setRemixer(Artist remixerArtist) {
		this.remixer = remixerArtist;
	}
	public RecordLabel getLabel() {
		return label;
	}
	public void setLabel(RecordLabel label) {
		this.label = label;
	}
	public String getLabelCatalogNum() {
		return labelCatalogNum;
	}
	public void setLabelCatalogNum(String labelCatalogNum) {
		this.labelCatalogNum = labelCatalogNum;
	}
	public String getTimeLength() {
		return timeLength;
	}
	public void setTimeLength(String timeLength) {
		this.timeLength = timeLength;
	}
	
	public LinkedList<Genre> getGenres() {
		return genres;
	}
	
	public void setGenres(LinkedList<Genre> genres) {
		this.genres = genres;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public float getTrackRating() {
		return trackRating;
	}
	public void setTrackRating(float trackRating) {
		this.trackRating = trackRating;
	}
	public int getNumTrackRatings() {
		return numTrackRatings;
	}
	public void setNumTrackRatings(int numTrackRatings) {
		this.numTrackRatings = numTrackRatings;
	}
	
	public LinkedList<PartnerInventory> getPartnerAvailability() {
		return partnerAvailability;
	}
	
	public void setPartnerAvailability(LinkedList<PartnerInventory> partnerAvailability) {
		this.partnerAvailability = partnerAvailability;
	}
	public java.util.Date getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(java.util.Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public int getUserRating() {
		return userRating;
	}
	public void setUserRating(int userRating) {
		this.userRating = userRating;
	}

	public Artist getVocalist() {
		return vocalist;
	}
	public void setVocalist(Artist vocalist) {
		this.vocalist = vocalist;
	}

	
	public String getBpm() {
		return this.bpm;
	}	
	public void setBpm( String bpm ) {
		this.bpm = bpm;
	}


	public String getBpmConfidence() {
		return this.bpmConfidence;
	}
	public void setBpmConfidence( String bpmConfidence ) {
		this.bpmConfidence = bpmConfidence;
	}

}