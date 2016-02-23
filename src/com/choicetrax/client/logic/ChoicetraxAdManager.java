package com.choicetrax.client.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.data.User;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.data.config.AdImage;
import com.choicetrax.client.data.config.AdImageQueue;
import com.choicetrax.client.data.config.AdImageQueueHash;
import com.choicetrax.client.data.config.AdImages;
import com.choicetrax.client.actions.handleractions.AdClickAction;
import com.choicetrax.client.actions.handleractions.AdImpressionAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.input.HyperlinkImage;
import com.choicetrax.client.logic.callbacks.DoNothingController;


public class ChoicetraxAdManager 
{
		
	private ChoicetraxViewManager viewManager = null;
	private List<Integer> lastGenreIDs = null;
	
	private AdImage topPaidBannerAd = null;
	private AdImageQueue topPaidTargetedAds = null;
	private AdImageQueue bannerAds = null;
	private AdImageQueueHash paidTargetedAdsHash = null;
	private AdImageQueueHash freeTargetedAdsHash = null;
	
	private boolean initialized = false;
	private boolean topAdsShown = false;
	
	private Timer refreshTimer = new AdRefreshTimer();
	private long lastRefresh = -1;
	
	
	
	public ChoicetraxAdManager( ChoicetraxViewManager manager ) {
		this.viewManager = manager;
	}
	
	
	public void initialize() 
	{
		AdImages imgLoad = viewManager.getAdImages();
		
		topPaidBannerAd = imgLoad.getTopPaidBannerAd();
		topPaidTargetedAds = imgLoad.getTopPaidTargetedAds();
		
		bannerAds = imgLoad.getBannerAds();
		paidTargetedAdsHash = imgLoad.getPaidTargetedAdsHashtable();
		freeTargetedAdsHash = imgLoad.getFreeTargetedAdsHashtable();
		
		refreshTimer.scheduleRepeating( 60000 );
		
		initialized = true;
	}

	
	private void clearAdPanels()
	{
		HorizontalPanel bannerPanel = viewManager.getView().getAdBannerPanel();
		bannerPanel.clear();
		
		VerticalPanel adLeftPanel = viewManager.getView().getAdLeftPanel();
		adLeftPanel.clear();
	}
	
	
	/**
	 * This is one of two points of entry into the ChoicetraxAdManager.
	 * It displays the top paid banner ad and the top 4 paid left targeted ads.
	 * 
	 * @return
	 */
	public void refreshTopAds()
	{
		if ( ! initialized ) return;
		
		DeferredCommand.addCommand( new Command() {
			public void execute() 
			{
				refreshAds( topPaidBannerAd,
						topPaidTargetedAds.getNextAd(),
						topPaidTargetedAds.getNextAd(),
						topPaidTargetedAds.getNextAd(),
						topPaidTargetedAds.getNextAd() );
				
				topAdsShown = true;
			}
		});
	}
	
	
	public void refreshNextAds() 
	{
		if ( ! initialized ) return;
		
		DeferredCommand.addCommand( new Command() {
			public void execute() 
			{
				if ( ! topAdsShown ) {
					// make sure top ads are always shown first
					refreshTopAds();
				}
				else if ( lastGenreIDs != null ) {
					refreshTargetedAds( lastGenreIDs );
				}
				else 
					refreshTargetedAds( new ArrayList<Integer>() );
			}
		});
	}

	
	
	public void refreshTargetedAds( final Cache cache )
	{
		if ( ! initialized ) return;
		
		DeferredCommand.addCommand( new Command() {
			public void execute() 
			{
				if ( ! topAdsShown ) {
					// make sure top ads are always shown first
					refreshTopAds();
				}
				else if ( cache != null ) {
					List<Integer> genreIDs = cache.getGenreIDsForCurrentPage();
					lastGenreIDs = genreIDs;
					
					refreshTargetedAds( genreIDs );
				}
				else 
					refreshNextAds();
			}
		});
	}
	
