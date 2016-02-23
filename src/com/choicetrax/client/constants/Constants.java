package com.choicetrax.client.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.handlers.buy.BeatportBuyHandler;
import com.choicetrax.client.logic.handlers.buy.BuyHandler;
import com.choicetrax.client.logic.handlers.buy.DJDownloadBuyHandler;
import com.choicetrax.client.logic.handlers.buy.JunoBuyHandler;
import com.choicetrax.client.logic.handlers.buy.PrimalBuyHandler;
import com.choicetrax.client.logic.handlers.buy.StompyBuyHandler;
import com.choicetrax.client.logic.handlers.buy.TraxsourceBuyHandler;


public class Constants 
{
	
	// format and price codes
	public static final String FORMAT_MP3 			= "mp3";
	public static final String FORMAT_WAV			= "wav";
	public static final String FORMAT_AAC 			= "aac";
	public static final String FORMAT_MP4			= "mp4";
	
	public static final String CURRENCY_US			= "$";
	public static final String CURRENCY_BRITISH		= "&pound;";
	
	public static final String BITRATE_192K 		= "192k";
	public static final String BITRATE_320K 		= "320k";
	public static final String BITRATE_441KHZ		= "44.1kHz";
	
	
	// external data format types
	public static final int DATA_FORMAT_CSV_TEMPLATE	= 0; 
		
	
	// number of releases to display per page in ReleasesComposite
	public static final int NUM_RELEASES_PER_PAGE		= 10;
	
	// number of charts to display per page
	public static final int NUM_CHARTS_PER_PAGE			= 10;
	
	// number of pages to keep ReleaseDetail objs loaded in ReleasesCache obj
	public static final int NUM_PAGES_TO_CACHE			= 10;
	// (note that this only applies to RELEASES_CACHE_SEARCH 
	//  & RELEASES_CACHE_BROWSE cache types)
	
	// max number of track IDs to load per query
	public static final int MAX_TRACKIDS_PER_QUERY		= 10000;
	
	
	// view request types
	public static final String VIEW_NEW_RELEASES			= "VIEW_NEW_RELEASES";
	public static final String VIEW_UNIQUE_RELEASES			= "VIEW_UNIQUE_RELEASES";
	public static final String VIEW_RECOMMENDED_TRACKS		= "VIEW_RECOMMENDED_TRACKS";
	public static final String VIEW_RECOMMENDED_CHARTS		= "VIEW_RECOMMENDED_CHARTS";
	public static final String VIEW_RATED_RELEASES			= "VIEW_RATED_RELEASES";
	public static final String VIEW_CHART					= "VIEW_CHART";
	public static final String VIEW_WISHLIST				= "VIEW_WISHLIST";
	public static final String VIEW_FAVORITE_ARTISTS		= "VIEW_FAVORITE_ARTISTS";
	public static final String VIEW_FAVORITE_LABELS			= "VIEW_FAVORITE_LABELS";
	public static final String VIEW_FAVORITE_GENRES			= "VIEW_FAVORITE_GENRES";
	
	// UI display panels
	public static final int PANEL_NEWS 					= 0;
	public static final int PANEL_RELEASES 				= 1;
	public static final int PANEL_ACCOUNT_CONFIG 		= 2;
	public static final int PANEL_WISHLIST 				= 3;
	public static final int PANEL_PURCHASE_HISTORY		= 4;
	public static final int PANEL_VIRTUAL_CARTS			= 5;
	public static final int PANEL_DJ_CHARTS_SELECT		= 6;
	public static final int PANEL_DJ_CHARTS_VIEW		= 7;
	public static final int PANEL_PASSWORD_RESET		= 14;
	public static final int PANEL_RECOMMENDED_TRACKS	= 15;
	public static final int PANEL_RECOMMENDED_CHARTS	= 16;
	// non-center panels
	public static final int PANEL_FAVORITES				= 8;
	public static final int PANEL_ACCOUNT_DETAILS		= 9;
	public static final int PANEL_ALBUM_EP_DETAILS		= 10;
	public static final int PANEL_RECOMMENDATIONS		= 11;
	public static final int PANEL_AUDIO_PLAYER			= 12;
	public static final int PANEL_SEARCH				= 13;
	
	
	// favorites tabs
	public static final int FAVORITES_TAB_ARTISTS	= 0;
	public static final int FAVORITES_TAB_LABELS	= 1;
	public static final int FAVORITES_TAB_SEARCHES 	= 2;

	
	// action handler action types
	public static final String ACTION_ADD		= "ACTION_ADD";
	public static final String ACTION_REMOVE	= "ACTION_REMOVE";
	
