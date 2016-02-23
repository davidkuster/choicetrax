package com.choicetrax.client.display.panels;

import java.util.HashMap;

import com.google.gwt.user.client.ui.*;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.display.*;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class ChoicetraxCenterPanel extends Composite 
{
	private ChoicetraxViewManager viewManager = null;
	
	private DeckPanel deckPanel = null;
	
	private NewsComposite newsComposite 							= null;
	private ReleasesComposite releasesComposite 					= null;
	private AccountConfigComposite accountConfigComposite 			= null;
	private WishlistReleasesComposite wishlistComposite 			= null;
	private PurchaseHistoryCartsComposite purchaseHistoryComposite	= null;
	private VirtualCartsComposite virtualCartsComposite				= null;
	private DJChartSelectComposite djChartSelectComposite			= null;
	private DJChartReleasesComposite djChartReleasesComposite		= null;
	private RecommendedReleasesComposite recommendedTracksComposite	= null;
	private RecommendedDJChartsComposite recommendedChartsComposite	= null;
	
	private int currentPanel = -1;

	
	private HashMap<Integer,Integer> deckIndexHash = new HashMap<Integer,Integer>();
	private int deckIndex = 0;
	
	
	public ChoicetraxCenterPanel( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		deckPanel = new DeckPanel();
		initWidget( deckPanel );
		deckPanel.setAnimationEnabled( true );
		
		showPanel( Constants.PANEL_NEWS );
	}
	
	
	/*
	 * This method tells the main/center panel to display a
	 * specific composite, as indicated by the passed parameter.
	 * (ex. ChoicetraxCenterPanel.NEWS, ChoicetraxCenterPanel.RELEASES,
	 * etc.)  
	 */
	public void showPanel( int panel )
	{
		Integer i = (Integer) deckIndexHash.get( new Integer( panel ) );
		if ( i != null )
			deckPanel.showWidget( i.intValue() );
		else
		{
			Widget w = null;
			
			if ( panel == Constants.PANEL_NEWS )
				w = getNewsComposite();
			else if ( panel == Constants.PANEL_RELEASES )
				w = getReleasesComposite();
			else if ( panel == Constants.PANEL_ACCOUNT_CONFIG )
				w = getAccountConfigComposite();
			else if ( panel == Constants.PANEL_WISHLIST )
				w = getWishlistComposite();
			else if ( panel == Constants.PANEL_PURCHASE_HISTORY )
				w = getPurchaseHistoryComposite();
			else if ( panel == Constants.PANEL_VIRTUAL_CARTS )
				w = getVirtualCartsComposite();
			else if ( panel == Constants.PANEL_DJ_CHARTS_SELECT )
				w = getDJChartSelectComposite();
			else if ( panel == Constants.PANEL_DJ_CHARTS_VIEW )
				w = getDJChartReleasesComposite();
			else if ( panel == Constants.PANEL_RECOMMENDED_TRACKS )
				w = getRecommendedTracksComposite();
			else if ( panel == Constants.PANEL_RECOMMENDED_CHARTS )
				w = getRecommendedDJChartsComposite();
			else 
				w = new Label( "Unknown panel type" );
			
			deckPanel.add( w );
			deckPanel.showWidget( deckIndex );
			deckIndexHash.put( new Integer( panel ), new Integer( deckIndex ) );
			deckIndex++;
		}
		
		currentPanel = panel;
	}
	
	
	public int getCurrentPanel() {
		return currentPanel;
	}
	
	
	public NewsComposite getNewsComposite()
	{
		if ( newsComposite == null )
			newsComposite = new NewsComposite( viewManager );
		
		return newsComposite;
	}
	
	public ReleasesComposite getReleasesComposite()
	{
		if ( releasesComposite == null )
			releasesComposite = new ReleasesComposite( viewManager );
		
		return releasesComposite;
	}
	
	public AccountConfigComposite getAccountConfigComposite()
	{
		if ( accountConfigComposite == null )
			accountConfigComposite = new AccountConfigComposite( viewManager );
		
		return accountConfigComposite;
	}
	
	public VirtualCartsComposite getVirtualCartsComposite()
	{
		if ( virtualCartsComposite == null )
			virtualCartsComposite = new VirtualCartsComposite( viewManager );
		
		return virtualCartsComposite;
	}
	
	public WishlistReleasesComposite getWishlistComposite()
	{
		if ( wishlistComposite == null )
			wishlistComposite = new WishlistReleasesComposite( viewManager );
		
		return wishlistComposite;
	}
	
	public PurchaseHistoryCartsComposite getPurchaseHistoryComposite()
	{
		if ( purchaseHistoryComposite == null )
			purchaseHistoryComposite = new PurchaseHistoryCartsComposite( viewManager );
		
		return purchaseHistoryComposite;
	}
	
	public DJChartSelectComposite getDJChartSelectComposite() 
	{
		if ( djChartSelectComposite == null )
			djChartSelectComposite	= new DJChartSelectComposite( viewManager );
		
		return djChartSelectComposite;
	}
	
	public DJChartReleasesComposite getDJChartReleasesComposite()
	{
		if ( djChartReleasesComposite == null )
			djChartReleasesComposite = new DJChartReleasesComposite( viewManager );
		
		return djChartReleasesComposite;
	}
	
	public RecommendedReleasesComposite getRecommendedTracksComposite() 
	{
		if ( recommendedTracksComposite == null )
			recommendedTracksComposite = new RecommendedReleasesComposite( viewManager );
		
		return recommendedTracksComposite;
	}
	
	public RecommendedDJChartsComposite getRecommendedDJChartsComposite() 
	{
		if ( recommendedChartsComposite == null )
			recommendedChartsComposite = new RecommendedDJChartsComposite( viewManager );
		
		return recommendedChartsComposite;
	}
		
	
}
