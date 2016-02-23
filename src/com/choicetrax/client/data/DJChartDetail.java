package com.choicetrax.client.data;

import java.util.LinkedList;

import com.choicetrax.client.data.cache.CacheObject;
import com.choicetrax.client.data.cache.ReleasesCache;


public class DJChartDetail implements CacheObject
{
	
	private int chartID = 0;
	private String chartName = null;
	private String chartDate = null;
	private int partnerID = 0;
	private String partnerChartID = null;
	private Artist artist = null;
	private String artistName = null;
	private String imageURL = null;
	private ReleasesCache chartTracks = null;
	private LinkedList<Genre> genres;
	
	
	public DJChartDetail() {
		super();
	}
	
	
	public int getChartID() {
		return this.chartID;
	}
	
	public void setChartID( int chartID ) {
		this.chartID = chartID;
	}
	
	public String getChartName() {
		return this.chartName;
	}
	
	public void setChartName( String chartName ) {
		this.chartName = chartName;
	}
	
	public String getChartDate() {
		return this.chartDate;
	}
	
	public void setChartDate( String chartDate ) {
		this.chartDate = chartDate;
	}
	
	public int getPartnerID() {
		return this.partnerID;
	}
	
	public void setPartnerID( int partnerID ) {
		this.partnerID = partnerID;
	}
	
	public String getPartnerChartID() {
		return this.partnerChartID;
	}
	
	public void setPartnerChartID( String partnerChartID ) {
		this.partnerChartID = partnerChartID;
	}
	
	public Artist getArtist() {
		return this.artist;
	}
	
	public void setArtist( Artist artist ) {
		this.artist = artist;
	}
	
	public String getArtistName() {
		return this.artistName;
	}
	
	public void setArtistName( String artistName ) {
		this.artistName = artistName;
	}
	
	public String getImageURL() {
		return this.imageURL;
	}
	
	public void setImageURL( String imageURL ) {
		this.imageURL = imageURL;
	}
	
	public ReleasesCache getChartTracks() {
		return this.chartTracks;
	}
	
	public void setChartTracks( ReleasesCache tracks ) {
		this.chartTracks = tracks;
	}

	
	public LinkedList<Genre> getGenres() {
		return this.genres;
	}
	
	public void setGenres( LinkedList<Genre> genres ) {
		this.genres = genres;
	}

}
