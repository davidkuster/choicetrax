package com.choicetrax.client.data;

import java.util.Iterator;
import java.util.LinkedList;


public class AudioPlaylist 
{

	private LinkedList<AudioPlaylistItem> playlist = null;
	private LinkedList<String> playlistIndex = null;
	
	private AudioPlaylistItem playingItem = null;
	
	
	public AudioPlaylist() {
		super();
		playlist = new LinkedList<AudioPlaylistItem>();
		playlistIndex = new LinkedList<String>();
	}
	
	
	public void add( AudioPlaylistItem playlistItem ) 
	{
		playlist.add( playlistItem );
		playlistIndex.add( createPlaylistIndexKey( playlistItem ) );
	}
	
	
	public AudioPlaylistItem getNextPlaylistItem() 
	{
		int playingItemIndex = playlist.indexOf( playingItem );
		if ( playingItemIndex < playlist.size() - 1 )
		{
			return playlist.get( playingItemIndex + 1 );
		}
		else
			return null;
	}
	
	
	public AudioPlaylistItem getPrevPlaylistItem()
	{
		int playingItemIndex = playlist.indexOf( playingItem );
		if ( playingItemIndex > 0 )
		{
			return playlist.get( playingItemIndex - 1 );
		}
		else
			return null;
	}
	
	
	/**
	 * if this is going to be called every time before a new track
	 * is added to playlist, look at ways to increase performance
	 * here.  (possibly an additional data structure in addition
	 * to the LinkedList?  just an index of trackID and partnerID,
	 * with no need to contain the full AudioPlaylistItem objects?)
	 * 
	 * on the flip side, if we just remove the AudioPlaylistItem's
	 * displayPanel from its parent and re-add the same panel to
	 * the parent we don't need to reconstruct that displayPanel.
	 * that is time saved...  
	 * 
	 * maybe make a contains() method using a new data structure.
	 * if it's in the playlist, then use this method to get the
	 * existing AudioPlaylistItem obj & re-use its displayPanel.
	 * 
	 * @param trackID
	 * @param partnerID
	 * @return
	 */
	public AudioPlaylistItem getPlaylistItem( int trackID, int partnerID ) 
	{
		Iterator<AudioPlaylistItem> i = playlist.iterator();
		while ( i.hasNext() )
		{
			AudioPlaylistItem playlistItem = i.next();
			
			if ( ( trackID == playlistItem.getReleaseDetail().getTrackID() )
				&& ( partnerID == playlistItem.getPartnerID() ) )
			{
				return playlistItem;
			}
		}
		
		return null;
	}
	
	
	public void clearPlaylist()
	{
		playlist.clear();
		playlistIndex.clear();
		playingItem = null;
	}
	
	
	public boolean isInPlaylist( int trackID, int partnerID )
	{
		String indexKey = createPlaylistIndexKey( trackID, partnerID );
		return playlistIndex.contains( indexKey );
	}
	
	
	public void remove( AudioPlaylistItem playlistItem ) 
	{
		playlist.remove( playlistItem );
		playlistIndex.remove( createPlaylistIndexKey( playlistItem ) );
	}
	
	
	public LinkedList<AudioPlaylistItem> getPlaylist() {
		return playlist;
	}
	
	
	public AudioPlaylistItem getPlayingItem() {
		return playingItem;
	}
	
	public void setPlayingItem( AudioPlaylistItem playlistItem ) {
		this.playingItem = playlistItem;
	}
	
	
	public int size() {
		return playlist.size();
	}
	
	
	
	private String createPlaylistIndexKey( AudioPlaylistItem playlistItem )
	{
		int trackID = playlistItem.getReleaseDetail().getTrackID(); 
		int partnerID = playlistItem.getPartnerID();
		
		return createPlaylistIndexKey( trackID, partnerID );
	}
	
	private String createPlaylistIndexKey( int trackID, int partnerID )
	{
		return "trackID=" + trackID + "partnerID=" + partnerID;
	}
	
}
