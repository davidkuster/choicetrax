package com.choicetrax.client.data.config;

import com.choicetrax.client.actions.responses.LoaderResponse;


public class ChoicetraxInitialData 
	implements LoaderResponse 
{
	
	private ChoicetraxConfigData configData 	= null;
	private AdImages imgLoad 					= null;
	private AdFeaturedReleases featuredReleases	= null;
	
	
	
	public ChoicetraxInitialData() {
		super();
	}
	
	
	public ChoicetraxConfigData getConfigData() {
		return configData;
	}
	public void setConfigData(ChoicetraxConfigData configData) {
		this.configData = configData;
	}
	
	public AdImages getImgLoad() {
		return imgLoad;
	}
	public void setImgLoad(AdImages imgLoad) {
		this.imgLoad = imgLoad;
	}
	
	public AdFeaturedReleases getFeaturedReleases() {
		return featuredReleases;
	}
	public void setFeaturedReleases(AdFeaturedReleases featuredReleases) {
		this.featuredReleases = featuredReleases;
	}

}
