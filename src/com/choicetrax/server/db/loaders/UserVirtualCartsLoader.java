package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.choicetrax.client.data.Releases;
import com.choicetrax.client.data.User;
import com.choicetrax.client.data.VirtualCart;
import com.choicetrax.client.data.VirtualCarts;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.cache.ReleasesCache;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.UserAction;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;


public class UserVirtualCartsLoader 
	extends BaseReleasesLoader
	implements DataLoader
{
	private User user = null;
	
	
	/**
	 * UserVirtualCartsLoader behaves differently that some of the
	 * other loaders (UserWishlistLoader, etc).  It will first
	 * pull the related data from the UserVirtualCarts DB table.
	 * For each virtual cart found it will dynamically determine
	 * the track IDs to be passed to the super class (BaseReleasesLoader)
	 * and load the first page of releases for each virtual cart. 
	 */
	public LoaderResponse loadData( LoaderAction loaderAction ) 
		throws ChoicetraxException
	{
		UserAction userAction = (UserAction) loaderAction;
		this.user = userAction.getUserObj();
		
		setUserID( user.getUserID() );
		
		// load data on all virtual carts in UserVirtualCarts table
		VirtualCarts carts = getVirtualCarts();
		
		// loop on virtual carts, load first page of releases for each
		Collection<VirtualCart> c = carts.getCarts().values();
		if ( c != null )
		{
			Iterator<VirtualCart> i = c.iterator();
			while ( i.hasNext() )
			{
				VirtualCart cart = i.next();
				ReleasesCache cache = cart.getReleasesCache();
				
				Releases firstPage = loadReleases( cache.getIDsForPage( 1 ) );
				cache.addReleases( firstPage, 1 );
			}
		}
		
		return carts;
	}
	
	
	private VirtualCarts getVirtualCarts()
		throws ChoicetraxException
	{
		String methodName = "getVirtualCarts()";
		
		VirtualCarts virtualCarts = new VirtualCarts();
		
		String sql = "select a.PartnerName, "
					+	"b.TrackID, b.PartnerFormatCode, "
					+	"pp.Format, pp.Bitrate, pp.Price "
					+ "from PartnerSites a, UserVirtualCarts b, "
					+	"PartnerInventory pi, PartnerPrices pp "
					+ "where b.UserID = " + this.userID + " "
					+	"and b.DateRemoved is null "
					+	"and b.PartnerID = a.PartnerID "
					+	"and b.PartnerID = pi.PartnerID "
					+	"and b.TrackID = pi.TrackID "
					+	"and pi.InventoryID = pp.InventoryID "
					+	"and b.PartnerFormatCode = pp.PartnerFormatCode "
					+ "order by a.PartnerOrdering, b.DateAdded desc, b.TrackID ";
		
		DBResource dbHandle = null;
    	
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		LinkedHashMap<String,LinkedHashMap<Integer,Format>> rowHash = 
    				new LinkedHashMap<String,LinkedHashMap<Integer,Format>>();
    		
    		ResultSet rs = dbHandle.executeQuery( sql );
    		while ( rs.next() )
    		{
    			String partnerName	= rs.getString( "PartnerName" 		);
    			String trackID		= rs.getString(	"TrackID"			);
    			String formatCode	= rs.getString( "PartnerFormatCode" );
    			String formatName	= rs.getString( "Format" 			);
    			String bitrate		= rs.getString( "Bitrate"			);
    			String price		= rs.getString( "Price"				);
    			
    			LinkedHashMap<Integer,Format> formatMap = rowHash.get( partnerName );
    			if ( formatMap == null ) formatMap = new LinkedHashMap<Integer,Format>();
    			
    			Format format = new Format( formatName, formatCode, bitrate, price );
    			formatMap.put( new Integer( trackID ), format );
    			
    			rowHash.put( partnerName, formatMap );
    		}
    		rs.close();
    		
    		
    		Iterator<String> k = rowHash.keySet().iterator();
    		while ( k.hasNext() )
    		{
    			String partnerName = k.next();
    			LinkedHashMap<Integer,Format> formatMap = rowHash.get( partnerName );
    			
    			int[] trackIDs = new int[ formatMap.size() ];
    			
    			Iterator<Integer> i = formatMap.keySet().iterator();
    			for ( int x=0; i.hasNext(); x++ )
    			{
    				Integer trackID = i.next();
    				trackIDs[ x ] = trackID.intValue();
    			}
    			
    			ReleasesCache cache = new ReleasesCache( 
    										trackIDs, 
    										Constants.CACHE_RELEASES_VIRTUAL_CART );
    			cache.setActionString( "(" + partnerName + ")" );
    			
    			VirtualCart cart = new VirtualCart( partnerName, cache );
    			cart.setSelectedFormats( formatMap );
    			virtualCarts.addCart( partnerName, cart );
    		}
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing virtual cart load: " + t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally 
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}
		
		return virtualCarts;
	}
	
	
	/*
	 * creates SQL to get track IDs.
	 */
	protected String createTrackIdSQL()
	{
		return null;
	}

}
