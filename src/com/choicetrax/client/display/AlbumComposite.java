package com.choicetrax.client.display;

import java.util.*;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.RecordLabel;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.Releases;
import com.choicetrax.client.display.panels.ListenBuyPanelEP;
import com.choicetrax.client.input.HyperlinkImage;
import com.choicetrax.client.input.TrackHyperlink;
import com.choicetrax.client.input.listeners.EPImageLoadListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class AlbumComposite 
	extends Composite 
	implements ChoicetraxViewComponent
{
	private ChoicetraxViewManager viewManager = null;
	
	private ScrollPanel epScrollPanel 	= null;
	
	private static DateTimeFormat dateFormatter	= DateTimeFormat.getFormat( "MMM dd, yyyy" );
	
	
	public AlbumComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		Panel p = createEPDisplay();
		initWidget( p );
	}
	
	
	private Panel createEPDisplay()
	{
		epScrollPanel = new ScrollPanel();
		
		HTML text = new HTML( 
					"<center><b>Album/EP Display</b></center> "
					+ "After you do a search, click on the cover "
					+ "art or \"EP\" link to the left of the track "
					+ "details to load the album or EP "
					+ "info in this window." );
		
		epScrollPanel.add( text );
		epScrollPanel.setStylePrimaryName( "albumScrollPanel" );
		epScrollPanel.addStyleName( "albumInfoPanel" );
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( epScrollPanel );
		
		return vp;
	}
	
	
	public void updateDisplay( LoaderResponse response )
	{
		Releases releases = (Releases) response;
		ReleaseDetail firstRelease = (ReleaseDetail) releases.getReleasesList().get( 0 );
		
		epScrollPanel.clear();
		
		VerticalPanel vp = new VerticalPanel();
		
		String albumName 	= firstRelease.getAlbumName();
		RecordLabel label 	= firstRelease.getLabel();
		String labelCatNum 	= firstRelease.getLabelCatalogNum();
		String releaseDate 	= "TBD";
		
		if ( firstRelease.getReleaseDate() != null )
			releaseDate = dateFormatter.format( firstRelease.getReleaseDate() );
		
		if ( ( albumName == null ) || ( albumName.equals( "null" ) ) )
			albumName = firstRelease.getTrackName();
		
		if ( ( labelCatNum == null ) || ( labelCatNum.equals( "null" ) ) )
			labelCatNum = "N/A";
		
		VerticalPanel titlePanel = new VerticalPanel();
		titlePanel.add( new HTML( "<b>Name:</b> " + albumName ) );
		titlePanel.add( new HTML( "<b>Date:</b> " + releaseDate ) );
		HorizontalPanel labelPanel = new HorizontalPanel();
		labelPanel.add( new HTML( "<b>Label:</b>&nbsp;" ) );
		labelPanel.add( new TrackHyperlink( label, viewManager ) );
		titlePanel.add( labelPanel );
		titlePanel.add( new HTML( "<b>Cat No:</b> " + labelCatNum ) );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add( titlePanel );
		hp.add( createCoverArtPanel( firstRelease ) );
		hp.setWidth( "100%" );

		vp.add( hp );
		vp.add( new HTML( "<p><br><b>Bundle Pricing:</b> We are not yet set up to handle release/EP bundle pricing.<p>" ) );
		
		for ( Iterator<ReleaseDetail> i = releases.getReleasesList().iterator(); i.hasNext(); )
		{
			ReleaseDetail rd = i.next();
			
			ListenBuyPanelEP listenBuy = new ListenBuyPanelEP( viewManager, rd );
			
			HorizontalPanel hp2 = new HorizontalPanel();
			hp2.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
			
			hp2.add( listenBuy.getListenBuyPanel() );
			hp2.add( listenBuy.getActionPanel() );
			
			vp.add( hp2 );
		}
		
		epScrollPanel.add( vp );
		epScrollPanel.setScrollPosition( 0 );
	}
	
	
	public void clearDisplay() { }
	
	public void setWaitingState( boolean waiting ) { }
	
	
	/*
	 * should i put the process of finding the first cover art URL
	 * in a method on ReleaseDetail ???
	 */
	private Widget createCoverArtPanel( ReleaseDetail rd )
	{
		String imageURL = rd.getCoverArtURL();
		HyperlinkImage coverArt = new HyperlinkImage( 
										imageURL, 
										"Click to view cover art", 
										new ViewCoverArtListener( imageURL ) );
		coverArt.setSize( "50px", "50px" );
		
		EPImageLoadListener handler = new EPImageLoadListener();
		coverArt.addLoadHandler( handler );
		coverArt.addErrorHandler( handler );
		
		//HyperlinkIconCoverArt coverArt = new HyperlinkIconCoverArt( viewManager, rd, 50 );
		
		//Label clickToView = new Label( "(click to view)" );
		//clickToView.setStylePrimaryName( "smallerItalicText" );
		
		VerticalPanel artvp = new VerticalPanel();
		artvp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		artvp.add( coverArt );
		//artvp.add( clickToView );
		
		return artvp;
	}
	
	
	
	private class ViewCoverArtListener implements ClickHandler
	{
		private String imageURL = null;
		
		public ViewCoverArtListener( String url ) {
			this.imageURL = url;
		}
		
		public void onClick( ClickEvent event ) 
		{
			final Widget w = (Widget) event.getSource();
			
			final PopupPanel imgPanel = new PopupPanel( true );
			imgPanel.setStyleName( "popupPanel" );
			imgPanel.setAnimationEnabled( true );
			
			imgPanel.add( new Image( imageURL ) );
			
			imgPanel.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
				public void setPosition(int offsetWidth, int offsetHeight) 
				{
					int left = w.getAbsoluteLeft() + w.getOffsetWidth() - offsetWidth;
					int top = w.getAbsoluteTop();
					imgPanel.setPopupPosition(left, top);
				}
			});
		}
	}

}