	/**
	 * This is one of two points of entry into the ChoicetraxAdManager.
	 * It displays targeted ads tailored to the genres associated to the 
	 * input Cache object.
	 * 
	 * @param type
	 * @param cache
	 */
	private void refreshTargetedAds( List<Integer> genreIDs )
	{
		AdImage bannerAd = bannerAds.getNextAd();
				
		ArrayList<Integer> adIDsToExclude = new ArrayList<Integer>();
		
		AdImage slot1Ad = getTargetedAd( genreIDs, 1, adIDsToExclude );
		if ( slot1Ad != null ) 
			adIDsToExclude.add( slot1Ad.getAdID() );
		
		AdImage slot2Ad = getTargetedAd( genreIDs, 2, adIDsToExclude );
		if ( slot2Ad != null ) 
			adIDsToExclude.add( slot2Ad.getAdID() );
		
		AdImage slot3Ad = getTargetedAd( genreIDs, 3, adIDsToExclude );
		if ( slot3Ad != null ) 
			adIDsToExclude.add( slot3Ad.getAdID() );
		
		AdImage slot4Ad = getTargetedAd( genreIDs, 4, adIDsToExclude );
		
		refreshAds( bannerAd, slot1Ad, slot2Ad, slot3Ad, slot4Ad );	
	}
	
	
	
