package com.choicetrax.client.logic;

import com.google.gwt.user.client.Window;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Command;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.handleractions.FavoritesAction;
import com.choicetrax.client.actions.handleractions.FavoritesSearchAction;
import com.choicetrax.client.actions.handleractions.PurchaseHistoryAction;
import com.choicetrax.client.actions.handleractions.RatingAction;
import com.choicetrax.client.actions.handleractions.RecommendationAction;
import com.choicetrax.client.actions.handleractions.VirtualCartAction;
import com.choicetrax.client.actions.handleractions.WishlistAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Artist;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.RecordLabel;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.TrackComponent;
import com.choicetrax.client.data.User;
import com.choicetrax.client.data.VirtualCart;
import com.choicetrax.client.data.VirtualCarts;
import com.choicetrax.client.data.cache.Cache;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.logic.callbacks.ActionHandlerController;


public class ChoicetraxUserManager 
{
	
	private ChoicetraxViewManager viewManager = null;
		
	// user object
	private User currentUser = null;
	
	// show audio player the first time audio is listened to
	private boolean firstAudioPlayed = false;
	
	// arraylist to keep track of what's been listened to this session
	private ArrayList<String> listenedAudioClips 	= new ArrayList<String>();
	
	private ArrayList<String> wishlistTracks		= new ArrayList<String>();
	private ArrayList<String> virtualCartTracks		= new ArrayList<String>();
	
	
	public ChoicetraxUserManager( ChoicetraxViewManager manager ) {
		this.viewManager = manager;
	}
	
	
	public void initialize() {
		populateArrayLists();
	}
	
	
	
	public void setCurrentUser( User user ) {
		this.currentUser	= user;
		
		//listenedAudioClips.clear();
		wishlistTracks.clear();
		virtualCartTracks.clear();
		
		DeferredCommand.addCommand( new Command() {
			public void execute() {
				populateArrayLists();
			}
		});
	}
	
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	
	private void populateArrayLists()
	{
		if ( ( currentUser != null ) 
			&& ( viewManager.getConfigData() != null ) ) 
		{
			// populate wishlistTracks ArrayList
			wishlistTracks = currentUser.getWishlist().getTrackIDsList();			
					
			// populate virtualCartTracks ArrayList
			VirtualCarts carts = currentUser.getVirtualCarts();
			if ( carts != null ) 
			{
				Iterator<VirtualCart> ci = carts.getCarts().values().iterator();
				while ( ci.hasNext() ) 
				{
					VirtualCart cart = ci.next();
					int partnerID = viewManager.getConfigData().lookupPartnerID( cart.getPartnerName() );
					
					ArrayList<String> trackIDs = cart.getReleasesCache().getTrackIDsList();
					Iterator<String> vi = trackIDs.iterator();
					while ( vi.hasNext() ) {
						int trackID = Integer.parseInt( vi.next() );
						virtualCartTracks.add( createKey( trackID, partnerID ) );
					}
				}
			}
		}
	}
	
	
	
