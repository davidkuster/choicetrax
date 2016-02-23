package com.choicetrax.server.data;


public class EchoNestRequestObj
{

	private int trackID = 0;
	private String audioPreviewURL = null;
	
	
	public EchoNestRequestObj( int trackID, String audioPreviewURL ) {
		this.trackID = trackID;
		this.audioPreviewURL = audioPreviewURL;
	}
	
	
	public int getTrackID() { 
		return trackID;
	}
	
	public String getAudioPreviewURL() {
		return audioPreviewURL;
	}
	
	public boolean equals( EchoNestRequestObj obj ) 
	{
		if ( trackID == obj.getTrackID() )
			return true;
		else
			return false;
	}
	
}
