package com.choicetrax.client.logic;

import java.util.Date;
import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.StatusCodeException;

import com.choicetrax.client.Choicetrax;
import com.choicetrax.client.actions.Action;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.handleractions.ClientLoggerAction;
import com.choicetrax.client.actions.loaderactions.*;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.*;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.data.config.AdFeaturedReleases;
import com.choicetrax.client.data.config.AdImages;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.data.config.ChoicetraxInitialData;
import com.choicetrax.client.display.ChoicetraxView;
import com.choicetrax.client.display.ChoicetraxViewComponent;
import com.choicetrax.client.logic.callbacks.*;
import com.choicetrax.client.util.exception.AudioPlayerException;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.client.util.exception.FailedAuthenticationException;


public class ChoicetraxViewManager
	implements UncaughtExceptionHandler, ValueChangeHandler<String>
{

	private static DateTimeFormat dtFormat = DateTimeFormat.getFormat( "HH:mm:ss:SSS yyyy-MM-dd" );

	// configuration data
	private ChoicetraxInitialData initialData = null;

	// ReleasesCache object of current search
	private Cache currentResultsCache = null;

	// stack of user actions
	private LinkedList<String> userActions = new LinkedList<String>();
	private static int MAX_ACTIONS = 20;

	// view object - layout & composites
	private ChoicetraxView view = null;

	private ChoicetraxServiceManager serviceManager = null;
	private ChoicetraxListenManager listenManager	= null;
	private ChoicetraxUserManager userManager		= null;
	private ChoicetraxAdManager adManager			= null;



	public ChoicetraxViewManager()
	{
		serviceManager 	= new ChoicetraxServiceManager();
		listenManager	= new ChoicetraxListenManager( this );
		userManager		= new ChoicetraxUserManager( this );
		adManager		= new ChoicetraxAdManager( this );
		view 			= new ChoicetraxView( this );

		listenManager.setTopPlayer( view.getAudioPlayerComposite() );
		listenManager.setFullPlayer( view.getAudioPlaylistComposite() );


		GWT.setUncaughtExceptionHandler( this );
		History.addValueChangeHandler( this );

		// load config
		executeAction( new LoadInitialDataAction(), new InitialDataLoadController( this ) );
	}



	public void initialize( ChoicetraxInitialData initialData )
	{
		this.initialData = initialData;

		adManager.initialize();
		userManager.initialize();
		listenManager.initialize();
		view.initialize();

		// initialize actual HTML page
		Choicetrax.initialize();

		// wait to do this until after app has fully initialized itself
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				History.fireCurrentHistoryState();
			}
		});
	}



	/**
	 * Replaces historyListener
	 */
	public void onValueChange( ValueChangeEvent<String> event ) {
		 onHistoryChanged( event.getValue() );
	}


	public void onHistoryChanged( String historyToken )
	{
		try
		{
			if ( historyToken == null ) return;
			else historyToken = URL.decode( historyToken );
			//else historyToken = URL.decodeComponent( historyToken );

			// removing the _ is screwing up some emails related to password resets
			if ( ! historyToken.startsWith( Constants.HISTORY_PASSWORD_RESET ) )
				historyToken = historyToken.replace( '_', ' ' );

			logUserAction( "[history] " + historyToken );

			int firstEqualIndex = historyToken.indexOf( '=' ) + 1;
			int endIndex = historyToken.length();
			int pageIndex = historyToken.indexOf( "page=" );
			int pageNum = 1;

			if ( pageIndex != -1 ) 	{
				endIndex = pageIndex;
				pageNum = Integer.parseInt( historyToken.substring( pageIndex + 5 ) );
			}

			if ( historyToken.startsWith( Constants.HISTORY_QUICKSEARCH )
					|| historyToken.startsWith( Constants.HISTORY_SEARCH )
					|| historyToken.startsWith( Constants.HISTORY_BROWSE )
					|| historyToken.startsWith( Constants.HISTORY_FAVORITES_SEARCH )
					|| historyToken.startsWith( Constants.HISTORY_FAVORITES_CHARTS )
					|| historyToken.startsWith( Constants.HISTORY_CHARTS_DJ_SELECT )
					|| historyToken.startsWith( Constants.HISTORY_CHARTS_DJ_VIEW )
					|| historyToken.startsWith( Constants.HISTORY_TRACK_ID )
					|| historyToken.startsWith( Constants.HISTORY_RECOMMENDED_TRACKS )
					|| historyToken.startsWith( Constants.HISTORY_RECOMMENDED_CHARTS ) )
			{
				// parse token to determine search terms
				// if current Releases display matches this search,
				//   just change page if necessary
				// if this is a different search, execute this search
				//   and update Releases display

				// Constants.HISTORY_SEARCH "=" + cache.getSearchTerms()
				//	+ "&page=" + cache.getCurrentPage();

				// search=(search terms)&page=123
				String actionString = historyToken.substring( firstEqualIndex, endIndex );

				if ( ( currentResultsCache != null )
					&& actionString.equals( currentResultsCache.getActionString() ) )
					// note that if the actionString is the same - "artists(true)labels(false)" - this
					// will cause a problem if it's actually a different search
				{
					currentResultsCache.setCurrentPage( pageNum );

					int displayPanel = -1;
					if ( historyToken.startsWith( Constants.HISTORY_CHARTS_DJ_SELECT )
							|| historyToken.startsWith( Constants.HISTORY_FAVORITES_CHARTS ) )
						displayPanel = Constants.PANEL_DJ_CHARTS_SELECT;
					else if ( historyToken.startsWith( Constants.HISTORY_CHARTS_DJ_VIEW ) )
						displayPanel = Constants.PANEL_DJ_CHARTS_VIEW;
					else if ( historyToken.startsWith( Constants.HISTORY_RECOMMENDED_TRACKS ) )
						displayPanel = Constants.PANEL_RECOMMENDED_TRACKS;
					else if ( historyToken.startsWith( Constants.HISTORY_RECOMMENDED_CHARTS ) )
						displayPanel = Constants.PANEL_RECOMMENDED_CHARTS;
					else
						// else use default releases display
						displayPanel = Constants.PANEL_RELEASES;

					updateDisplay( displayPanel, currentResultsCache );
				}
				else
				{
					LoaderHistoryAction action = null;
					SearchResultsController callback = null;

					if ( historyToken.startsWith( Constants.HISTORY_QUICKSEARCH ) ) {
						action = new ReleasesSearchQuickAction( actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_SEARCH ) ) {
						action = new ReleasesSearchAdvancedAction( actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_BROWSE ) ) {
						action = new ReleasesBrowseAction( actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_FAVORITES_SEARCH ) ) {
						action = new SearchTrackFavoritesAction( getCurrentUser(), actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_FAVORITES_CHARTS ) ) {
						action = new SearchDJChartsFavoritesAction( getCurrentUser(), actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
						callback.setRedirectPanel( Constants.PANEL_DJ_CHARTS_SELECT );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_CHARTS_DJ_SELECT ) ) {
						action = new DJChartsSearchAction( actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
						callback.setRedirectPanel( Constants.PANEL_DJ_CHARTS_SELECT );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_CHARTS_DJ_VIEW ) ) {
						action = new ViewDJChartAction( actionString );
						callback = new SearchResultsDJChartController( this, actionString, pageNum );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_TRACK_ID ) ) {
						action = new LoadTrackIDAction( actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_RECOMMENDED_TRACKS ) ) {
						action = new RecommendedTracksAction( actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
						callback.setRedirectPanel( Constants.PANEL_RECOMMENDED_TRACKS );
					}
					else if ( historyToken.startsWith( Constants.HISTORY_RECOMMENDED_CHARTS ) ) {
						action = new RecommendedDJChartsAction( actionString );
						callback = new SearchResultsController( this, actionString, pageNum );
						callback.setRedirectPanel( Constants.PANEL_RECOMMENDED_CHARTS );
					}

					if ( getCurrentUser() != null )
						action.setUserID( getCurrentUser().getUserID() );

					action.setRequestedPage( pageNum );
					executeAction( action, callback );
				}
			}
			else if ( historyToken.startsWith( Constants.HISTORY_WISHLIST ) )
			{
				if ( getCurrentUser() != null )
				{
					ReleasesCache wishlist = getCurrentUser().getWishlist();
					wishlist.setCurrentPage( pageNum );

					updateDisplay( Constants.PANEL_WISHLIST, wishlist );
				}
				else
					History.newItem( "" );
			}
			else if ( historyToken.startsWith( Constants.HISTORY_VIRTUAL_CARTS ) )
			{
				if ( getCurrentUser() != null )
				{
					VirtualCarts multiCart = getCurrentUser().getVirtualCarts();
					VirtualCart cart = multiCart.getCurrentCart();
					if ( cart != null ) cart.getReleasesCache().setCurrentPage( pageNum );

					updateDisplay( Constants.PANEL_VIRTUAL_CARTS, multiCart );
				}
				else
					History.newItem( "" );
			}
			else if ( historyToken.startsWith( Constants.HISTORY_PURCHASE_HISTORY ) )
			{
				if ( getCurrentUser() != null )
				{
					VirtualCarts multiCart = getCurrentUser().getPurchaseHistory();
					VirtualCart cart = multiCart.getCurrentCart();
					if ( cart != null ) cart.getReleasesCache().setCurrentPage( pageNum );

					updateDisplay( Constants.PANEL_PURCHASE_HISTORY, multiCart );
				}
				else
					History.newItem( "" );
			}
			else if ( historyToken.startsWith( Constants.HISTORY_NEWS ) )
			{
				showCenterPanel( Constants.PANEL_NEWS );

				// show top paid ads
				adManager.refreshTopAds();
			}
			else if ( historyToken.startsWith( Constants.HISTORY_PASSWORD_RESET ) )
			{
				String actionString = historyToken.substring( firstEqualIndex, endIndex );

				PasswordResetAction action = new PasswordResetAction( actionString );
				PasswordResetController callback = new PasswordResetController( this );

				executeAction( action, callback );
			}
			else if ( "".equals( historyToken ) )
			{
				adManager.refreshTopAds();
			}
			else
			{
				adManager.refreshNextAds();
			}
		}
		catch ( Exception e )
		{
			handleError( e, this.getClass().getName() + ".onHistoryChanged()" );
		}

		// only call urchinTracker when in web mode (ie. not hosted development mode)
		if ( ! "".equals( historyToken ) && GWT.isScript() )
			view.jsCallUrchinTracker( "/" + historyToken );
	}



	private void logUserAction( String action )
	{
		userActions.add( action + " @ " + dtFormat.format( new Date() ) );
		if ( userActions.size() > MAX_ACTIONS ) userActions.remove( 0 );
	}


	public void executeAction( Action action, ChoicetraxCallback callback )
	{
		try
		{
			serviceManager.callService( action, callback );
		}
		catch( ChoicetraxException cte )
		{
			handleError( cte, this.getClass().getName() + ".executeAction()" );
		}

		logUserAction( "[execute] " + action.getLogString() );

		//deferAction( action, callback );
	}


	public void deferAction( Action action, ChoicetraxCallback callback )
	{
		// put non-immediate actions into a DeferredCommand
		// to improve application responsiveness

		DeferredCommand.addCommand( new DeferAction( action, callback ) );

		//logUserAction( "[defer] " + action.getLogString() );
	}


	public void executeUserSearch( String searchName )
	{
		LoaderHistoryAction action = getCurrentUser().getFavoriteSearch( searchName );

		com.choicetrax.client.display.SearchComposite sc = view.getSearchComposite();
		sc.updateDisplay( action );

		SearchResultsController callback = new SearchResultsController(
													this, action.getActionString(), 1 );

		if ( action instanceof DJChartsSearchAction )
			callback.setRedirectPanel( Constants.PANEL_DJ_CHARTS_SELECT );

		executeAction( action, callback );
	}


	public ChoicetraxView getView() {
		return this.view;
	}

	public ChoicetraxListenManager getListenManager() {
		return this.listenManager;
	}

	public ChoicetraxUserManager getUserManager() {
		return this.userManager;
	}

	public ChoicetraxAdManager getAdManager() {
		return this.adManager;
	}


	public ChoicetraxConfigData getConfigData() {
		if ( initialData != null )
			return this.initialData.getConfigData();
		else
			return null;
	}

	public AdImages getAdImages() {
		return this.initialData.getImgLoad();
	}

	public AdFeaturedReleases getFeaturedReleases() {
		return this.initialData.getFeaturedReleases();
	}

	public Cache getCurrentSearchCache() {
		return this.currentResultsCache;
	}

	public void setCurrentSearchCache( Cache cache ) {
		this.currentResultsCache = cache;
	}


	private ChoicetraxViewComponent lookupViewComponent( int viewComposite )
	{
		ChoicetraxViewComponent viewComponent = null;

		if ( viewComposite == Constants.PANEL_RELEASES )
			viewComponent = view.getCenterPanel().getReleasesComposite();
		else if ( viewComposite == Constants.PANEL_ALBUM_EP_DETAILS )
			viewComponent = view.getAlbumComposite();
		else if ( viewComposite == Constants.PANEL_ACCOUNT_DETAILS )
			viewComponent = view.getAccountComposite();
		else if ( viewComposite == Constants.PANEL_ACCOUNT_CONFIG )
			viewComponent = view.getCenterPanel().getAccountConfigComposite();
		else if ( viewComposite == Constants.PANEL_FAVORITES )
			viewComponent = view.getFavoritesComposite();
		else if ( viewComposite == Constants.PANEL_RECOMMENDATIONS )
			viewComponent = view.getRecommendationsComposite();
		else if ( viewComposite == Constants.PANEL_SEARCH )
			viewComponent = view.getSearchComposite();
		else if ( viewComposite == Constants.PANEL_WISHLIST )
			viewComponent = view.getCenterPanel().getWishlistComposite();
		else if ( viewComposite == Constants.PANEL_PURCHASE_HISTORY )
			viewComponent = view.getCenterPanel().getPurchaseHistoryComposite();
		else if ( viewComposite == Constants.PANEL_VIRTUAL_CARTS )
			viewComponent = view.getCenterPanel().getVirtualCartsComposite();
		else if ( viewComposite == Constants.PANEL_DJ_CHARTS_SELECT )
			viewComponent = view.getCenterPanel().getDJChartSelectComposite();
		else if ( viewComposite == Constants.PANEL_DJ_CHARTS_VIEW )
			viewComponent = view.getCenterPanel().getDJChartReleasesComposite();
		else if ( viewComposite == Constants.PANEL_RECOMMENDED_TRACKS )
			viewComponent = view.getCenterPanel().getRecommendedTracksComposite();
		else if ( viewComposite == Constants.PANEL_RECOMMENDED_CHARTS )
			viewComponent = view.getCenterPanel().getRecommendedDJChartsComposite();
		else
			Window.alert( "Unknown panel code: " + viewComposite );

		return viewComponent;
	}


	public void setWaitingState( int viewComposite, boolean waiting )
	{
		ChoicetraxViewComponent viewComponent = lookupViewComponent( viewComposite );

		if ( viewComponent != null )
			viewComponent.setWaitingState( waiting );
	}


	public void updateDisplay( int viewComposite, LoaderResponse responseObj )
	{
		Cache cache = null;

		if ( responseObj instanceof ReleasesCache ) {
			cache = (ReleasesCache) responseObj;
		}
		else if ( responseObj instanceof VirtualCarts )
		{
			VirtualCart cart = ((VirtualCarts) responseObj).getCurrentCart();
			if ( cart != null ) cache = cart.getReleasesCache();
		}
		else if ( responseObj instanceof DJChartsCache )
		{
			cache = (DJChartsCache) responseObj;
		}

		if ( cache != null )
		{
			// check for cache misses
			int currentPage= cache.getCurrentPage();
			if ( ! cache.isPagePopulated( currentPage ) )
			{
				// workaround to get around infinite loops.
				// TODO: probably need to build prefetching into cache model
				cache.setPagePopulated( currentPage );

				// load releases from server
				LoaderAction action = cache.getCacheLoaderAction( currentPage );

				if ( action != null ) {
					executeAction( action,
							new CacheLoadController( this, cache, currentPage, viewComposite ) );
					return;
				}
			}
		}

		ChoicetraxViewComponent viewComponent = lookupViewComponent( viewComposite );

		if ( viewComponent != null )
			viewComponent.updateDisplay( responseObj );

		if ( viewComposite == Constants.PANEL_ALBUM_EP_DETAILS )
			view.showRightPanel( viewComposite );

		if ( cache != null ) {
			// show targeted ads
			final Cache c = cache;
			DeferredCommand.addCommand( new Command() {
				public void execute() {
					adManager.refreshTargetedAds( c );
				}
			});
		}
	}


	public void updateHistoryDisplay( String historyToken )
	{
		String token = historyToken.replace( ' ', '_' );
		token = URL.encode( token );
		//token = URL.encodeComponent( token );
		History.newItem( token );
		//History.newItem( URL.encode( historyToken ) );
		//History.newItem( historyToken );
	}


	public void updateSearchDisplay( Cache cache )
	{
		setCurrentSearchCache( cache );

		String historyToken = Constants.createHistoryToken( cache.getCacheType(),
															cache.getActionString(),
															cache.getCurrentPage() );

		updateHistoryDisplay( historyToken );
	}


	public void clearViewComponent( int viewComposite )
	{
		ChoicetraxViewComponent viewComponent = lookupViewComponent( viewComposite );

		if ( viewComponent != null )
			viewComponent.clearDisplay();
	}



	public void handleError( Throwable t, String errorSource )
	{
		String errorMsg = null;
		if ( t instanceof IncompatibleRemoteServiceException )
		{
			errorMsg = "An error has occurred."
						+ "\n\n"
						+ "It appears that you need to refresh your browser as we have "
						+ "made a code update."
						+ "\n\n"
						+ "We are constantly working to make Choicetrax better - if you "
						+ "experience this \n"
						+ "again after a refresh please notify us at dave@choicetrax.com."
						+ "\n\n"
						+ "Thanks!  And sorry for the inconvenience."
						+ "\n\n"
						+ "original error message: \n"
						+ t.getMessage();
		}
		else if ( t instanceof StatusCodeException )
		{
			StatusCodeException sce = (StatusCodeException) t;
			int statusCode = sce.getStatusCode();

			errorMsg = "An error has occurred."
						+ "\n\n"
						+ "It appears that there is a network problem between your computer "
						+ "and our server.  Please try your request again."
						+ "\n\n"
						+ "We are constantly working to make Choicetrax better - if you "
						+ "continue to experience \n"
						+ "this problem please notify us at dave@choicetrax.com."
						+ "\n\n"
						+ "Thanks!  And sorry for the inconvenience."
						+ "\n\n"
						+ "original error message: \n"
						+ sce.getMessage() + "\n"
						+ Constants.readStackTrace( sce.getCause() ) + "\n"
						+ "status code: " + statusCode;
		}
		else if ( t instanceof JavaScriptException )
		{
			String msg = t.getMessage();
			if ( ( msg != null ) && ( msg.indexOf( "URI" ) != -1 ) )
				errorMsg = "An error has occurred."
						+ "\n\n"
						+ "The link you clicked on to get to this page is not formatted "
						+ "correctly.\n"
						+ "Please run your search manually using the input text boxes."
						+ "\n\n"
						+ "We are constantly working to make Choicetrax better - if you "
						+ "continue to experience \n"
						+ "this problem please notify us at dave@choicetrax.com."
						+ "\n\n"
						+ "Thanks!  And sorry for the inconvenience.";
		}
		else if ( ( t instanceof ChoicetraxException )
				&& ( t.getMessage().indexOf( "incomplete reply" ) != -1 ) )
		{
			errorMsg = "An error has occurred."
					+ "\n\n"
					+ "Your search terms contain unrecognized letters or characters. \n"
					+ "Please remove any accented or non-ASCII characters "
					+ "and try your search again."
					+ "\n\n"
					+ "We are constantly working to make Choicetrax better - if you "
					+ "continue to experience this problem \n"
					+ "please notify us at dave@choicetrax.com."
					+ "\n\n"
					+ "Thanks!  And sorry for the inconvenience.";
		}
		else
			errorMsg = t.getMessage();

		if ( errorMsg == null )
			errorMsg = "An unknown error has occurred. "
					+ "\n\n"
					+ "All error messages are logged and we will be investigating this ASAP. "
					+ "We are constantly working to make Choicetrax better - if you "
					+ "continue to experience this problem \n"
					+ "please notify us at dave@choicetrax.com."
					+ "\n\n"
					+ "Thanks!  And sorry for the inconvenience.";


		// send non-login exceptions to server side for logging
		if ( ! ( t instanceof FailedAuthenticationException )
			&& ! ( t instanceof JavaScriptException ) )
		{
			final ClientLoggerAction action = new ClientLoggerAction();

			String exceptionText = errorMsg
									+ "\n\n"
									+ "Error source: " + errorSource
									+ "\n\n"
									+ "Client Stacktrace: "
									+ Constants.readStackTrace( t );

			action.setExceptionText( exceptionText );
			action.setBrowserName( view.jsDetectBrowser() );
			action.setActions( userActions );

			if ( getCurrentUser() != null ) {
				action.setUserID( getCurrentUser().getUserID() );
				action.setUsername( getCurrentUser().getUserName() );
			}

			//deferAction( action, new DoNothingController() );
			DeferredCommand.addCommand( new Command() {
				public void execute() {
					try {
						serviceManager.callService( action, new DoNothingController() );
					} catch ( Exception e ) {
						GWT.log( "Error logging client side exception", null );
					}
				}
			});
		}

		String alertToken = null;
		if ( t instanceof AudioPlayerException )
			alertToken = "audioAlert";
		else if ( t instanceof FailedAuthenticationException )
			alertToken = "loginAlert";
		else
			alertToken = "alert";


		GWT.log( "ChoicetraxViewManager.handleError()", t );

		// temporarily turn off javascript exception notification to the users
		if ( ! ( t instanceof JavaScriptException ) )
		{
			Window.alert( errorMsg );

			History.newItem( alertToken );
		}

		/*}
		catch (IncompatibleRemoteServiceException e)
		{
			// this client is not compatible with the server; cleanup and refresh the
			// browser
		}
		catch (InvocationException e)
		{
			// the call didn't complete cleanly
		//} catch (ShapeException e) {
			// one of the 'throws' from the original method
		//} catch (DbException e) {
			// one of the 'throws' from the original method
		}
		catch (Throwable e)
		{
			// last resort -- a very unexpected exception
		}*/
	}

	public void onUncaughtException( Throwable t )
	{
		handleError( t, this.getClass().getName() + ".onUncaughtException()" );
	}


	public void showCenterPanel( int panelCode )
	{
		// load appropriate page in main panel
		view.getCenterPanel().showPanel( panelCode );
		view.jsScrollWindowToTop();
	}



	public void setCurrentUser( User user )
	{
		userManager.setCurrentUser( user );

		clearViewComponent( Constants.PANEL_ACCOUNT_CONFIG );

		updateDisplay( Constants.PANEL_ACCOUNT_CONFIG, user );
		updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, user );
		updateDisplay( Constants.PANEL_FAVORITES, user );
		updateDisplay( Constants.PANEL_RECOMMENDATIONS, user );


		String sessionID = null;
		String userID = null;

		if ( user != null )
		{
			sessionID = user.getSessionID();
			userID = user.getUserID() + "";
		}

		Choicetrax.setCookie( Constants.COOKIE_SESSIONID,
								sessionID,
								Constants.COOKIE_DURATION_1WEEK );
		Choicetrax.setCookie( Constants.COOKIE_USERID,
								userID,
								Constants.COOKIE_DURATION_1WEEK );
	}

	public User getCurrentUser()
	{
		return userManager.getCurrentUser();
	}


	public void logoffUser()
	{
		// need to clear any data in AccountConfigComposite
		// and wherever else - also reset favorites &
		// recommended panels to  their original states...

		userManager.setCurrentUser( null );

		updateHistoryDisplay( "logoff" );

		Choicetrax.setCookie( Constants.COOKIE_SESSIONID, null );
		Choicetrax.setCookie( Constants.COOKIE_USERID, null );

		clearViewComponent( Constants.PANEL_ACCOUNT_CONFIG );
		clearViewComponent( Constants.PANEL_FAVORITES );
		clearViewComponent( Constants.PANEL_RECOMMENDATIONS );
		clearViewComponent( Constants.PANEL_ALBUM_EP_DETAILS );
		clearViewComponent( Constants.PANEL_VIRTUAL_CARTS );
		clearViewComponent( Constants.PANEL_WISHLIST );
		clearViewComponent( Constants.PANEL_PURCHASE_HISTORY );

		showCenterPanel( Constants.PANEL_NEWS );
	}



	private class DeferAction implements Command
	{
		private Action action;
		private ChoicetraxCallback callback;

		public DeferAction( Action a, ChoicetraxCallback c )
		{
			this.action = a;
			this.callback = c;
		}

		public void execute()
		{
			executeAction( action, callback );
		}
	}

}
