package com.choicetrax.client.data;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.choicetrax.client.actions.LoaderHistoryAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.cache.ReleasesCache;


/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class User implements LoaderResponse
{
    private int userID;
    private String userName;
    private String userPass;
    private String userFirstName;
    private String userLastName;
    private String emailAddress;
    private String city;
    private String state;
    private String zipCode;
    private String countryCode;
    private int countryID;
    private boolean receiveEmail;
    private boolean emailOnlyShowFavoriteGenres;
    private String emailFrequency;
    private String favoriteCurrency;
    private String sessionID;
    
    // arrays of favorites objects 
    private FavoritesList favoriteArtists = new FavoritesList();
	private FavoritesList favoriteLabels = new FavoritesList();
	private HashMap<String,LoaderHistoryAction> favoriteSearches = new HashMap<String,LoaderHistoryAction>();
	
	// arrays of wishlist & purchase history Releases
	private ReleasesCache wishlist = new ReleasesCache( Constants.CACHE_RELEASES_WISHLIST );
	private VirtualCarts purchaseHistory = null;
	private VirtualCarts virtualCarts = null;
	
	
	public User() {
		super();
	}
		
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append( "UserID [" + userID + "] "
					+ "UserName [" + userName + "] " );
		
		if ( userFirstName != null )
			sb.append( "first name [" + userFirstName + "] " );
		if ( userLastName != null )
			sb.append( "last name [" + userLastName + "] " );
		if ( emailAddress != null )
			sb.append( "email [" + emailAddress + "] " );
		if ( city != null )
			sb.append( "city [" + city + "] " );
		if ( state != null )
			sb.append( "state [" + state + "] " );
		if ( zipCode != null )
			sb.append( "zip [" + zipCode + "] " );
		if ( countryCode != null )
			sb.append( "countryCode [" + countryCode + "] " );
		if ( countryID > 0 )
			sb.append( "countryID [" + countryID + "] " );
		
		return sb.toString();
	}
	
	
	
	public boolean addFavorite( TrackComponent tc ) {
		return addFavorite( tc, false );
		
		// turned sorting off for now - may be running into performance problems
		// doesn't seem to be working correctly anyway
	}
	
	public boolean addFavorite( TrackComponent tc, boolean sort )
	{
		if ( ! isFavorite( tc ) )
		{
			if ( tc instanceof Artist )
			{
				favoriteArtists.add( tc );
				if ( sort ) sortList( favoriteArtists, 0, favoriteArtists.size() - 1 );
			}
			else if ( tc instanceof RecordLabel )
			{
				favoriteLabels.add( tc );
				if ( sort ) sortList( favoriteLabels, 0, favoriteLabels.size() - 1 );
			}
			
			return true;
		}
		else
			return false;
	}
	
	
	private void sortList( List<TrackComponent> list, int leftBound, int rightBound )
	{
		if ( list.size() < 2 ) return;
		if ( leftBound >= rightBound ) return;
		
		int leftIndex = leftBound;
		int rightIndex = rightBound - 1;
		
		String pivotName = ((TrackComponent) list.get( rightBound )).getName();
		String leftPivotName = ((TrackComponent) list.get( leftIndex )).getName();
		String rightPivotName = ((TrackComponent) list.get( rightIndex )).getName();
		
		while ( leftIndex <= rightIndex ) 
		{
			while ( ( leftIndex <= rightIndex )
					&& pivotName.compareTo( leftPivotName ) >= 0 )
				leftIndex++;
			
			while ( ( rightIndex >= leftIndex )
					&& pivotName.compareTo( rightPivotName ) <= 0 )
				rightIndex--;
			
			if ( leftIndex < rightIndex )
				swap( list, leftIndex, rightIndex );
		}
		
		swap( list, leftIndex, rightBound );
		
		sortList( list, leftBound, leftIndex - 1 );
		sortList( list, leftIndex + 1, rightBound );
	}
	
	
	private void swap( List<TrackComponent> list, int i, int j )
	{
		TrackComponent temp = list.get( i );
		list.set( i, list.get( j ) );
		list.set( j, temp );
	}
	
	
	public void removeFavorite( TrackComponent tc )
	{
		if ( tc instanceof Artist )
			favoriteArtists.remove( tc );
		else if ( tc instanceof RecordLabel )
			favoriteLabels.remove( tc );
	}
	
	
	public void addFavoriteSearch( String searchName, LoaderHistoryAction actionObj )
	{
		favoriteSearches.put( searchName, actionObj );
	}
	
	
	public void removeFavoriteSearch( String searchName )
	{
		favoriteSearches.remove( searchName );
	}
	
	
	public boolean addWishlist( ReleaseDetail rd )
	{
		if ( ! isWishlist( rd ) )
		{
			wishlist.addReleaseDetail( rd );
			return true;
		}
		else return false;
	}
	
	public boolean removeWishlist( ReleaseDetail rd )
	{
		if ( isWishlist( rd ) )
		{
			wishlist.removeReleaseDetail( rd );
			return true;
		}
		else return false;
	}
	
	/*public boolean addPurchaseHistory( ReleaseDetail rd )
	{
		if ( ! isPurchaseHistory( rd ) )
		{
			purchaseHistory.addReleaseDetail( rd );
			return true;
		}
		else return false;
	}*/
	
	public void removePurchaseHistory( String partnerName, ReleaseDetail rd )
	{
		purchaseHistory.removeFromCart( partnerName, rd );
	}
	
	
	
	public boolean isFavorite( TrackComponent tc )
	{
		if ( tc instanceof Artist ) 
			return favoriteArtists.containsTrackID( tc.getID() );
		else if ( tc instanceof RecordLabel )
			return favoriteLabels.containsTrackID( tc.getID() );
		else return false;
	}
	
	public boolean isWishlist( ReleaseDetail rd )
	{
		return wishlist.containsTrackID( rd.getTrackID() );
	}
	
	/*public boolean isPurchaseHistory( ReleaseDetail rd )
	{
		purchaseHistory.
		return purchaseHistory.containsTrackID( rd.getTrackID() );
	}*/
	
	public FavoritesList getFavoriteArtists() {
		return favoriteArtists;
	}
	
	public FavoritesList getFavoriteLabels() {
		return favoriteLabels;
	}
	
	public Set<String> getFavoriteSearchNames() {
		return favoriteSearches.keySet();
	}
	
	public LoaderHistoryAction getFavoriteSearch( String searchName )
	{
		return favoriteSearches.get( searchName );
	}
	
	public ReleasesCache getWishlist() {
		return wishlist;
	}
	
	public void setWishlist( ReleasesCache cache ) {
		this.wishlist = cache;
	}
	
	public VirtualCarts getPurchaseHistory() {
		return purchaseHistory;
	}
	
	public void setPurchaseHistory( VirtualCarts carts ) {
		this.purchaseHistory = carts;
	}
	
	public VirtualCarts getVirtualCarts() {
		return virtualCarts;
	}
	
	public void setVirtualCarts( VirtualCarts carts ) {
		this.virtualCarts = carts;
	}
    
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public int getCountryID() {
		return countryID;
	}
	public void setCountryID( int countryID ) {
		this.countryID = countryID;
	}
	public boolean isReceiveEmail() {
		return receiveEmail;
	}
	public void setReceiveEmail(boolean receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
	public boolean isEmailOnlyShowFavoriteGenres() {
		return emailOnlyShowFavoriteGenres;
	}
	public void setEmailOnlyShowFavoriteGenres(boolean emailOnlyShowFavoriteGenres) {
		this.emailOnlyShowFavoriteGenres = emailOnlyShowFavoriteGenres;
	}
	public String getEmailFrequency() {
		return emailFrequency;
	}
	public void setEmailFrequency(String emailFrequency) {
		this.emailFrequency = emailFrequency;
	}
	public String getFavoriteCurrency() {
		return favoriteCurrency;
	}
	public void setFavoriteCurrency(String favoriteCurrency) {
		this.favoriteCurrency = favoriteCurrency;
	}

}