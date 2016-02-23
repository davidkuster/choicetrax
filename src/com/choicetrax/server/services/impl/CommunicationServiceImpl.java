package com.choicetrax.server.services.impl;

import javax.servlet.ServletException;
import java.net.URL;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.Logger;

import com.choicetrax.client.actions.*;
import com.choicetrax.client.actions.handleractions.*;
import com.choicetrax.client.actions.loaderactions.*;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.services.CommunicationService;
import com.choicetrax.client.util.exception.ChoicetraxDatabaseException;
import com.choicetrax.client.util.exception.ChoicetraxEmailException;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.client.util.exception.ChoicetraxSphinxException;
import com.choicetrax.client.util.exception.FailedAuthenticationException;

import com.choicetrax.server.util.CacheCheckerInitialData;
import com.choicetrax.server.util.CacheCheckerSearchTracking;
import com.choicetrax.server.util.EchoNestApiManager;
import com.choicetrax.server.util.jaxb.JAXBHelper;
import com.choicetrax.server.util.jdbc.ResourceManager;
import com.choicetrax.server.db.*;
import com.choicetrax.server.db.handlers.*;
import com.choicetrax.server.db.loaders.*;
import com.choicetrax.server.db.loaders.sphinx.BrowseReleasesLoader;
import com.choicetrax.server.db.loaders.sphinx.DJChartsSearchLoader;
import com.choicetrax.server.db.loaders.sphinx.SearchDJChartsFavoritesLoader;
import com.choicetrax.server.db.loaders.sphinx.SearchReleasesAdvancedLoader;
import com.choicetrax.server.db.loaders.sphinx.SearchReleasesQuickLoader;
import com.choicetrax.server.db.loaders.sphinx.SearchTrackFavoritesLoader;


