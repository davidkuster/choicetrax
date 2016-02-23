package com.choicetrax.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Genre
	implements IsSerializable, MultiSelectItem, TrackComponent
{
	
	private int genreID;
	private String genreName;
	private String genreShortName;
	
	protected boolean selected = false;
	
	
	
	public Genre() {
		super();
	}
	
	public Genre( int id, String name )
	{
		this.genreID = id;
		this.genreName = name;
	}
	
	
	public int getGenreID() {
		return genreID;
	}
	public void setGenreID(int genreID) {
		this.genreID = genreID;
	}
	public String getGenreName() {
		return genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	public String getGenreShortName() {
		return genreShortName;
	}
	public void setGenreShortName(String genreShortName) {
		this.genreShortName = genreShortName;
	}
	
	public boolean isSelected() {
		return selected;
	}	
	public void setSelected( boolean b ) {
		this.selected = b;
	}
	

	// TrackComponent interface methods
	public int getID() {
		return getGenreID();
	}
	
	public String getName() {
		return getGenreName();
	}
	
	public String getShortName() {
		return getGenreShortName();
	}
	
	public String getIconURL() {
		return null;
	}
	
	public MultiSelectItem clone()
	{
		Genre cloneObj = new Genre();
		cloneObj.setGenreID( getGenreID() );
		cloneObj.setGenreName( getGenreName() );
		cloneObj.setGenreShortName( getGenreShortName() );
		cloneObj.setSelected( isSelected() );
		
		return cloneObj;
	}
	
	
	public String toString() 
	{
		return "Genre [" + getGenreName() + "] "
				+ "ID [" + getGenreID() + "] ";
	}
	
}