	// load request types
	public static final String LOAD_FAVORITES			= "LOAD_FAV_ARTISTS_LABELS_GENRES";
	//public static final String LOAD_PURCHASE_HISTORY	= "LOAD_PURCHASE_HISTORY";
	
	// recommended releases load request types
	public static final String LOAD_RECOMMENDED_TRACKS	= "tracks";
	public static final String LOAD_RECOMMENDED_CHARTS	= "charts";
	
	
	// action handler response codes
	public static final int ACTION_RESPONSE_ERROR	= -1;
	public static final int ACTION_RESPONSE_NORMAL	= 0;
	
	// user action types
	public static final String USER_LOGIN		= "LOGIN_USER";
	public static final String USER_AUTOLOGIN	= "LOGIN_AUTO";
	public static final String USER_CREATE		= "CREATE_USER";
	public static final String USER_CONFIG		= "CONFIG_USER";
	
	// history tokens
	public static final String HISTORY_NEWS					= "news";
	public static final String HISTORY_WISHLIST				= "wishlist";
	public static final String HISTORY_VIRTUAL_CARTS		= "virtualcarts";
	public static final String HISTORY_PURCHASE_HISTORY		= "purchasehistory";
	public static final String HISTORY_QUICKSEARCH			= "quicksearch=";
	public static final String HISTORY_SEARCH				= "search=";
	public static final String HISTORY_BROWSE				= "browse=";
	public static final String HISTORY_FAVORITES_SEARCH		= "favsearch=";
	public static final String HISTORY_FAVORITES_CHARTS		= "favcharts=";
	public static final String HISTORY_CHARTS_DJ_SELECT		= "djcharts=";
	public static final String HISTORY_CHARTS_DJ_VIEW		= "djchart=";
	public static final String HISTORY_CHARTS_SALES			= "salescharts=";
	public static final String HISTORY_TRACK_ID				= "id=";
	public static final String HISTORY_PASSWORD_RESET		= "passreset=";
	public static final String HISTORY_RECOMMENDED_TRACKS	= "rt=";
	public static final String HISTORY_RECOMMENDED_CHARTS	= "rc=";

	
	// browse recent history options
	public static final String BROWSE_FUTURE		= "Future";
	public static final String BROWSE_TODAY			= "Today";
	public static final String BROWSE_LAST_7_DAYS	= "Last 7 Days";
	public static final String BROWSE_LAST_14_DAYS	= "Last 14 Days";
	public static final String BROWSE_LAST_21_DAYS	= "Last 21 Days";
	public static final String BROWSE_LAST_MONTH	= "Last 30 Days";
	public static final String BROWSE_LAST_QUARTER	= "Last 90 Days";

	public static final String[] BROWSE_DATE_OPTIONS = new String[] {
														"",
														//BROWSE_FUTURE,
														BROWSE_TODAY,
														BROWSE_LAST_7_DAYS,
														BROWSE_LAST_14_DAYS,
														BROWSE_LAST_21_DAYS,
														BROWSE_LAST_MONTH,
														BROWSE_LAST_QUARTER };
	
	// cookie names
	public static final String COOKIE_SESSIONID 		= "sid";
	public static final String COOKIE_USERID			= "uid";
	public static final String COOKIE_FULLSCREEN		= "full_screen";
	public static final String COOKIE_PLAYLIST			= "playlist";
	public static final String COOKIE_FEATURED_GENRE_ID	= "featgenreid";
	public static final String COOKIE_QUEUE_TRACKS		= "queue_tracks";
	
	// cookie expiration 
	public static final long COOKIE_DURATION_DEFAULT 	= 86400000; // 1 day
	public static final long COOKIE_DURATION_1WEEK		= 604800000; // 1 week
	public static final long COOKIE_DURATION_2WEEKS		= 1209600000; // 2 weeks
	public static final long COOKIE_DURATION_1YEAR		= 31449600000L; // 1 year
	
		
	// Cache types
	public static final int CACHE_RELEASES_QUICKSEARCH		= 0;
	public static final int CACHE_RELEASES_SEARCH			= 1;
	public static final int CACHE_RELEASES_BROWSE			= 2;
	public static final int CACHE_RELEASES_RECOMMENDED		= 3;
	public static final int CACHE_RELEASES_WISHLIST 		= 4;
	public static final int CACHE_RELEASES_PURCHASE_HISTORY	= 5;
	public static final int CACHE_RELEASES_VIRTUAL_CART		= 6;
	public static final int CACHE_CHARTS_DJ					= 7;
	public static final int CACHE_CHARTS_SALES				= 8;
	public static final int CACHE_RELEASES_DJ_TRACKS		= 9;
	public static final int CACHE_RELEASES_SALES_TRACKS		= 10;
	public static final int CACHE_RELEASES_FAVORITES		= 11;
	public static final int CACHE_CHARTS_FAVORITES			= 12;
	public static final int CACHE_CHARTS_DJ_RECOMMENDED		= 13;
	
	
	// releases cache sort options
	public static final String SORT_BY_DEFAULT				= "default";
	public static final String RELEASES_SORT_BY_ARTIST		= "artist";
	public static final String RELEASES_SORT_BY_TRACK		= "track";
	public static final String RELEASES_SORT_BY_MIX			= "mix";
	public static final String RELEASES_SORT_BY_LABEL		= "label";
	public static final String RELEASES_SORT_BY_DATE		= "date";
	public static final String RELEASES_SORT_BY_RATING		= "rating";
	
