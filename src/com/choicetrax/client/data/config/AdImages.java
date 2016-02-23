package com.choicetrax.client.data.config;

import com.google.gwt.user.client.rpc.IsSerializable;


public class AdImages 
	implements IsSerializable 
{
	
	private AdImageQueue bannerAds = null;
	private AdImageQueueHash paidTargetedAds = new AdImageQueueHash();
	private AdImageQueueHash freeTargetedAds = new AdImageQueueHash();
	
	private AdImageQueue topPaidTargetedAds = null;
	private AdImage topPaidBannerAd = null;
	
	
	public AdImages() {
		super();
	}
	
	
	
	public void setBannerAds( AdImageQueue bannerAds ) {
		this.bannerAds = bannerAds;
	}
	
	public void setPaidTargetedAds( int genreID, AdImageQueue genreAds ) {
		paidTargetedAds.addQueue( genreID, genreAds );
	}
	
	public void setFreeTargetedAds( int genreID, AdImageQueue genreAds ) {
		freeTargetedAds.addQueue( genreID, genreAds );
	}
	
	public void setTopPaidTargetedAds( AdImageQueue topPaidAds ) {
		this.topPaidTargetedAds = topPaidAds;
	}
	
	public void setTopPaidBannerAd( AdImage topBannerAd ) {
		this.topPaidBannerAd = topBannerAd;
	}
	
	public AdImageQueue getBannerAds() {
		return bannerAds;
	}
	
	public AdImageQueue getPaidTargetedAds( int genreID ) {
		return this.paidTargetedAds.lookup( genreID );
	}
	
	public AdImageQueue getFreeTargetedAds( int genreID ) {
		return this.freeTargetedAds.lookup( genreID );
	}
	
	public AdImageQueueHash getPaidTargetedAdsHashtable() {
		return this.paidTargetedAds;
	}
	
	public AdImageQueueHash getFreeTargetedAdsHashtable() {
		return this.freeTargetedAds;
	}
	
	public AdImageQueue getTopPaidTargetedAds() {
		return this.topPaidTargetedAds;
	}
	
	public AdImage getTopPaidBannerAd() {
		return this.topPaidBannerAd;
	}

}
