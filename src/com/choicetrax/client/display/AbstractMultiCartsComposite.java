package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

import java.util.HashMap;
import java.util.Iterator;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.data.VirtualCarts;
import com.choicetrax.client.data.VirtualCart;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.data.config.ChoicetraxConfigData;


public abstract class AbstractMultiCartsComposite 
	extends Composite
	implements ChoicetraxViewComponent
{
	
	private ChoicetraxViewManager viewManager = null;
	private VerticalPanel vp = null;
	private TabPanel cartsTabPanel = null;
	
	private VirtualCarts virtualCarts = null;
	private HashMap<Integer,String> cartNames = new HashMap<Integer,String>();
			
	
	
	public AbstractMultiCartsComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		vp = new VerticalPanel();
		initWidget( vp );
		
		cartsTabPanel = new TabPanel();
		cartsTabPanel.getDeckPanel().setAnimationEnabled( true );
		cartsTabPanel.setStylePrimaryName( "multiCartsTabPanel" );
		cartsTabPanel.addTabListener( new CartSelectListener() );
		
		Label headerLabel = createHeaderLabel();
		headerLabel.setStyleName( "mainPanelHeaderLabel" );
		vp.add( headerLabel );
		vp.setCellHorizontalAlignment( headerLabel, HasHorizontalAlignment.ALIGN_CENTER );
		
		vp.add( cartsTabPanel );
	}
	
	
	protected abstract Label createHeaderLabel();
	
	
	protected abstract AbstractReleasesComposite createMultiCartReleasesComposite( ChoicetraxViewManager manager, int partnerID );

	
	protected abstract void emptyCartsAlert();

	

	
	public void updateDisplay( LoaderResponse response )
	{
		if ( response instanceof VirtualCarts )
			initDisplay( (VirtualCarts) response );
		else if ( response instanceof VirtualCart )
			refreshCart( (VirtualCart) response );
		else if ( response instanceof ReleasesCache )
			updateCartDisplay( (ReleasesCache) response );
		else
			Window.alert( "AbstractMultiCartsComposite.updateDisplay() "
						 + "- unknown response type: " + response );
	}
	
	public void clearDisplay() 
	{ 
		cartsTabPanel.clear();
	}
	
	
	
	public void setWaitingState( boolean waiting ) { }
	
	
	
	
	private void initDisplay( VirtualCarts carts )
	{
		virtualCarts = carts;
		cartNames.clear();
		
		clearDisplay();
		
		int selectedTabIndex = cartsTabPanel.getTabBar().getSelectedTab();
		String currentCartName = virtualCarts.getCurrentCartName();
		
		ChoicetraxConfigData configData = viewManager.getConfigData();
		
		Iterator<VirtualCart> i = carts.getCarts().values().iterator();
		for ( int tabIndex = 0; i.hasNext(); tabIndex++ )
		{
			VirtualCart cart = i.next();
			String partnerName = cart.getPartnerName();
			String tabName = partnerName + " (" + cart.size() + ")";
			int partnerID = configData.lookupPartnerID( partnerName );
			
			// select correct tab when changing pages among multiple carts
			if ( ( currentCartName != null ) && ( currentCartName.equals( partnerName ) ) )
				selectedTabIndex = tabIndex;
			
			AbstractReleasesComposite releases 
				= createMultiCartReleasesComposite( viewManager, partnerID );
			
			releases.updateDisplay( cart.getReleasesCache() );
			
			cartsTabPanel.add( releases, tabName );
			
			cartNames.put( new Integer( tabIndex ), partnerName );
		}
		
		if ( cartsTabPanel.getTabBar().getTabCount() > 0 )
		{
			if ( selectedTabIndex < 0 ) selectedTabIndex = 0;
			cartsTabPanel.selectTab( selectedTabIndex );
		}
		else
			emptyCartsAlert();
	}
	
		
	
	private void refreshCart( VirtualCart cart )
	{
		int selectedTabIndex = cartsTabPanel.getTabBar().getSelectedTab();
		
		ReleasesCache cache = cart.getReleasesCache();
		
		if ( cache.size() > 0 )
		{
			VirtualCartReleasesComposite releases = 
				(VirtualCartReleasesComposite) cartsTabPanel.getWidget( selectedTabIndex );
			
			releases.updateDisplay( cache );
			
			cartsTabPanel.remove( selectedTabIndex );
			
			String tabName = cart.getPartnerName() + " (" + cart.size() + ")";
			cartsTabPanel.insert( releases, tabName, selectedTabIndex );
			
			cartsTabPanel.selectTab( selectedTabIndex );
		}
		else 
		{
			cartsTabPanel.remove( selectedTabIndex );
			
			if ( cartsTabPanel.getTabBar().getTabCount() > 0 )
				cartsTabPanel.selectTab( 0 );
		}
	}
	
	
	private void updateCartDisplay( ReleasesCache cache )
	{
		int selectedTabIndex = cartsTabPanel.getTabBar().getSelectedTab();
		
		VirtualCartReleasesComposite releases = 
			(VirtualCartReleasesComposite) cartsTabPanel.getWidget( selectedTabIndex );
		
		releases.updateDisplay( cache );
	}
	
	
	
	private class CartSelectListener implements TabListener
	{
		public boolean onBeforeTabSelected( SourcesTabEvents sender, int tabIndex ) {
			return true;
		}
		
		public void onTabSelected( SourcesTabEvents sender, int tabIndex ) { 
			String partnerName = cartNames.get( new Integer( tabIndex ) );
			virtualCarts.setCurrentCart( partnerName );
		}
	}

}
