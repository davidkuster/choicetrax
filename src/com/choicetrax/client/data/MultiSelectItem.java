package com.choicetrax.client.data;

public interface MultiSelectItem
{

	public int getID();
	
	public String getName();
	
	public String getShortName();
	
	public String getIconURL();
	
	public MultiSelectItem clone();
	
	
	public boolean isSelected();
	
	public void setSelected( boolean b );

}
