package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;


public class RequestBpmAction implements LoaderAction
{
	
	private int trackID = -1;
	private String audioPreviewURL = null;
	
	
	@SuppressWarnings("unused")
	private RequestBpmAction() {
		super();
	}
	
	
	public RequestBpmAction( int trackID, String audioClipURL ) {
		super();
		this.trackID = trackID;
		this.audioPreviewURL = audioClipURL;
	}
	
	
	
	public int getTrackID() {
		return this.trackID;
	}
	
	public String getAudioPreviewURL() {
		return this.audioPreviewURL;
	}

	
	
	public String getLogString() {
		return "RequestBpmAction: trackID [" + trackID + "]";
	}

}