	// sort types
	public static final String SORT_ORDER_DEFAULT			= "default";
	public static final String SORT_ORDER_ASCENDING			= "asc";
	public static final String SORT_ORDER_DESCENDING		= "desc";
	
	
	// date formatter
	public static DateTimeFormat DATE_FORMATTER_USA = DateTimeFormat.getFormat( "MM/dd/yyyy" );
	
	
	// partner names
	// note that these must match what's in the PartnerSites DB table
	public static final String PARTNER_NAME_TRAXSOURCE 		= "Traxsource";
	public static final String PARTNER_NAME_JUNODOWNLOAD	= "junodownload";
	public static final String PARTNER_NAME_BEATPORT 		= "Beatport";
	public static final String PARTNER_NAME_PRIMAL 			= "Primal Records";
	public static final String PARTNER_NAME_STOMPY 			= "Stompy";
	public static final String PARTNER_NAME_DJDOWNLOAD		= "DJDownload";
	public static final String PARTNER_NAME_XPRESSBEATS		= "Xpressbeats";
	
	// partner cart window names
	public static final String PARTNER_WINDOW_TRAXSOURCE 	= "_choicetrax_traxsource";
	public static final String PARTNER_WINDOW_JUNODOWNLOAD 	= "_choicetrax_junodownload";
	public static final String PARTNER_WINDOW_BEATPORT 		= "_choicetrax_beatport";
	public static final String PARTNER_WINDOW_PRIMAL		= "_choicetrax_primal_records";
	public static final String PARTNER_WINDOW_STOMPY		= "_choicetrax_stompy";
	public static final String PARTNER_WINDOW_DJDOWNLOAD	= "_choicetrax_djdownload";
	public static final String PARTNER_WINDOW_XPRESSBEATS	= "_choicetrax_xpressbeats";
	
	
	// ad image sizes
	public static final String AD_SIZE_TOP_BANNER_WIDTH		= "609px";
	public static final String AD_SIZE_TOP_BANNER_HEIGHT	= "75px";
	
	public static final String AD_SIZE_LEFT_SMALL_WIDTH		= "150px";
	public static final String AD_SIZE_LEFT_SMALL_HEIGHT	= "150px";
	
	public static final String AD_SIZE_LEFT_MEDIUM_WIDTH	= "150px";
	public static final String AD_SIZE_LEFT_MEDIUM_HEIGHT	= "300px";
	
	public static final String AD_SIZE_LEFT_TALL_WIDTH		= "150px";
	public static final String AD_SIZE_LEFT_TALL_HEIGHT		= "600px";
	
	public static final String AD_SIZE_MAIN_NEWS_WIDTH		= "500px";
	public static final String AD_SIZE_MAIN_NEWS_HEIGHT		= "311px";
	
	// ad image types
	public static final String AD_TYPE_TOP_BANNER		= "AD_TOP_BANNER";
	public static final String AD_TYPE_LEFT_SMALL		= "AD_LEFT_SMALL";
	public static final String AD_TYPE_LEFT_MEDIUM		= "AD_LEFT_MEDIUM";
	public static final String AD_TYPE_LEFT_TALL		= "AD_LEFT_TALL";
	public static final String AD_TYPE_MAIN_NEWS		= "AD_MAIN_NEWS";
	
	
	// image bundles
	public static ImageBundleCorners IMAGE_BUNDLE_CORNERS = 
		(ImageBundleCorners) GWT.create( ImageBundleCorners.class );
	
