package com.choicetrax.client.display;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.*;

import com.choicetrax.client.Choicetrax;
import com.choicetrax.client.display.panels.ChoicetraxCenterPanel;
import com.choicetrax.client.display.panels.decorators.BoxTabPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.input.ClickableImage;
import com.choicetrax.client.input.ClickableImageHTML;
import com.choicetrax.client.input.ClickableImageOnOff;
import com.choicetrax.client.constants.Constants;


public class ChoicetraxView 
	//extends Composite
	//implements WindowResizeListener
{
	
	private ChoicetraxViewManager viewManager = null;
	
	// UI components
	private VerticalPanel rightPanel 		= null;
	private BoxTabPanel audioAlbumTabPanel 	= null;
	private HorizontalPanel adBannerPanel 	= null;
	private VerticalPanel adLeftPanel 		= null;
	
	private ClickableImage hideButton	= null;
	private ClickableImage resizeButton = null;
	
	private boolean isFullScreen 	= true;
	private boolean isRightVisible	= false;
		
	// composite display classes
	private SearchComposite searchComposite						= null;
	private FavoritesComposite favoritesComposite				= null;
	private AccountComposite accountComposite					= null;
	private AlbumComposite albumComposite						= null;
	private ChoicetraxCenterPanel centerPanel					= null;
	private AboutUsComposite aboutUsComposite					= null;
	private RecommendationsComposite recommendationsComposite 	= null;
	private FeaturedReleasesComposite featuredComposite			= null;
	private AudioPlayerPlaylistComposite playlistComposite		= null;
	private AudioPlayerComposite audioPlayerComposite 			= null;
	
	
	
	public ChoicetraxView( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		searchComposite				= new SearchComposite( manager );
		favoritesComposite 			= new FavoritesComposite( manager );
		accountComposite 			= new AccountComposite( manager );
		albumComposite 				= new AlbumComposite( manager );
		centerPanel					= new ChoicetraxCenterPanel( manager );
		aboutUsComposite 			= new AboutUsComposite( manager );
		recommendationsComposite	= new RecommendationsComposite( manager );
		featuredComposite			= new FeaturedReleasesComposite( manager );
		playlistComposite			= new AudioPlayerPlaylistComposite( manager );
		audioPlayerComposite		= new AudioPlayerComposite( manager );
								
		createImages();
		createAdPanels();
		createRightPanel();
					
		//Window.addWindowResizeListener( this );
		
		if ( isRightVisible )
			hideButton.click();
	}
	
	
	private void createImages()
	{
		// wrap existing logo image 
		new ClickableImageHTML( DOM.getElementById( "logoHeader" ),
				new NewsListener( viewManager ) );
		
		// create full width / fixed width button
		resizeButton = new ClickableImage( "img/layout/btnFixedWidth.gif" );
		resizeButton.setTitle( "Fixed Width" );
		resizeButton.addClickHandler( new ResizeListener() );
		
		// create right panel show / hide button
		hideButton = new ClickableImageHTML( DOM.getElementById( "btnRightPanel" ),
						new HideListener() );
		hideButton.setTitle( "Show Right Panel" );
	}
	

		
	private void createAdPanels()
	{
		adBannerPanel = new HorizontalPanel();
		adBannerPanel.add( new LoadingComposite() );
		
		adLeftPanel = new VerticalPanel();
		adLeftPanel.add( new LoadingComposite() );
	}
	
		
	
	private void createRightPanel()
	{
		audioAlbumTabPanel = new BoxTabPanel();
		audioAlbumTabPanel.getDeckPanel().setAnimationEnabled( true );
		audioAlbumTabPanel.add( playlistComposite, 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.audioplayer_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.audioplayer_off() ),
						ClickableImageOnOff.DEFAULT_ON ) );
		audioAlbumTabPanel.add( albumComposite, 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.albums_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.albums_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		audioAlbumTabPanel.selectTab( 0 );
		audioAlbumTabPanel.addTabListener( new PlaylistScrollListener() );
				
		rightPanel = new VerticalPanel();
		
		rightPanel.add( audioAlbumTabPanel );
		rightPanel.add( recommendationsComposite );
		rightPanel.add( favoritesComposite );
		
		recommendationsComposite.addStyleName( "rightPanelComponent" );
		favoritesComposite.addStyleName( "rightPanelComponent" );
		
		rightPanel.setVisible( isRightVisible );
	}
	
		
	
	/*private ScrollPanel createMainPanel()
	{
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.add( centerPanel );
		//scrollPanel.setStyleName( "layoutPanel" );
		//scrollPanel.setHeight( "578px" ); // 610px with right panel open
		//scrollPanel.setWidth( "800px" ); // 585px with right panel open
		
		return scrollPanel;
	}*/
	
	
	public void initialize()
	{
		searchComposite.initialize();
		featuredComposite.initialize();
		aboutUsComposite.initialize();
		getCenterPanel().getAccountConfigComposite().initialize();
		
		// read full screen and queue cookies
		String fsCookie = Choicetrax.getCookie( Constants.COOKIE_FULLSCREEN );
		if ( "false".equals( fsCookie ) ) {
			DeferredCommand.addCommand( new Command() {
				public void execute() {
					resizeButton.click();
					//resizeMainPanel();
				}
			});
		}
		
		boolean queueCookie = false;
		String qCookie = Choicetrax.getCookie( Constants.COOKIE_QUEUE_TRACKS );
		if ( "true".equals( qCookie ) ) queueCookie = true;
		
		audioPlayerComposite.setQueued( queueCookie );
		viewManager.getListenManager().setQueued( queueCookie );
	}
	
	
	public void showRightPanel( int panelCode )
	{
		if ( panelCode == Constants.PANEL_ALBUM_EP_DETAILS )
			audioAlbumTabPanel.selectTab( 1 );
		else if ( panelCode == Constants.PANEL_AUDIO_PLAYER )
			audioAlbumTabPanel.selectTab( 0 );
		
		if ( ! rightPanel.isVisible() )
			hideButton.click();
	}
	
	
	public ClickableImage getResizeButton() {
		return resizeButton;
	}
	
	public ClickableImage getHideButton() {
		return hideButton;
	}
			
	public ChoicetraxCenterPanel getCenterPanel() {
		return centerPanel;
	}
	
	public Panel getRightPanel() {
		return rightPanel;
	}
	
	public Panel getCenterRightPanel() {
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing( 2 );
		hp.add( centerPanel );
		hp.add( rightPanel );
		return hp;
	}
	
	public HorizontalPanel getAdBannerPanel() {
		return adBannerPanel;
	}
	
	public VerticalPanel getAdLeftPanel() {
		return adLeftPanel;
	}
		
	public AlbumComposite getAlbumComposite() {
		return albumComposite;
	}
	
	public AudioPlayerPlaylistComposite getAudioPlaylistComposite() {
		return playlistComposite;
	}
	
	public AudioPlayerComposite getAudioPlayerComposite() {
		return audioPlayerComposite;
	}
	
	public FeaturedReleasesComposite getFeaturedReleasesComposite() {
		return featuredComposite;
	}
	
	public FavoritesComposite getFavoritesComposite() {
		return favoritesComposite;
	}
	
	public SearchComposite getSearchComposite() {
		return searchComposite;
	}
	
	public AccountComposite getAccountComposite() {
		return accountComposite;
	}
	
	public AboutUsComposite getAboutUsComposite() {
		return aboutUsComposite;
	}
	
	public RecommendationsComposite getRecommendationsComposite() {
		return recommendationsComposite;
	}
	
	
	
		
	private void resizeMainPanel()
	{	
		try {
			if ( isFullScreen )
				RootPanel.get( "container" ).getElement().setId( "containerLiquid" );
			else
				RootPanel.get( "containerLiquid" ).getElement().setId( "container" );
		}
		catch ( Exception e ) {
			GWT.log( "resizeMainPanel() exception", e );
		}
	}
	
	
	//public void onWindowResized( int width, int height )
	//{
		//resizeMainPanel();
	//}
	
	
	
	public native void jsCallUrchinTracker( String pageName ) /*-{
		if ( $wnd.urchinTracker )
			$wnd.urchinTracker( pageName );
	}-*/;
	
	
	public native void jsCallPageView( String pageName ) /*-{
		if ( $wnd.pageTracker )
			$wnd.pageTracker._trackPageview( pageName );
	}-*/;
	
	
	public native void jsScrollWindowToTop() /*-{
		$wnd.scrollToTop();
	}-*/;
	
	
	public native String jsDetectBrowser() /*-{
		return navigator.userAgent;
	}-*/;
	

		
	
	private class NewsListener implements ClickHandler
	{
		private ChoicetraxViewManager viewManager = null;
		
		public NewsListener( ChoicetraxViewManager manager )
		{
			this.viewManager = manager;
		}
		
		public void onClick( ClickEvent event ) 
		{
			viewManager.updateHistoryDisplay( Constants.HISTORY_NEWS );
		}
	}
	
	
	private class HideListener implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			if ( isRightVisible )
			{
				isRightVisible = false;
			
				hideButton.setUrl( "img/layout/btnRightPanel.gif" );
				hideButton.setTitle( "Show right panel" );
				
				//RootPanel.get( "rightPanel" ).getElement().setId( "rightPanelHidden" );
				//RootPanel.get( "results900" ).getElement().setId( "results" );
			}
			else
			{
				isRightVisible = true;
				
				hideButton.setUrl( "img/layout/btnHide.gif" );
				hideButton.setTitle( "Hide right panel" );
				
				//RootPanel.get( "rightPanel" ).getElement().setId( "rightPanelVisible" );
				//RootPanel.get( "results" ).getElement().setId( "results900" );
			}
			
			rightPanel.setVisible( isRightVisible );
		}
	}
	
	
	private class ResizeListener implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			if ( isFullScreen )
			{
				resizeButton.setUrl( "img/layout/btnFullPage.gif" );
				resizeButton.setSize( "80px", "20px" );
				resizeButton.setTitle( "Full width" );
				
				isFullScreen = false;
				Choicetrax.setCookie( Constants.COOKIE_FULLSCREEN, 
										"false", 
										Constants.COOKIE_DURATION_1YEAR );
			}
			else
			{
				resizeButton.setUrl( "img/layout/btnFixedWidth.gif" );
				resizeButton.setSize( "94px", "20px" );
				resizeButton.setTitle( "Fixed width" );
				
				isFullScreen = true;
				Choicetrax.setCookie( Constants.COOKIE_FULLSCREEN, 
										"true", 
										Constants.COOKIE_DURATION_1YEAR );
			}
			
			resizeMainPanel();
		}
	}
	
	
	private class PlaylistScrollListener implements TabListener
	{
		public boolean onBeforeTabSelected( SourcesTabEvents sender, int tabIndex ) {
			return true;
		}

		public void onTabSelected( SourcesTabEvents sender, int tabIndex ) 
		{
			playlistComposite.scrollPlaylist();
		}
		
	}
		
}
