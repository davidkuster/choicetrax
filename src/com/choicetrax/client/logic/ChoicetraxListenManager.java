package com.choicetrax.client.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.choicetrax.client.Choicetrax;
import com.choicetrax.client.actions.loaderactions.LoadReleasesAction;
import com.choicetrax.client.actions.loaderactions.RequestBpmAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.AudioPlaylist;
import com.choicetrax.client.data.AudioPlaylistItem;
import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.display.AudioPlayer;
import com.choicetrax.client.logic.callbacks.AudioPlaylistLoadController;
import com.choicetrax.client.logic.callbacks.AudioSavedPlaylistLoadController;
import com.choicetrax.client.logic.callbacks.RequestBpmController;
import com.choicetrax.client.util.exception.AudioPlayerException;

import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;


public class ChoicetraxListenManager 
{
	
	private ChoicetraxViewManager viewManager = null;
	
	private AudioPlayer smallTopPlayer = null;
	private AudioPlayer fullRightPlayer = null;
	
	private AudioPlaylist playlist = new AudioPlaylist();
	
	private boolean isPlaying = false;
	private boolean autoStart = true;
	private boolean isQueued = false;
	
	
	public ChoicetraxListenManager( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		initJS( this );
	}
	
	public void initialize() {
		loadSavedPlaylist();
	}
	
	public void setTopPlayer( AudioPlayer small ) {
		this.smallTopPlayer = small;
	}
	
	public void setFullPlayer( AudioPlayer full ) {
		this.fullRightPlayer = full;
	}
	
	
	
	private void loadSavedPlaylist()
	{
		String cookie = Choicetrax.getCookie( Constants.COOKIE_PLAYLIST );
		if ( cookie != null )
		{
			// trackID, partnerID
			LinkedHashMap<Integer, Integer> partnerMap = new LinkedHashMap<Integer, Integer>();
			
			boolean loop = true;
			while ( loop )
			{
				int delimIndex = cookie.indexOf( '|' );
				if ( delimIndex == -1 ) 
				{
					delimIndex = cookie.length();
					loop = false;
				}
				
				// item format "t=12345&p=1"
				String item = cookie.substring( 0, delimIndex );
				String trackID = item.substring( 2, item.indexOf( "&" ) );
				String partnerID = item.substring( item.indexOf( "p=" ) + 2 );
				
				partnerMap.put( Integer.parseInt( trackID ),
								Integer.parseInt( partnerID ) );
				
				if ( loop )
					cookie = cookie.substring( delimIndex + 1 );
			}
			
			int[] trackIDs = new int[ partnerMap.size() ];
			Iterator<Integer> i = partnerMap.keySet().iterator();
			for ( int x=0; i.hasNext(); x++ )
			{
				int trackID = (int) i.next();
				trackIDs[ x ] = trackID;
			}
			
			LoadReleasesAction action = new LoadReleasesAction( trackIDs );
			if ( viewManager.getCurrentUser() != null )
				action.setUserID( viewManager.getCurrentUser().getUserID() );
			
			viewManager.deferAction( action, 
									 new AudioSavedPlaylistLoadController( viewManager, partnerMap ) );
		}
	}
	
	
	private native void initJS( ChoicetraxListenManager clm ) /*-{
		$wnd.playbackComplete = function playbackComplete() {
			clm.@com.choicetrax.client.logic.ChoicetraxListenManager::jsPlaybackComplete()();
		}
		
		$wnd.loadingError = function loadingError(str) {
			clm.@com.choicetrax.client.logic.ChoicetraxListenManager::jsLoadingError(Ljava/lang/String;)(str);
		}
	}-*/;
	
	
	//$wnd.hello( url );
	private native void jsLoadAndPlay( String url ) /*-{
		$wnd.loadAndPlay(url);
	}-*/;
	
	private native void jsRestartPlayback() /*-{
		$wnd.restartPlayback();
	}-*/;
	
	private native void jsPausePlayback() /*-{
		$wnd.pausePlayback();
	}-*/;
	
	private native void jsStopPlayback() /*-{
	 	$wnd.stopPlayback();
	}-*/;
	
	/*private native void jsShowAudioPlayer( int top, int left ) /*-{
		$wnd.showAudioPlayer( top, left );
	}-*/;
	