	public void addFavoriteSearch( String searchName, LoaderHistoryAction loaderAction )
	{
		currentUser.addFavoriteSearch( searchName, loaderAction );
		viewManager.updateDisplay( Constants.PANEL_FAVORITES, currentUser );
		
		viewManager.getView().getFavoritesComposite().selectTab( Constants.FAVORITES_TAB_SEARCHES );
		
		loaderAction.setUserID( currentUser.getUserID() );
		
		FavoritesSearchAction action = new FavoritesSearchAction();
		action.setActionObj( loaderAction );
		action.setSearchName( searchName );
		action.setUserID( currentUser.getUserID() );
		action.setActionType( Constants.ACTION_ADD );
		
		//viewManager.executeAction( action, new ActionHandlerController( viewManager ) );
		viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
	}
	
	
	public void removeFavoriteSearch( String searchName )
	{
		currentUser.removeFavoriteSearch( searchName );
		viewManager.updateDisplay( Constants.PANEL_FAVORITES, currentUser );
		
		FavoritesSearchAction action = new FavoritesSearchAction();
		action.setActionType( Constants.ACTION_REMOVE );
		action.setSearchName( searchName );
		action.setUserID( currentUser.getUserID() );
		
		//viewManager.executeAction( action, new ActionHandlerController( viewManager ) );
		viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
	}
		
	
	public void addFavorite( TrackComponent tc )
	{
		if ( currentUser == null )
			Window.alert( "Please log in or create an account to add to Favorites." );
		else
		{
			if ( currentUser.addFavorite( tc ) )
			{
				viewManager.updateDisplay( Constants.PANEL_FAVORITES, currentUser );
				
				if ( tc instanceof Artist )
					viewManager.getView().getFavoritesComposite().selectTab( Constants.FAVORITES_TAB_ARTISTS );
				else if ( tc instanceof RecordLabel )
					viewManager.getView().getFavoritesComposite().selectTab( Constants.FAVORITES_TAB_LABELS );
				
				/*
				 * should i immediately send this new favorite to
				 * the server or batch them together?
				 */
				FavoritesAction action = new FavoritesAction();
				action.setActionType( Constants.ACTION_ADD );
				action.setUserID( currentUser.getUserID() );
				action.setFavorite( tc );
				
				viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
			}
		}
	}
	

	public void removeFavorite( TrackComponent tc )
	{
		currentUser.removeFavorite( tc );
		viewManager.updateDisplay( Constants.PANEL_FAVORITES, currentUser );
		
		FavoritesAction action = new FavoritesAction();
		action.setActionType( Constants.ACTION_REMOVE );
		action.setFavorite( tc );
		action.setUserID( currentUser.getUserID() );
		
		viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
	}
	
	
	public void removeRecommendation( ReleaseDetail rd ) 
	{
		Cache cache = viewManager.getCurrentSearchCache();
		if ( cache.getCacheType() == Constants.CACHE_RELEASES_RECOMMENDED ) {
			((ReleasesCache) cache).removeReleaseDetail( rd );
			viewManager.updateDisplay( Constants.PANEL_RECOMMENDED_TRACKS, cache );
		}
				
		RecommendationAction action = new RecommendationAction();
		action.setActionType( Constants.ACTION_REMOVE );
		action.setUserID( currentUser.getUserID() );
		action.setTrackID( rd.getTrackID() );
		
		viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
	}
	
	
	public boolean addWishlist( ReleaseDetail rd )
	{
		if ( currentUser == null )
			Window.alert( "Please log in or create an account to add to Wishlist." );
			
		else
		{
			if ( currentUser.addWishlist( rd ) )
			{
				wishlistTracks.add( rd.getTrackID() + "" );
				
				// update account display
				viewManager.updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, currentUser );
				
				// if user is currently looking at wishlist, refresh it
				if ( viewManager.getView().getCenterPanel().getCurrentPanel() == Constants.PANEL_WISHLIST )
					viewManager.updateDisplay( Constants.PANEL_WISHLIST, currentUser.getWishlist() );
				
				WishlistAction action = new WishlistAction();
				action.setActionType( Constants.ACTION_ADD );
				action.setUserID( currentUser.getUserID() );
				action.setTrackID( rd.getTrackID() );
				
				viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
				
				return true;
			}
		}
		
