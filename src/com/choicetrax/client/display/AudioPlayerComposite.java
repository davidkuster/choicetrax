package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.ChoicetraxListenManager;
import com.choicetrax.client.data.*;
import com.choicetrax.client.display.panels.ReleaseRatingPanelUserOnly;
import com.choicetrax.client.display.panels.ReleaseTrackNamePanel;
import com.choicetrax.client.input.*;
import com.choicetrax.client.input.listeners.*;


/**
 * This is audio player design 2 from the whiteboard.
 * It has a large area on the left that displays the
 * various track details.
 * 
 * @author David
 */
public class AudioPlayerComposite 
	extends Composite
	implements AudioPlayer
{
	
	private ChoicetraxViewManager viewManager		= null;
	private ChoicetraxListenManager listenManager	= null;
	
	private ClickableImageHTML transportPlayPause	= null;
	
	private SimpleCheckBox queueCheckBox = SimpleCheckBox.wrap( 
											RootPanel.get( "queue" ).getElement() );
	
	private VerticalPanel trackInfoPanel = null;
	

	
	
	public AudioPlayerComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		this.listenManager = viewManager.getListenManager();
		
		transportPlayPause = new ClickableImageHTML( RootPanel.get( "audioPlay" ).getElement(), 
								new AudioTransportPlayPauseListener( listenManager ) );
		
		Label queue = new Label( "queue" );
		queue.setTitle( "controls playlist queuing" );
		RootPanel.get( "queueText" ).add( queue );
		
		queueCheckBox.setTitle( "controls playlist queueing" );
		
		// TODO: add waveform control
		/*Label waveform = new Label( "waveform" );
		waveform.setTitle( "controls waveform display" );
		RootPanel.get( "waveformText" ).add( waveform );*/
		
		queueCheckBox.addClickHandler( new AudioQueueClickListener( listenManager ) );
		
		HorizontalPanel hp = new HorizontalPanel();
		initWidget( hp );
		
		hp.add( createTrackInfoPanel() );
		
		// add listeners to existing transport images
		new ClickableImageHTML( RootPanel.get( "audioPrev" ).getElement(),
								new AudioTransportPrevListener( listenManager ) );
		new ClickableImageHTML( RootPanel.get( "audioStop" ).getElement(),
								new AudioTransportStopListener( listenManager ) );
		new ClickableImageHTML( RootPanel.get( "audioNext" ).getElement(),
								new AudioTransportNextListener( listenManager ) );
	}
		
	
	
	private Panel createTrackInfoPanel()
	{
		trackInfoPanel = new VerticalPanel();
		trackInfoPanel.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
		trackInfoPanel.setStylePrimaryName( "smallTrackInfoPanel" );
				
		HorizontalPanel hp = new HorizontalPanel();
		hp.add( new Label( "Click listen icons (" ) );
		hp.add( new Image( "img/listenicons/listen_test_traxsource_icon.gif" ) );
		hp.add( new Label( ") to play audio clips." ) );
		
		trackInfoPanel.add( hp );
		
		return trackInfoPanel;
	}
	
	
	// no-op method - here to satisfy AudioPlayer interface
	public void addToPlaylistPanel( AudioPlaylistItem playlistItem ) {}
	
	
	
	public void setPlaying( boolean isPlaying )
	{
		if ( isPlaying ) 	transportPlayPause.setUrl( "img/layout/transport/btnPause.gif" );
		else				transportPlayPause.setUrl( "img/layout/transport/btnPlay.gif" );
	}
	
	
	public void setQueued( boolean isQueued )
	{
		queueCheckBox.setChecked( isQueued );
	}
	
	
	public void updateTrackInfo( AudioPlaylistItem playlistItem )
	{
		ReleaseDetail rd 	= playlistItem.getReleaseDetail();
		int partnerID 		= playlistItem.getPartnerID();
		
		// create track info display
		HyperlinkIconCoverArt coverArt 	= new HyperlinkIconCoverArt( viewManager, rd );			
		HyperlinkIconWishlist wishlist 	= new HyperlinkIconWishlist( viewManager, rd );
		//HyperlinkIconOptions options	= new HyperlinkIconOptions( viewManager, rd );
		HyperlinkIconBuy partnerIcon 	= new HyperlinkIconBuy( viewManager, rd, partnerID );
		
		ReleaseTrackNamePanel trackInfo = createTrackInfo( rd );
		
		/*ScrollPanel sp = new ScrollPanel();
		//sp.setSize( "100%", "20px" );
		sp.setStylePrimaryName( "smallTrackInfoPanel" );
		sp.setAlwaysShowScrollBars( false );
		sp.add( trackInfo );
		sp.setTitle( rd.getTitle() );*/
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
		hp.setSpacing( 2 );
		hp.add( new HTML( "&nbsp;" ) );
		hp.add( coverArt );
		hp.add( new HTML( "&nbsp;" ) );
		hp.add( trackInfo );
		hp.add( new HTML( "&nbsp;" ) );
		hp.add( partnerIcon );
		hp.add( wishlist );
		//hp.add( options );
		hp.add( new ReleaseRatingPanelUserOnly( viewManager, rd ) );
		hp.add( new HTML( "&nbsp;" ) );
				
		trackInfoPanel.clear();
		trackInfoPanel.add( hp );
	}
	
	
	private ReleaseTrackNamePanel createTrackInfo( ReleaseDetail rd )
	{		
		ReleaseTrackNamePanel trackNamePanel = new ReleaseTrackNamePanel( rd, viewManager );
		trackNamePanel.add( new Label( " - " ) );
		trackNamePanel.add( new TrackHyperlink( rd.getLabel(), viewManager ) );
		
		/*Label bpmLabel = null;
		
		if ( rd.getBpm() > 0 )
		{
			int bpm = (int) ( rd.getBpm() + 0.5 );
			int conf = (int) ( ( rd.getBpmConfidence() * 100 ) + 0.5 );
			
			String s = " [" + bpm + " BPM";
			if ( bpm > 170 || bpm < 70 ) s += "(?)";
			s += "]";
			
			bpmLabel = new Label( s );
			bpmLabel.setTitle( "BPM estimated at " + rd.getBpm() + ", "
								+ "with " + conf + "% confidence" );
		}
		else if ( rd.getBpm() == -1 )
		{
			// BPM cannot be determined
			bpmLabel = new Label( " [BPM=?]" );
			bpmLabel.setTitle( "BPM cannot be determined" );
		}
		else {
			bpmLabel = new Label( " [BPM TBD]" );
			bpmLabel.setTitle( "Determining BPM..." );
		}
		
		bpmLabel.setStylePrimaryName( "italicText" );
		trackNamePanel.add( bpmLabel );*/
		
		trackNamePanel.addStyleName( "smallerText" );
		
		return trackNamePanel;
	}
	

}
