package com.choicetrax.client.display;

import com.choicetrax.client.data.AudioPlaylistItem;


public interface AudioPlayer 
{
	
	
	public void updateTrackInfo( AudioPlaylistItem playlistItem );
	
	public void setPlaying( boolean playing );
	
	public void setQueued( boolean queued );
	
	public void addToPlaylistPanel( AudioPlaylistItem playlistItem );
	

}
