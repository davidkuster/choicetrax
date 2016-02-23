package com.choicetrax.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RecordLabel 
	implements IsSerializable, TrackComponent 
{
	
	private int labelID;
	private String labelName;
	//private String legitName;
	
	
	public RecordLabel() {
		super();
	}
	
	
	public int getLabelID() {
		return labelID;
	}
	public void setLabelID(int labelID) {
		this.labelID = labelID;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	/*public String getLegitName() {
		return legitName;
	}
	public void setLegitName( String legit ) {
		this.legitName = legit;
	}*/

	
	// TrackComponent interface methods
	public int getID() {
		return getLabelID();
	}
	
	public String getName() {
		return getLabelName();
	}
	
	
	public String toString()
	{
		return "Label [" + getLabelName() + "] "
				+ "ID [" + getLabelID() + "] ";
	}
	
}