	public static ImageBundleTabs IMAGE_BUNDLE_TABS = 
		(ImageBundleTabs) GWT.create( ImageBundleTabs.class );
	
	
	
	
	private static final String FILTER_PATTERN = "[<>{}\\[\\];\\=]";	
	
	/**
	 * removes/escapes invalid data to protect from javascript attacks
	 * 
	 * @param s
	 * @return
	 */
	public static String escapeChars( String s )
	{
		if ( s == null ) return null;
		
		s = s.replaceAll( FILTER_PATTERN, "" );
		s = s.replace( "'" , "" );
		
		return s;
	}
	
	
	
	public static String getPartnerWindowName( String partnerName )
	{
		if ( PARTNER_NAME_TRAXSOURCE.equals( partnerName ) )
			return PARTNER_WINDOW_TRAXSOURCE;
		else if ( PARTNER_NAME_JUNODOWNLOAD.equals( partnerName ) )
			return PARTNER_WINDOW_JUNODOWNLOAD;
		else if ( PARTNER_NAME_BEATPORT.equals( partnerName ) )
			return PARTNER_WINDOW_BEATPORT;
		else if ( PARTNER_NAME_PRIMAL.equals( partnerName ) )
			return PARTNER_WINDOW_PRIMAL;
		else if ( PARTNER_NAME_STOMPY.equals( partnerName ) )
			return PARTNER_WINDOW_STOMPY;
		else if ( PARTNER_NAME_DJDOWNLOAD.equals( partnerName ) )
			return PARTNER_WINDOW_DJDOWNLOAD;
		else if ( PARTNER_NAME_XPRESSBEATS.equals( partnerName ) )
			return PARTNER_WINDOW_XPRESSBEATS;
		else
			return "_choicetrax";
	}
	
	
	
	public static String readStackTrace( Throwable t )
	{
		if ( t == null )
			return "stackTrace not available: t is null";
		
		StackTraceElement[] trace = t.getStackTrace(); 
		StringBuffer sb = new StringBuffer(); 
		 
		sb.append( t.toString() + ": " + t.getMessage() ); 
		
		// cut the stacktrace at depth 7 
		int length = Math.min( 7, trace.length ); 		
		for ( int i=0; i < length; i++ ) 
		{ 
			sb.append( "\n\t" 
						+ "at "
						+ trace[ i ].toString() );
		} 
		if ( trace.length > 7 ) sb.append( " ..." ); 

		return sb.toString();
	}
	
	
	public static String createHistoryToken( int cacheType, String actionString, int page )
	{
		String historyToken = "";
		
		if ( cacheType == Constants.CACHE_RELEASES_SEARCH )
			historyToken += Constants.HISTORY_SEARCH + actionString;
		else if ( cacheType == Constants.CACHE_RELEASES_QUICKSEARCH )
			historyToken += Constants.HISTORY_QUICKSEARCH + actionString;
		else if ( cacheType == Constants.CACHE_RELEASES_BROWSE )
			historyToken += Constants.HISTORY_BROWSE + actionString;
		else if ( cacheType == Constants.CACHE_RELEASES_WISHLIST )
			historyToken += Constants.HISTORY_WISHLIST;
		else if ( cacheType == Constants.CACHE_RELEASES_VIRTUAL_CART )
			historyToken += Constants.HISTORY_VIRTUAL_CARTS + actionString;
		else if ( cacheType == Constants.CACHE_RELEASES_PURCHASE_HISTORY )
			historyToken += Constants.HISTORY_PURCHASE_HISTORY + actionString;
		else if ( cacheType == Constants.CACHE_CHARTS_DJ )
			historyToken += Constants.HISTORY_CHARTS_DJ_SELECT + actionString;
		else if ( cacheType == Constants.CACHE_RELEASES_DJ_TRACKS )
			historyToken += Constants.HISTORY_CHARTS_DJ_VIEW + actionString;
		else if ( cacheType == Constants.CACHE_CHARTS_SALES )
			historyToken += Constants.HISTORY_CHARTS_SALES + actionString;
		else if ( cacheType == Constants.CACHE_RELEASES_FAVORITES )
			historyToken += Constants.HISTORY_FAVORITES_SEARCH + actionString;
		else if ( cacheType == Constants.CACHE_CHARTS_FAVORITES )
			historyToken += Constants.HISTORY_FAVORITES_CHARTS + actionString;
		else if ( cacheType == Constants.CACHE_RELEASES_RECOMMENDED )
			historyToken += Constants.HISTORY_RECOMMENDED_TRACKS + actionString;
		else if ( cacheType == Constants.CACHE_CHARTS_DJ_RECOMMENDED )
			historyToken += Constants.HISTORY_RECOMMENDED_CHARTS + actionString;
		
		historyToken += "page=" + page;
		
		return historyToken;
	}
	
	
	
