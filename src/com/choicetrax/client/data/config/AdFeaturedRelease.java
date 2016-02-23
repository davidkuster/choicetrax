package com.choicetrax.client.data.config;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.ReleaseDetail;


public class AdFeaturedRelease 
	implements IsSerializable 
{
	
	private Genre targetGenre	 		= null;
	private ReleaseDetail releaseDetail = null;
	
	
	public AdFeaturedRelease() { }
	
	
	public Genre getTargetGenre() {
		return targetGenre;
	}
	public void setTargetGenre(Genre targetGenre) {
		this.targetGenre = targetGenre;
	}
	public ReleaseDetail getReleaseDetail() {
		return releaseDetail;
	}
	public void setReleaseDetail(ReleaseDetail releaseDetail) {
		this.releaseDetail = releaseDetail;
	}
	

}