public class CommunicationServiceImpl 
	extends RemoteServiceServlet 
	implements CommunicationService
{
	
	private static final long	serialVersionUID	= 1L;
	
	private static final String DB_PROPERTIES 		= "database";
	private static final String LOG4J_PROPS 		= "log4j_server.properties";
	private static final String PASS_RESET_PROPS	= "pass_reset_email.properties";
	
	private static Logger logger = Logger.getLogger( CommunicationServiceImpl.class );
	
	private static CacheCheckerInitialData initialDataCacheChecker = null;
	private static CacheCheckerSearchTracking trackingCacheChecker = null;
	
	private static final String EXCEPTION_TEXT_SUFFIX = 
		"All errors are logged and we will be investigating "
		+ "this ASAP.  Sorry for the inconvenience!";
	
	private static final String DATABASE_EXCEPTION_TEXT = 
		"A database error has occurred. Obviously we're not as clever as we think we are. :( "
		+ EXCEPTION_TEXT_SUFFIX;
	
	private static final String SPHINX_EXCEPTION_TEXT =
		"An error has occurred with our search system.  Please try your request again. "
		+ EXCEPTION_TEXT_SUFFIX;
	
	private static final String EMAIL_EXCEPTION_TEXT =
		"An error has occurred sending an email. "
		+ EXCEPTION_TEXT_SUFFIX;
	
	private static final String UNHANDLED_EXCEPTION_TEXT =
		"An unknown error has occurred. "
		+ EXCEPTION_TEXT_SUFFIX;
	

	/**
	 * initializes ResourceManager
	 */
	public void init() 
		throws ServletException
	{
		super.init();
		
		try
		{
			// initialize log4j
			URL url = Loader.getResource( LOG4J_PROPS );
			PropertyConfigurator.configure( url );
			logger.info( "initialized log4j logger" );
			
			// initialize database connections
			ResourceManager.initialize( DB_PROPERTIES );
			logger.info( "initialized ResourceManager with DB properties file [" + DB_PROPERTIES + "]" );
			
			// initialize initial partner & genre config data
			initialDataCacheChecker = new CacheCheckerInitialData();
			initialDataCacheChecker.start();
			logger.info( "initialized initial data cache checker thread" );
			
			// initialize user search tracking thread
			trackingCacheChecker = new CacheCheckerSearchTracking();
			trackingCacheChecker.start();
			logger.info( "initialized tracking cache checker thread" );
			
			// initialize Echo Nest API manager
			EchoNestApiManager.getInstance().start();
			logger.info( "started Echo Nest API manager thread" );
		}
		catch ( ChoicetraxException cte )
		{
			logger.error( "Exception initializing ResourceManager", cte );
		}
	}
	
	/**
	 * closes ResourceManager resources
	 */
	public void destroy()
	{
		super.destroy();
		
		JAXBHelper.shutdown();
		
		try
		{
			logger.info( "destroying CommunicationServiceImpl servlet, "
						+ "shutting down ResourceManager resources..." );
			
			ResourceManager.closeResources();
			
			logger.info( "ResourceManager resources closed \r\n" );
			
			initialDataCacheChecker.stopChecker();
			
			logger.info( "stopped initial data cache checker thread" );
			
			trackingCacheChecker.stopChecker();
			
			logger.info( "stopped tracking cache checker thread" );
			
			// initialize Echo Nest API manager
			EchoNestApiManager.getInstance().stopManager();
			
			logger.info( "stopped Echo Nest API manager thread" );
		}
		catch( ChoicetraxException cte )
		{
			logger.error( "Exception closing ResourceManager", cte );
		}
	}
	
	
	
	protected void doUnexpectedFailure(Throwable e) 
	{
		logger.error( "CommunicationServiceImpl.doUnexpectedFailure(), "
						+ "client IP [" + getClientIPAddress() + "], "
						+ "msg [" + e.getMessage() + "]", 
					e );
		
		super.doUnexpectedFailure( e );
	}

	
	public LoaderResponse loadData( LoaderAction action )
		throws ChoicetraxException
	{
		try
		{
			DataLoader loader = null;
			
			// if userID not set, get IP address for logging
			if ( action instanceof UserIpAction )
				setIpIfNeeded( (UserIpAction) action );
			
			if ( action instanceof LoadInitialDataAction )
			{
				loader = new ChoicetraxInitialDataLoader();
			}
			else if ( action instanceof RequestBpmAction )
			{
				loader = new BPMLoader();
			}
			else if ( action instanceof ReleasesSearchQuickAction ) 
			{
				loader = new SearchReleasesQuickLoader();
			}
			else if ( action instanceof ReleasesSearchAdvancedAction )
			{
				loader = new SearchReleasesAdvancedLoader();
			}
			else if ( action instanceof ReleasesBrowseAction )
			{
				loader = new BrowseReleasesLoader();
			}
			else if ( action instanceof DJChartsSearchAction )
			{
				loader = new DJChartsSearchLoader();
			}
			else if ( action instanceof LoadReleasesAction )
			{
				loader = new RetrieveReleasesLoader();
			}
			else if ( action instanceof UserAction )
			{
				loader = new UserLoader();
			}
			else if ( action instanceof LoadFormatsAndPricesAction )
			{
				loader = new FormatsAndPricesLoader();
			}
			else if ( action instanceof AlbumEPSearchAction )
			{
				loader = new AlbumEPReleasesLoader();
			}
			else if ( action instanceof LoadUserPurchaseHistoryAction )
			{
				loader = new UserPurchaseHistoryLoader();
			}
			else if ( action instanceof SearchTrackFavoritesAction )
			{
				loader = new SearchTrackFavoritesLoader();
			}
			else if ( action instanceof SearchDJChartsFavoritesAction ) 
			{
				loader = new SearchDJChartsFavoritesLoader();
			}
			else if ( action instanceof ViewDJChartAction )
			{
				loader = new DJChartTracksLoader();
			}
			else if ( action instanceof LoadDJChartsAction ) 
			{
				loader = new DJChartsLoader();
			}
			else if ( action instanceof LoadTrackIDAction )
			{
				loader = new TrackIDLoader();
			}
			else if ( action instanceof PasswordResetAction )
			{
				loader = new PasswordResetUserLoader();
			}
			else if ( action instanceof RecommendedTracksAction )
			{
				loader = new RecommendedTracksLoader();
			}
			else if ( action instanceof RecommendedDJChartsAction )
			{
				loader = new RecommendedDJChartsLoader();
			}
			
			return loader.loadData( action );
		}
		catch ( Exception e ) {
			throw handleException( e, "loadData()" );
		}
	}
	
	
	public ActionResponse handleAction( HandlerAction action )
		throws ChoicetraxException
	{
		try
		{
			ActionHandler handler = null;
			
			// if userID not set, get IP address for logging
			if ( action instanceof UserIpAction )
				setIpIfNeeded( (UserIpAction) action );
			
			if ( action instanceof AdImpressionAction ) 
			{				
				handler = new AdImpressionHandler( action );
			}
			else if ( action instanceof FavoritesAction )
			{
				handler = new FavoritesActionHandler( action );
			}
			else if ( action instanceof FavoritesSearchAction )
			{
				handler = new FavoritesSearchActionHandler( action );
			}
			else if ( action instanceof WishlistAction )
			{
				handler = new WishlistActionHandler( action );
			}
			else if ( action instanceof VirtualCartAction )
			{
				handler = new VirtualCartActionHandler( action );
			}
			else if ( action instanceof PurchaseHistoryAction )
			{
				handler = new PurchaseHistoryActionHandler( action );
			}
			else if ( action instanceof RatingAction )
			{
				handler = new RatingActionHandler( action );
			}
			else if ( action instanceof AdClickAction )
			{
				handler = new AdClickActionHandler( action );
			}
			else if ( action instanceof BetaUserLoginAction ) 
			{
				handler = new BetaUserLoginActionHandler( action );
			}
			else if ( action instanceof ClientLoggerAction )
			{
				// get IP address for logging
				((ClientLoggerAction) action).setIpAddress( getClientIPAddress() );
				
				handler = new ClientLoggerActionHandler( action );
			}
			else if ( action instanceof PasswordResetRequestAction )
			{
				handler = new PasswordResetRequestActionHandler( action );
			}
			else if ( action instanceof PartnerTransferAction )
			{
				handler = new TrackingTransferDataHandler( action );
			}
			else if ( action instanceof RecommendationAction )
			{
				handler = new RecommendationActionHandler( action );
			}
			else if ( action instanceof FeedbackAction )
			{
				handler = new FeedbackActionHandler( action );
			}
			
			return handler.handleAction();
		}
		catch ( Exception e ) {
			throw handleException( e, "handleAction()" );
		}
	}
	
	
	private ChoicetraxException handleException( Exception e, String methodName ) 
	{
		ChoicetraxException exception = null;
		
		if ( e instanceof FailedAuthenticationException ) {
			exception = (FailedAuthenticationException) e;
		}
		else if ( e instanceof ChoicetraxException )
		{
			logger.error( "CommunicationServiceImpl." + methodName + " ChoicetraxException", e );
			
			if ( e instanceof ChoicetraxDatabaseException )
				exception = new ChoicetraxException( DATABASE_EXCEPTION_TEXT );
			else if ( e instanceof ChoicetraxSphinxException )
				exception = new ChoicetraxException( SPHINX_EXCEPTION_TEXT );
			else if ( e instanceof ChoicetraxEmailException )
				exception = new ChoicetraxException( EMAIL_EXCEPTION_TEXT );
			else
				exception = (ChoicetraxException) e;
		}
		else
		{
			logger.error( "CommunicationServiceImpl." + methodName + " Exception", e );
			
			exception = new ChoicetraxException( UNHANDLED_EXCEPTION_TEXT );
		}
		
		return exception;
	}
	
	
	private String getClientIPAddress() {
		return getThreadLocalRequest().getRemoteAddr();
	}
	
	
	private void setIpIfNeeded( UserIpAction action )
	{
		if ( action.getUserID() <= 0 )
			action.setIpAddress( getClientIPAddress() );
	}
	
}