		return false;
	}
	
	
	public void addRating( ReleaseDetail rd, int rating )
	{
		if ( currentUser == null )
			Window.alert( "Please log in or create an account to rate trax." );
		else
		{
			rd.setUserRating( rating );
			
			RatingAction action = new RatingAction();
			action.setRating( rating );
			action.setTrackID( rd.getTrackID() );
			action.setUserID( currentUser.getUserID() );
			action.setActionType( Constants.ACTION_ADD );
			
			viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
		}
	}
	
	
	public void removeWishlist( ReleaseDetail rd )
	{
		wishlistTracks.remove( rd.getTrackID() + "" );
		
		currentUser.removeWishlist( rd );
		viewManager.updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, currentUser );
		viewManager.updateDisplay( Constants.PANEL_WISHLIST, currentUser.getWishlist() );
		
		WishlistAction action = new WishlistAction();
		action.setActionType( Constants.ACTION_REMOVE );
		action.setUserID( currentUser.getUserID() );
		action.setTrackID( rd.getTrackID() );
		
		viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
	}
	
	
	public void removePurchaseHistory( ReleaseDetail rd, int partnerID )
	{
		String partnerName = viewManager.getConfigData().lookupPartnerName( partnerID );
		
		currentUser.removePurchaseHistory( partnerName, rd );
		viewManager.updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, currentUser );
		viewManager.updateDisplay( Constants.PANEL_PURCHASE_HISTORY, currentUser.getPurchaseHistory() );
		
		VirtualCart cart = new VirtualCart( partnerName );
		cart.addRelease( rd, null );
		
		PurchaseHistoryAction action = new PurchaseHistoryAction();
		action.setActionType( Constants.ACTION_REMOVE );
		action.setUserID( currentUser.getUserID() );
		action.setPartnerID( partnerID );
		action.setVirtualCart( cart );
		
		viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
	}
	
	
	public void addToVirtualCart( ReleaseDetail rd, PartnerInventory pi, Format format )
	{
		User currentUser = viewManager.getCurrentUser();
		if ( currentUser != null )
		{
			String partnerName = viewManager.getConfigData().lookupPartnerName( pi.getPartnerID() );
			VirtualCart cart = currentUser.getVirtualCarts().getCart( partnerName );
			
			if ( cart.addRelease( rd, format ) )
			{
				virtualCartTracks.add( createKey( rd.getTrackID(), pi.getPartnerID() ) );
				
				viewManager.updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, currentUser );
				
				// if user is currently looking at virtual cart, refresh it
				if ( viewManager.getView().getCenterPanel().getCurrentPanel() == Constants.PANEL_VIRTUAL_CARTS )
					viewManager.updateDisplay( Constants.PANEL_VIRTUAL_CARTS, currentUser.getVirtualCarts() );				
				
				VirtualCart vc = new VirtualCart( partnerName );
				vc.addRelease( rd, format );
				
				VirtualCartAction action = new VirtualCartAction();
				action.setPartnerID( pi.getPartnerID() );
				action.setUserID( currentUser.getUserID() );
				action.setVirtualCart( vc );
				action.setActionType( Constants.ACTION_ADD );
				
				viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
			}
		}
		else
			Window.alert( "Please log in or create an account to use Virtual Carts." );
	}
	
	
	public void removeFromVirtualCart( ReleaseDetail rd, int partnerID )
	{
		User currentUser = viewManager.getCurrentUser();
		if ( currentUser != null )
		{
			String partnerName = viewManager.getConfigData().lookupPartnerName( partnerID );
			VirtualCarts carts = currentUser.getVirtualCarts();
			VirtualCart cart = carts.getCart( partnerName );
			
			Format format = cart.getSelectedFormat( rd.getTrackID() );
			
			if ( cart.removeRelease( rd ) )
			{
				virtualCartTracks.remove( createKey( rd.getTrackID(), partnerID ) );
				
				if ( cart.size() == 0 )
					carts.removeCart( partnerName );
					
				viewManager.updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, currentUser );
				viewManager.updateDisplay( Constants.PANEL_VIRTUAL_CARTS, cart );
				
				VirtualCart vc = new VirtualCart( partnerName );
				vc.addRelease( rd, format );
				
				VirtualCartAction action = new VirtualCartAction();
				action.setPartnerID( partnerID );
				action.setUserID( currentUser.getUserID() );
				action.setVirtualCart( vc );
				action.setActionType( Constants.ACTION_REMOVE );
				
				viewManager.deferAction( action, new ActionHandlerController( viewManager ) );
			}
		}
	}
	
	
	public void virtualCartCheckoutComplete( String partnerName ) 
	{
		User currentUser = viewManager.getCurrentUser();
		VirtualCart cart = currentUser.getVirtualCarts().getCart( partnerName );
		int userID = currentUser.getUserID();
		int partnerID = viewManager.getConfigData().lookupPartnerID( partnerName );
		
		// add trax to purchase history
		PurchaseHistoryAction phAction = new PurchaseHistoryAction();
		phAction.setActionType( Constants.ACTION_ADD );
		phAction.setPartnerID( partnerID );
		phAction.setUserID( userID );
		phAction.setVirtualCart( cart );
		
		viewManager.executeAction( phAction, new ActionHandlerController( viewManager ) );
		
		clearVirtualCart( partnerName );
	}
	
	
	
	public void clearVirtualCart( String partnerName )
	{
		User currentUser = viewManager.getCurrentUser();
		VirtualCart cart = currentUser.getVirtualCarts().getCart( partnerName );
		int userID = currentUser.getUserID();
		int partnerID = viewManager.getConfigData().lookupPartnerID( partnerName );
		
		// remove trax from virtual cart
		VirtualCartAction cartAction = new VirtualCartAction();
		cartAction.setActionType( Constants.ACTION_REMOVE );
		cartAction.setPartnerID( partnerID );
		cartAction.setUserID( userID );
		cartAction.setVirtualCart( cart );
		
		viewManager.executeAction( cartAction, new ActionHandlerController( viewManager ) );
		
		// update UI
		VirtualCarts carts = currentUser.getVirtualCarts();
		Iterator<ReleaseDetail> i = cart.getReleaseDetailIterator();
		while ( i.hasNext() ) 
		{
			ReleaseDetail rd = i.next();
			
			carts.removeFromCart( partnerName, rd );
			virtualCartTracks.remove( createKey( rd.getTrackID(), partnerID ) );
		}
				
		viewManager.updateDisplay( Constants.PANEL_ACCOUNT_DETAILS, currentUser );
		viewManager.updateDisplay( Constants.PANEL_VIRTUAL_CARTS, currentUser.getVirtualCarts() );
	}
	
	
	
	public void listenTrack( ReleaseDetail rd, int partnerID )
	{
		if ( ! firstAudioPlayed )
		{
			firstAudioPlayed = true;
			
			// only do this if user isn't logged in.  i'm assuming if they know enough
			// about the site to have created an account, they probably know how the
			// right panel works and don't need to be reminded everytime they log in.
			
			if ( viewManager.getCurrentUser() == null )
				viewManager.getView().showRightPanel( Constants.PANEL_AUDIO_PLAYER );
		}

		viewManager.getListenManager().addToPlaylist( rd, partnerID);
		
		listenedAudioClips.add( createKey( rd.getTrackID(), partnerID ) );
	}
	
	
	public boolean hasListened( int trackID, int partnerID ) {
		return listenedAudioClips.contains( createKey( trackID, partnerID ) );
	}
	
	
	public boolean isPurchased( int trackID, int partnerID ) {
		return virtualCartTracks.contains( createKey( trackID, partnerID ) );
	}
	
	
	public boolean isInWishlist( int trackID ) {
		return wishlistTracks.contains( trackID + "" );
	}
	
	
	public boolean isFavorite( TrackComponent tc )
	{
		if ( currentUser != null )
			return currentUser.isFavorite( tc );
		else 
			return false;
	}
	
	
	public boolean isRestricted( String territoryRestrictions )
	{
		boolean isRestricted = false;
		
		if ( ( territoryRestrictions == null ) || "".equals( territoryRestrictions ) )
			isRestricted = false;
		else if ( currentUser == null )
			isRestricted = true;
		else
		{
			// determine if this track is restricted for this user
			String countryID = currentUser.getCountryID() + "";
			if ( "0".equals( countryID ) ) 
				isRestricted = false;
			else if ( territoryRestrictions.indexOf( "," + countryID + "," ) != -1 )
				isRestricted = false;
			else if ( territoryRestrictions.equals( countryID ) )
				isRestricted = false;
			else if ( territoryRestrictions.startsWith( countryID + "," ) )
				isRestricted = false;
			else if ( territoryRestrictions.endsWith( "," + countryID ) )
				isRestricted = false;
			else 
				isRestricted = true;
		}
		
		return isRestricted;
	}
	
	
	private String createKey( int trackID, int partnerID ) {
		return trackID + "+" + partnerID;
	}

}
