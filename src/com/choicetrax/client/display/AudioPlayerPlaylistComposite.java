package com.choicetrax.client.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.ChoicetraxListenManager;
import com.choicetrax.client.data.*;
import com.choicetrax.client.display.panels.ReleaseRatingPanelUserOnly;
import com.choicetrax.client.input.*;


/**
 * This is audio player design 2 from the whiteboard.
 * It has a large area on the left that displays the
 * various track details.
 * 
 * @author David
 */
public class AudioPlayerPlaylistComposite 
	extends Composite
	implements AudioPlayer
{
	
	private ChoicetraxViewManager viewManager		= null;
	private ChoicetraxListenManager listenManager	= null;
	
	//private VerticalPanel trackInfoPanel 	= null;
		
	private VerticalPanel playlistPanel 	= null;
	private ScrollPanel playlistScrollPanel = null;
	private Widget lastPlaylistPanel 		= null;
	private Label playlistSizeLabel			= new Label( "(0)" );
	
	
	public AudioPlayerPlaylistComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		this.listenManager = viewManager.getListenManager();
				
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		initWidget( vp );
				
		vp.add( createPlaylistTitle() );
		vp.add( createPlaylistPanel() );
		
		vp.setStylePrimaryName( "audioPlayer" );
	}
	
	
	
	private Panel createPlaylistPanel()
	{
		playlistPanel = new VerticalPanel();
		
		playlistScrollPanel = new ScrollPanel( playlistPanel );
		playlistScrollPanel.setStyleName( "playlistScrollPanel" );
		
		// workaround necessary to get ScrollPanel to scroll
		VerticalPanel vp2 = new VerticalPanel();
		vp2.add( playlistScrollPanel );
		
		return vp2;
	}
	
	
	private Panel createPlaylistTitle()
	{
		HorizontalPanel hp = new HorizontalPanel();
		hp.setStylePrimaryName( "playlistTitlePanel" );
		
		Label save = new HyperlinkLabel( "save", "save playlist", new SavePlaylistListener() );
		
		Label title = new Label( "Playlist" );
		title.setStylePrimaryName( "playlistTitle" );
		
		playlistSizeLabel.setStylePrimaryName( "smallerText" );
		
		HorizontalPanel pp = new HorizontalPanel();
		pp.add( title );
		pp.add( playlistSizeLabel );
		
		Label clear = new HyperlinkLabel( "clear", "clear playlist", new ClearPlaylistListener() );
		
		hp.add( save );
		hp.add( pp );
		hp.add( clear );
		
		hp.setCellWidth( save, "33%" );
		hp.setCellWidth( title, "34%" );
		hp.setCellWidth( clear, "33%" );
		
		hp.setCellHorizontalAlignment( title, HasHorizontalAlignment.ALIGN_CENTER );
		hp.setCellHorizontalAlignment( clear, HasHorizontalAlignment.ALIGN_RIGHT );
		
		return hp;
	}
	
	
	private void updatePlaylistSize() 
	{
		playlistSizeLabel.setText( "(" + listenManager.getPlaylistSize() + ")" );
	}
	
	
	public void addToPlaylistPanel( AudioPlaylistItem playlistItem )
	{
		Panel displayPanel = createPlaylistItemPanel( playlistItem );
		playlistItem.setDisplayPanel( displayPanel );
		playlistPanel.add( displayPanel );
		
		updatePlaylistSize();
	}
	
	
	private Panel createPlaylistItemPanel( AudioPlaylistItem playlistItem )
	{
		ReleaseDetail rd = playlistItem.getReleaseDetail();
		int partnerID = playlistItem.getPartnerID();
		
		StringBuffer sb = new StringBuffer();
		sb.append( rd.getArtist().getArtistName() + " - " );
		sb.append( rd.getTrackName() );
		
		String mix = rd.getMixName();
		if ( ( mix != null ) && ( ! mix.trim().equals( "" ) ) )
			sb.append( " (" + mix + ") " );
		
		sb.append( " - " + rd.getLabel().getLabelName() );
		
		Label track = new Label( sb.toString() );
		if ( sb.length() >= 75 ) 
			track = new Label( sb.substring( 0, 71 ) + "..." ); // was 100 & 96
		track.setTitle( sb.toString() );
		track.setStylePrimaryName( "smallerText" );
				
		// create track info display
		HyperlinkIconCoverArt coverArt = new HyperlinkIconCoverArt( viewManager, rd );		
		HyperlinkIconWishlist wishlist = new HyperlinkIconWishlist( viewManager, rd );
		HyperlinkIconBuy partnerIcon = new HyperlinkIconBuy( viewManager, rd, partnerID );
		
		HyperlinkIconDelete remove = new HyperlinkIconDelete( 
													"remove from playlist",
													new RemoveListener( playlistItem ) );
		
		FocusPanel fp = new FocusPanel( track );
		fp.setStylePrimaryName( "playlistFocusPanel" );
		fp.addClickHandler( new PlaylistClickListener( playlistItem ) );
				
		HorizontalPanel actionPanel = new HorizontalPanel();
		actionPanel.setSpacing( 0 );
		actionPanel.setWidth( "78px" );
		actionPanel.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
		actionPanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		actionPanel.add( partnerIcon );
		actionPanel.add( wishlist );
		actionPanel.add( remove );
		actionPanel.setCellWidth( partnerIcon, "32px" );
		actionPanel.setCellWidth( wishlist, "22px" );
		actionPanel.setCellWidth( remove, "24px" );
		
		HorizontalPanel ratePanel = new HorizontalPanel();
		ratePanel.setSpacing( 0 );
		ratePanel.setWidth( "78px" );
		ratePanel.setVerticalAlignment( HasVerticalAlignment.ALIGN_BOTTOM );
		ratePanel.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		ratePanel.add( new ReleaseRatingPanelUserOnly( viewManager, rd ) );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing( 0 );
		vp.setHeight( "45px" );
		vp.add( actionPanel );
		vp.add( ratePanel );
				
		FlexTable wrapper = new FlexTable();
		wrapper.setWidth( "100%" );
		wrapper.setStylePrimaryName( "playlistRow" );
		
		wrapper.setWidget( 0, 0, coverArt );
		wrapper.getFlexCellFormatter().setVerticalAlignment( 0, 0, 
					HasVerticalAlignment.ALIGN_MIDDLE );
		
		wrapper.setWidget( 0, 1, fp );
		wrapper.getFlexCellFormatter().setHorizontalAlignment( 0, 1, 
					HasHorizontalAlignment.ALIGN_LEFT );
		
		wrapper.setWidget( 0, 2, vp );
		wrapper.getFlexCellFormatter().setVerticalAlignment( 0, 2, 
					HasVerticalAlignment.ALIGN_MIDDLE );
		
		return wrapper;
	}
	
	
	
	public void scrollPlaylist()
	{
		if ( lastPlaylistPanel != null )
		{
			int itemTop = lastPlaylistPanel.getAbsoluteTop();
			int itemOff = lastPlaylistPanel.getOffsetHeight();
			int scrollTop = playlistScrollPanel.getAbsoluteTop();
			int scrollOff = playlistScrollPanel.getOffsetHeight();
			
			if ( ( itemTop + itemOff > scrollTop + scrollOff )
				|| ( itemTop < scrollTop ) )
			{
				int scrollPos = itemTop - itemOff - playlistPanel.getAbsoluteTop();
				playlistScrollPanel.setScrollPosition( scrollPos );
			}
		}
	}
	
	
	
	
	public void setPlaying( boolean isPlaying ) { }
	
	
	public void setQueued( boolean isQueued ) { }
	
	
	public void updateTrackInfo( AudioPlaylistItem playlistItem )
	{
		// remove highlist from last playing item
		if ( lastPlaylistPanel != null ) 
			lastPlaylistPanel.removeStyleName( "playlistRowHighlighted" );
		
		lastPlaylistPanel = playlistItem.getDisplayPanel();
			
		// highlight currently playing item in playlist
		if ( lastPlaylistPanel != null )
			// checking for null again to avoid some occasional javascript errors
			lastPlaylistPanel.addStyleName( "playlistRowHighlighted" );
		
		// scroll playlist if necessary
		scrollPlaylist();
	}
	
	
	
	
	private class PlaylistClickListener implements ClickHandler
	{
		private AudioPlaylistItem playlistItem = null;
		
		public PlaylistClickListener( AudioPlaylistItem playlistItem )
		{
			this.playlistItem = playlistItem;
		}
		
		public void onClick( ClickEvent sender )
		{
			sender.getRelativeElement().blur();
			//((FocusPanel) sender).setFocus( false );
			
			listenManager.updateTrackInfo( playlistItem );
		}
	}
	
	
	private class ClearPlaylistListener implements ClickHandler
	{
		public void onClick( ClickEvent sender )
		{
			playlistPanel.clear();
			
			listenManager.clearPlaylist();
			
			updatePlaylistSize();
		}
	}
	
	
	private class SavePlaylistListener implements ClickHandler
	{
		public void onClick( ClickEvent sender )
		{
			listenManager.savePlaylist();
		}
	}
	
	
	private class RemoveListener implements ClickHandler
	{
		private AudioPlaylistItem playlistItem = null;
		
		public RemoveListener( AudioPlaylistItem playlistItem )
		{
			this.playlistItem = playlistItem;
		}
		
		public void onClick( ClickEvent sender )
		{
			playlistItem.getDisplayPanel().removeFromParent();

			listenManager.removePlaylistItem( playlistItem );
			
			updatePlaylistSize();
		}
	}

	
}