	private AdImage getTargetedAd( List<Integer> genreIDs, int slotNum, List<Integer> idsToExclude )
	{
		AdImage adImage = null;
		List<Integer> genreIDsToExclude = new LinkedList<Integer>();
		
		for ( int x=0; adImage == null; x++ )
		{		
			int genreID = determineGenreID( genreIDs, slotNum );
			
			AdImageQueue genreAdQueue = determineFreeOrPaid( genreID, slotNum, idsToExclude );
							
			if ( genreAdQueue != null )
				adImage = getImageFromQueue( slotNum, genreAdQueue, idsToExclude );
			
			if ( adImage == null )
			{
				// first look for ad in next genreID in genreIDs list
				genreIDs.remove( new Integer( genreID ) );
				genreIDsToExclude.add( genreID );

				// if no genreIDs in that list, get random genre 
				if ( genreIDs.size() == 0 ) {
					int randomGenreID = getRandomGenreID( genreIDsToExclude );
					
					if ( randomGenreID == 0 ) {
						// if randomGenreID == 0, it's not finding any genres.
						// quit trying to exclude ads
						idsToExclude.clear();
						genreIDsToExclude.clear();
					}
					genreIDs.add( randomGenreID );
				}
			}
			
			// avoid infinite loop, just in case
			if ( x == 10 ) 
				break;
		}
		
		return adImage;
	}
	
	
	private AdImageQueue determineFreeOrPaid( int genreID, 
												int slotNum, 
												List<Integer> adIdsToExclude )
	{
		AdImageQueue paidQueue = paidTargetedAdsHash.lookup( genreID );
		AdImageQueue freeQueue = freeTargetedAdsHash.lookup( genreID );
		
		boolean paidNotExcluded = queueContainsNonExcludedID( paidQueue, adIdsToExclude );
		boolean freeNotExcluded = queueContainsNonExcludedID( freeQueue, adIdsToExclude );
		
		if ( ( slotNum == 1 || slotNum == 2 ) && paidNotExcluded )
			return paidQueue;
		else if ( ( paidQueue != null ) && ( ! paidQueue.allAdsShown() ) )
			return paidQueue;
		else if ( freeNotExcluded )
			return freeQueue;
		else		
			return null;
	}
	
	
	private boolean queueContainsNonExcludedID( AdImageQueue queue, List<Integer> adIdsToExclude )
	{
		if ( ( queue != null ) && ( queue.size() > 0 ) ) 
		{
			int size = queue.size();
			for ( int x=0; x < size; x++ ) {
				AdImage adImage = queue.peekAd( x );
				if ( ! adIdsToExclude.contains( adImage.getAdID() ) )
					return  true;
			}
		}
		return false;
	}
	
	
	private AdImage getImageFromQueue( int slotNum, 
										AdImageQueue queue, 
										List<Integer> idsToExclude )
	{
		AdImage adImage = null;
		
		int size = queue.size();
		for ( int x=0; x < size; x++ ) 
		{
			AdImage img = queue.peekAd( x );
			
			if ( ! idsToExclude.contains( img.getAdID() ) ) 
			{
				adImage = img;
				queue.moveAdToEndOfQueue( adImage );
				break;
			}
		}
				
		return adImage;
	}
	
	
	private int determineGenreID( List<Integer> genreIDs, int slotNum )
	{
		int genreID = 0;
		int size = genreIDs.size();
		
		if ( genreIDs != null && size > 0 ) 
		{
			if ( slotNum == 1 || slotNum == 2 || size <= 1 )
				genreID = genreIDs.get( 0 );
			else if ( slotNum == 3 || size <= 2 )
				genreID = genreIDs.get( 1 );
			else
				genreID = genreIDs.get( 2 );
		} else {
			genreID = getRandomGenreID( genreIDs );
		}
		
		return genreID;
	}
	
	
	/**
	 * Get a random genre ID.  First try to get a genre ID out of the paid hash.
	 * If that fails, try to get one out of the free hash.
	 * If that fails I'm not sure what happens...
	 * 
	 * @return
	 */
	private int getRandomGenreID( List<Integer> genreIDsToExclude )
	{
		int genreID = getRandomGenreIDForHash( paidTargetedAdsHash, genreIDsToExclude );
		
		if ( genreID == 0 )
			genreID = getRandomGenreIDForHash( freeTargetedAdsHash, genreIDsToExclude );		
		
		return genreID;
	}
	
	
	private int getRandomGenreIDForHash( AdImageQueueHash targetedHash, 
										 List<Integer> genreIDsToExclude )
	{
		int genreID = 0;
		
		// first remove excluded genreIDs from targetedHash keys
		List<Integer> keyList = targetedHash.keys();
		Iterator<Integer> i = genreIDsToExclude.iterator();
		while ( i.hasNext() )
			keyList.remove( i.next() );
		
		if ( keyList.size() != 0 )
		{
			// randomly select genreID from remaining, non-excluded genreIDs
			int random = Random.nextInt( keyList.size() - 1 );
			
			Iterator<Integer> keys = keyList.iterator();
			for ( int x=0; keys.hasNext(); x++ ) {
				int key = keys.next();
				if ( x == random ) {
					genreID = key;
					break;
				}
			}
		}

		return genreID;
	}
	
	
	
	
	private void refreshAds( AdImage bannerAd, 
							 AdImage slot1Ad, 
							 AdImage slot2Ad, 
							 AdImage slot3Ad, 
							 AdImage slot4Ad )
	{
		clearAdPanels();
		
		AdImpressionAction action = new AdImpressionAction();
		action.setActionType( Constants.ACTION_ADD );
		
		if ( bannerAd != null ) {
			displayBannerAd( bannerAd );
			action.addAdID( bannerAd.getAdID() );
		}
		
		if ( slot1Ad != null ) {
			displayTargetedAd( 1, slot1Ad );
			action.addAdID( slot1Ad.getAdID() );
		}
		
		if ( slot2Ad != null ) {
			displayTargetedAd( 2, slot2Ad );
			action.addAdID( slot2Ad.getAdID() );
		}
		
		if ( slot3Ad != null ) {
			displayTargetedAd( 3, slot3Ad );
			action.addAdID( slot3Ad.getAdID() );
		}
		
		if ( slot4Ad != null ) {
			displayTargetedAd( 4, slot4Ad );
			action.addAdID( slot4Ad.getAdID() );
		}
		
		User currentUser = viewManager.getCurrentUser();
		if ( currentUser != null ) action.setUserID( currentUser.getUserID() );
		
		// only count impressions when in web mode (ie. not hosted development mode)
		if ( GWT.isScript() )
			viewManager.deferAction( action, new DoNothingController() );
			
		lastRefresh = System.currentTimeMillis();
	}
		
	
	/**
	 * This method actuallys displays the banner ad passed in as a parameter.
	 * 
	 * @param bannerAd
	 * @return
	 */
	private void displayBannerAd( AdImage bannerAd )
	{
		HorizontalPanel bannerPanel = viewManager.getView().getAdBannerPanel();
		
		if ( ( bannerAds != null ) && ( bannerAds.size() > 0 ) )
		{
			HyperlinkImage img = new HyperlinkImage( bannerAd.getImageURL(), 
					 									new AdClickListener( bannerAd ) );
			img.setSize( Constants.AD_SIZE_TOP_BANNER_WIDTH,
						 Constants.AD_SIZE_TOP_BANNER_HEIGHT );
			img.setTitle( bannerAd.getLinkURL() );
						
			bannerPanel.add( img );
		}		
	}
	
		
	
