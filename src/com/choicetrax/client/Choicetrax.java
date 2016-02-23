package com.choicetrax.client;

import java.util.Date;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.display.ChoicetraxView;
//import com.choicetrax.client.display.LoadingComposite;
import com.choicetrax.client.logic.ChoicetraxViewManager;



/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Choicetrax implements EntryPoint 
{

	private static ChoicetraxView view = null;
	
	
	/**
	 *This is the entry point method.
	 */
	public void onModuleLoad() 
	{
		HorizontalPanel hp = new HorizontalPanel();
		hp.setWidth( "100%" );
		hp.setSpacing( 5 );
		hp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		hp.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
				
		hp.add( new Image( "img/loading5.gif" ) );
		hp.add( new Label( "Loading Choicetrax partner store and genre data... " ) );
		
		RootPanel.get( "results" ).add( hp );
		
		
		ChoicetraxViewManager viewManager = new ChoicetraxViewManager();
		view = viewManager.getView();
		
		//new PrivateLogin( viewManager );		
		//initialize();
	}
	
	
	
	public static void initialize() 
	{			
		RootPanel.get( "results" ).clear();
		
		RootPanel.get( "headerRight" ).add( view.getAdBannerPanel() );
		RootPanel.get( "mainHeader" ).add( view.getResizeButton() );
		RootPanel.get( "login" ).add( view.getAccountComposite() );
		RootPanel.get( "search" ).add( view.getSearchComposite() );
		RootPanel.get( "futureReleases" ).add( view.getFeaturedReleasesComposite() );
		RootPanel.get( "bannerLeft" ).add( view.getAdLeftPanel() );
		RootPanel.get( "playerTrackInfo" ).add( view.getAudioPlayerComposite() );
		RootPanel.get( "results" ).add( view.getCenterRightPanel() );
		RootPanel.get( "subFooter" ).add( view.getAboutUsComposite() );
	}
	
	
	
	
	public static void setCookie( String cookieName, String value )
	{
		//final long DURATION = 1000 * 60 * 60 * 24; // * 14; 
		//duration remembering login. 2 weeks in this example.
		
		setCookie( cookieName, value, Constants.COOKIE_DURATION_DEFAULT );
	}
	
	
	public static void setCookie( String cookieName, String value, long duration )
	{
		if ( value != null )
		{
			Date expires = new Date(System.currentTimeMillis() + duration );
			Cookies.setCookie( cookieName, value, expires );
		}
		else
		{
			Cookies.removeCookie( cookieName );
		}
	}
	
	
	public static String getCookie( String cookieName )
	{
		String value = null;
		
		if ( cookieName != null )
			value = Cookies.getCookie( cookieName );
		
		return value;
	}
				
}