	/*private native void jsHideAudioPlayer() /*-{
		$wnd.hideAudioPlayer();
	}-*/;
	
	
	
	public void jsPlaybackComplete()
	{
		setPlaying( false );
		
		if ( isQueued )
			playNextPlaylistItem();
	}
	
	public void jsLoadingError( String errorMsg )
	{
		viewManager.handleError( new AudioPlayerException( errorMsg ),
								 this.getClass().getName() + ".jsLoadingError()" );
		
		if ( isQueued )
			playNextPlaylistItem();
	}
	
	
	public void setAutoStart( boolean start ) {
		this.autoStart = start;
	}
	
	
	public void setPlaying( boolean playing ) 
	{
		this.isPlaying = playing;
		
		smallTopPlayer.setPlaying( isPlaying );
		fullRightPlayer.setPlaying( isPlaying );
	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public boolean isQueued() {
		return isQueued;
	}
	
	public void setQueued( boolean queued ) {
		this.isQueued = queued;
		
		smallTopPlayer.setQueued( isQueued );
		fullRightPlayer.setQueued( isQueued );
	}
	
		
	
	
	public void addToPlaylist( ReleaseDetail rd, int partnerID )
	{
		AudioPlaylistItem playlistItem = null;
		
		// check if trackID w/partner clip already exists in playlist.
		if ( playlist.isInPlaylist( rd.getTrackID(), partnerID ) )
		{
			// if so, use item already in playlist
			playlistItem = playlist.getPlaylistItem( rd.getTrackID(), partnerID );
			// note that this will only be used if tracks are
			// not being queued
		}
		else
		{
			// otherwise create new AudioPlaylistItem obj
			playlistItem = new AudioPlaylistItem( rd, partnerID );
			
			// add to end of playlist
			playlist.add( playlistItem );
			fullRightPlayer.addToPlaylistPanel( playlistItem );
		}
		
		if ( autoStart 
			&& ( ( ! isQueued ) 
				|| ( ! isPlaying ) ) )
			updateTrackInfo( playlistItem );
	}
	
	
	public void updateTrackInfo( AudioPlaylistItem playlistItem )
	{
		ReleaseDetail rd 	= playlistItem.getReleaseDetail();
		int partnerID 	 	= playlistItem.getPartnerID();
		PartnerInventory pi = rd.getPartnerInventory( partnerID );	
		
		// call javascript play function
		loadAndPlay( pi.getAudioPreviewURL() );
		
		// keep track of currently playing item in playlist obj
		setPlayingItem( playlistItem );
		
		// change play button to pause button
		setPlaying( true );
		
		smallTopPlayer.updateTrackInfo( playlistItem );
		fullRightPlayer.updateTrackInfo( playlistItem );
		
		//if ( rd.getBpm() == 0 )
		//	requestBPM( playlistItem );
	}
	
	
	private void requestBPM( AudioPlaylistItem playlistItem )
	{
		ReleaseDetail rd = playlistItem.getReleaseDetail();
		int trackID = rd.getTrackID();
		int partnerID = playlistItem.getPartnerID();
		String audioPreviewURL = rd.getPartnerInventory( partnerID ).getAudioPreviewURL();
		
		viewManager.deferAction( new RequestBpmAction( trackID, audioPreviewURL ), 
								 new RequestBpmController( viewManager ) );
	}
	
	
	public void setPlayingItem( AudioPlaylistItem playlistItem )
	{
		playlist.setPlayingItem( playlistItem );
	}
	
	public AudioPlaylistItem getPlayingItem() 
	{
		return playlist.getPlayingItem();
	}
	
	public void removePlaylistItem( AudioPlaylistItem playlistItem )
	{
		playlist.remove( playlistItem );
	}
	
	
	public void playPreviousPlaylistItem()
	{
		AudioPlaylistItem playlistItem = playlist.getPrevPlaylistItem();
		if ( playlistItem != null )
			updateTrackInfo( playlistItem );
	}
	
	
	
	public void playNextPlaylistItem()
	{
		AudioPlaylistItem playlistItem = playlist.getNextPlaylistItem();
		if ( playlistItem != null )
			updateTrackInfo( playlistItem );
	}
	
	
	public void loadAndPlay( String url )
	{
		jsLoadAndPlay( url );
	}
	
	public void restartPlayback() 
	{
		jsRestartPlayback();
		setPlaying( true );
	}
	
	public void pausePlayback() 
	{
		jsPausePlayback();
		setPlaying( false );
	}
	
	public void stopPlayback()
	{
		jsStopPlayback();
		setPlaying( false );
	}
	
	
	
	public void savePlaylist()
	{
		StringBuffer cookie = new StringBuffer();
		
		LinkedList<AudioPlaylistItem> list = playlist.getPlaylist();
		if ( list.size() == 0 ) return;
			
		Iterator<AudioPlaylistItem> i = list.iterator();
		while ( i.hasNext() )
		{
			AudioPlaylistItem item = i.next();
			int trackID = item.getReleaseDetail().getTrackID();
			int partnerID = item.getPartnerID();
			
			// write trackID & partnerID to cookies
			if ( cookie.length() > 0 ) cookie.append( "|" );
			
			cookie.append( "t=" + trackID + "&p=" + partnerID );
			
			// everytime they remove a track from the playlist
			// do i remove it from the cookies ???  probably not...
		}
		
		Choicetrax.setCookie( Constants.COOKIE_PLAYLIST, 
								cookie.toString(),
								Constants.COOKIE_DURATION_1WEEK );
		
		Window.alert( "Playlist has been saved to browser cookies for 1 week." );
	}
	
	
	public void clearPlaylist()
	{
		playlist.clearPlaylist();
		
		// clear playlist cookie
		Choicetrax.setCookie( Constants.COOKIE_PLAYLIST, null );
	}	
	
	
	public int getPlaylistSize() {
		return playlist.size();
	}
	
	
	public void playPage( ReleasesCache cache, int pageNum )
	{
		Releases releases = cache.getReleasesForPage( pageNum );
		
		if ( ( releases != null ) && ( releases.size() > 0 ) )
		{
			addReleasesToPlaylist( releases );
		}
		else
		{
			int[] trackIDs = cache.getIDsForPage( pageNum );
			LoadReleasesAction action = new LoadReleasesAction( trackIDs );
			if ( viewManager.getCurrentUser() != null )
				action.setUserID( viewManager.getCurrentUser().getUserID() );
			
			viewManager.deferAction( action, new AudioPlaylistLoadController( viewManager ) );
		}
	}
	
	
	public void playRandom( ReleasesCache cache )
	{
		ArrayList<String> trackIDs = cache.getTrackIDsList();
		int size = Math.min( trackIDs.size(), 100 );
		ArrayList<Integer> idsToLoad = new ArrayList<Integer>();
		
		for ( int x=0; x < size; x++ ) 
		{
			int index = Random.nextInt( trackIDs.size() );
			String id = trackIDs.remove( index );
			idsToLoad.add( Integer.parseInt( id ) );
		}
		
		if ( idsToLoad.size() > 0 )
		{
			while ( idsToLoad.size() > 0 )
			{
				// send requests to server in groups of 10
				int[] ids = new int[ 10 ];
				int size2 = Math.min( idsToLoad.size(), 10 );
				for ( int x=0; x < size2; x++ ) {
					ids[ x ] = idsToLoad.remove( 0 );
				}
				
				LoadReleasesAction action = new LoadReleasesAction( ids );
				// turn off server-side ordering to increase randomness
				action.setOrdering( false );
				
				if ( viewManager.getCurrentUser() != null )
					action.setUserID( viewManager.getCurrentUser().getUserID() );
				
				viewManager.deferAction( action, new AudioPlaylistLoadController( viewManager ) );
			}
		}
	}
	
	
	public void playAll( ReleasesCache cache )
	{
		int numPages = cache.getNumPages();
		for ( int x=1; x <= numPages; x++ )
		{
			playPage( cache, x );
		}
	}
	
	
	public void addReleasesToPlaylist( Releases releases )
	{
		Iterator<ReleaseDetail> i = releases.getReleasesList().iterator();
		while ( i.hasNext() )
		{
			ReleaseDetail rd = i.next();
			int partnerID = rd.getPartnerAvailability().get( 0 ).getPartnerID();
			
			if ( this.isPlaying() )
				this.setAutoStart( false );
			
			viewManager.getUserManager().listenTrack( rd, partnerID );
		}

		this.setAutoStart( true );
	}

}
