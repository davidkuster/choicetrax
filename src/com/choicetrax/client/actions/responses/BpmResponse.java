package com.choicetrax.client.actions.responses;


public class BpmResponse implements LoaderResponse
{
	
	private int trackID = -1;
	private float bpm = 0f;
	private float confidence = 0f;
	
	
	@SuppressWarnings("unused")
	private BpmResponse() {
		super();
	}
	
	
	public BpmResponse( int trackID, float bpm, float confidence ) {
		super();
		this.trackID = trackID;
		this.bpm = bpm;
		this.confidence = confidence;
	}
	
	
	public int getTrackID() {
		return this.trackID;
	}
	
	public float getBPM() {
		return this.bpm;
	}
	
	public float getConfidence() {
		return this.confidence;
	}

}