	private void displayTargetedAd( int slotNum, AdImage adImage )
	{
		VerticalPanel adLeftPanel = viewManager.getView().getAdLeftPanel();
		
		HyperlinkImage img = new HyperlinkImage( adImage.getImageURL(), 
												 new AdClickListener( adImage ) );
		img.setSize( Constants.AD_SIZE_LEFT_SMALL_WIDTH,
					 Constants.AD_SIZE_LEFT_SMALL_HEIGHT );
		img.setTitle( adImage.getLinkURL() );
		img.setStylePrimaryName( "bannerLeft" + slotNum );
		
		adLeftPanel.add( img );
	}
	
	
	
	
	private class AdClickListener implements ClickHandler
	{
		private AdImage adImage = null;
		
		public AdClickListener( AdImage adImage ) {
			this.adImage = adImage;
		}
		
		public void onClick( ClickEvent event ) 
		{
			String actionString = adImage.getLinkURL();
			
			if ( actionString == null ) 
				Window.alert( "AdID [" + adImage.getAdID() + "] has no action defined" );
			else
			{
				if ( actionString.startsWith( "http" ) )
					Window.open( actionString, "_choicetrax_" + adImage.getAdID(), "" );
				else
					viewManager.updateHistoryDisplay( actionString );
			}
						
			
			// notify server side of ad click 
			
			int userID = 0;
			if ( viewManager.getCurrentUser() != null )
				userID = viewManager.getCurrentUser().getUserID();
			
			AdClickAction action = new AdClickAction();
			action.setActionType( Constants.ACTION_ADD );
			action.setAdID( adImage.getAdID() );
			action.setUserID( userID );
			
			viewManager.deferAction( action, //new ActionHandlerController( viewManager ) );
									 new DoNothingController() );
		}
	}
	
	
	/**
	 * This class will automatically refresh the ads if it's been 1 minute
	 * since they were last refreshed.
	 * 
	 * @author David
	 *
	 */
	private class AdRefreshTimer extends Timer
	{
		public void run() 
		{
			long currentTime = System.currentTimeMillis();
			
			if ( currentTime - lastRefresh > 55000 )
				refreshNextAds();
		}
	}
	
	
	// old code from attempting to integrate with ad networks
	
	// only do our ads in spots 1-3, try adding OpenX ads to spot #4
	/*HTML testAd = new HTML( "<a href='http://d1.openx.org/ck.php?n=a9de444e"
							+ "&amp;cb=INSERT_RANDOM_NUMBER_HERE' target='_top'>"
							+ "<img src='http://d1.openx.org/avw.php?zoneid=94238"
							+	"&amp;cb=INSERT_RANDOM_NUMBER_HERE&amp;n=a9de444e' "
							+ 	"border='0' alt='' /></a>" );
	/*HTML testAd = new HTML( "<iframe id='a2234336' name='a2234336' src='http://d1.openx.org/afr.php?zoneid=94238&amp;cb=INSERT_RANDOM_NUMBER_HERE' framespacing='0' frameborder='no' scrolling='no' width='150' height='150'><a href='http://d1.openx.org/ck.php?n=a1c6ddc4&amp;cb=INSERT_RANDOM_NUMBER_HERE' target='_blank'><img src='http://d1.openx.org/avw.php?zoneid=94238&amp;cb=INSERT_RANDOM_NUMBER_HERE&amp;n=a1c6ddc4' border='0' alt='' /></a></iframe>" );*/
	
	// trying out google adsense
	/*HTML testAd = new HTML( "<script type=\"text/javascript\">"
							+ "<!-- google_ad_client = \"pub-9496096525942515\"; "
							+ "/* 125x125, created 12/23/09 * "
							+ "google_ad_slot = \"2750891259\"; "
							+ "google_ad_width = 125; "
							+ "google_ad_height = 125; "
							+ "//--> "
							+ "</script> "
							+ "<script type=\"text/javascript\" "
							+ "src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\"> "
							+ "</script>" );*
	
	testAd.setSize( Constants.AD_SIZE_LEFT_SMALL_WIDTH,
					 Constants.AD_SIZE_LEFT_SMALL_HEIGHT );
	testAd.setStylePrimaryName( "bannerLeft4" );
	adLeftPanel.add( testAd );*/

}