	public static BuyHandler createBuyHandler( ChoicetraxViewManager viewManager, 
												String partnerName ) 
	{
		BuyHandler buyHandler = null;
		
		if ( Constants.PARTNER_NAME_TRAXSOURCE.equals( partnerName ) )
			buyHandler = new TraxsourceBuyHandler( viewManager );
		else if ( Constants.PARTNER_NAME_BEATPORT.equals( partnerName ) )
			buyHandler = new BeatportBuyHandler( viewManager );
		else if ( Constants.PARTNER_NAME_PRIMAL.equals( partnerName ) )
			buyHandler = new PrimalBuyHandler( viewManager );
		else if ( Constants.PARTNER_NAME_JUNODOWNLOAD.equals( partnerName ) )
			buyHandler = new JunoBuyHandler( viewManager );
			//buyHandler = new JunoBuyAtOnceHandler( viewManager );
			// TODO: try to make JunoBuyAtOnceHandler work
		else if ( Constants.PARTNER_NAME_STOMPY.equals( partnerName ) )
			buyHandler = new StompyBuyHandler( viewManager );
		else if ( Constants.PARTNER_NAME_DJDOWNLOAD.equals( partnerName ) )
			buyHandler = new DJDownloadBuyHandler( viewManager );
			
		return buyHandler;
	}
	
	
	/**
	 * Creates a URL that links directly to the release/EP at the partner site.
	 * 
	 * Note that currenly all partners only need the releaseID to create the link
	 * but that may change as more partner sites are added.
	 * 
	 * @param partnerName
	 * @param releaseID
	 * @return
	 */
	public static String createDirectEpLink( String partnerName, int releaseID )
	{
		String url = null;
		
		if ( Constants.PARTNER_NAME_TRAXSOURCE.equals( partnerName ) ) {
			url = "http://www.traxsource.com/"
				+ "index.php?act=show&fc=tpage&cr=titles&cv=" + releaseID + "&alias=downloads";
		}
		else if ( Constants.PARTNER_NAME_BEATPORT.equals( partnerName ) ) {
			url = "https://www.beatport.com/en-US/html/content/release/detail/" + releaseID + "/";
		}
		else if ( Constants.PARTNER_NAME_PRIMAL.equals( partnerName ) ) {
			url = "http://www.primalrecords.com/store/release.php?productid=" + releaseID;
		}
		else if ( Constants.PARTNER_NAME_JUNODOWNLOAD.equals( partnerName ) ) {
			url = "http://www.junodownload.com/products/" + releaseID + "-02.htm?ref=ctrx";
		}
		else if ( Constants.PARTNER_NAME_STOMPY.equals( partnerName ) ) {
			url = "https://www.stompy.com/EP/" + releaseID;
		}
		else if ( Constants.PARTNER_NAME_DJDOWNLOAD.equals( partnerName ) ) {
			url = "http://www.djdownload.com/mp3-detail/x/y/z/" + releaseID;
		}
			
		return url;
	}
	
	
	public static String readFirstWord( String text ) 
	{
		if ( text == null || "".equals( text ) ) return null;
		
		int index = text.indexOf( ' ' );
		if ( index != -1 ) {
			return text.substring( 0, index );
		}
		else 
			return text;
	}



	/**
	 * This method takes as input a hashtable with genreIDs as keys and count of occurrences
	 * of each ID as the value.  It returns an int array of the genreIDs, ordered by
	 * the count of occurrences from high to low.
	 * 
	 * @param genreCount
	 * @return
	 */
	public static LinkedList<Integer> sortHashValuesToOrderedIdList( HashMap<Integer,Integer> hash ) 
	{
		LinkedList<Integer> genreIDs = new LinkedList<Integer>();
		
		ArrayList<Integer> values = new ArrayList<Integer>( hash.values() );
		Collections.sort( values ); // sort values in ascending order
		Collections.reverse( values ); // reverse order so highest values are first
		
		Iterator<Integer> i = values.iterator();
		while ( i.hasNext() )
		{
			int count = i.next();
			
			// get genreID associated to this count
			Iterator<Integer> keys = hash.keySet().iterator();
			while ( keys.hasNext() ) 
			{
				int genreID = keys.next();
				int genreCount = hash.get( genreID );
				
				if ( count == genreCount ) {
					genreIDs.add( genreID );
					hash.remove( genreID );
					break;
				}
			}
		}
				
		return genreIDs;
	}
		
}
