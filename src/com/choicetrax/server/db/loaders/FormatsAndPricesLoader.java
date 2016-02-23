package com.choicetrax.server.db.loaders;

import java.sql.*;

import com.choicetrax.client.actions.LoaderAction;
import com.choicetrax.client.actions.loaderactions.LoadFormatsAndPricesAction;
import com.choicetrax.client.actions.responses.FormatsAndPrices;
import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.db.DataLoader;
import com.choicetrax.server.util.jdbc.*;


/**
 * loads format and price options for a specific partner
 * and track combination.  called from the client side 
 * when user clicks on a "buy" icon.
 * 
 * @author David Kuster
 */
public class FormatsAndPricesLoader 
	implements DataLoader
{
	
	public FormatsAndPricesLoader()
	{
		super();
	}
	
	
	public LoaderResponse loadData( LoaderAction loaderAction )
		throws ChoicetraxException
	{
		LoadFormatsAndPricesAction action = (LoadFormatsAndPricesAction) loaderAction;
		int partnerID = action.getPartnerID();
		String partnerTrackID = action.getPartnerTrackID();
		int trackID = action.getTrackID();
		
		String methodName = "loadFormatsAndPrices()";
		
		String sql = "select pp.Format, pp.Bitrate, pp.Price, pp.PartnerFormatCode "
					+ "from PartnerInventory pi, PartnerPrices pp "
					+ "where pi.InventoryID = pp.InventoryID "
					+	"and pi.PartnerID = ? "
					+ 	"and pi.PartnerTrackID = ? "
					+	"and pi.TrackID = ? "
					+ "order by pp.Price desc, pp.Bitrate desc";
		
		DBResource dbHandle = null;
		
		FormatsAndPrices formats = new FormatsAndPrices();
    	
    	try
    	{
    		dbHandle = ResourceManager.getDBConnection();
    		
    		dbHandle.prepareStatement( sql );
    		dbHandle.setInt( 	1, partnerID		);
    		dbHandle.setString( 2, partnerTrackID 	);
    		dbHandle.setInt(	3, trackID			);
    		
    		ResultSet rs = dbHandle.executeQuery();
    		while ( rs.next() )
    		{
				Format format = new Format();
				format.setFormatName( 	rs.getString( "Format" ) 	);
				format.setBitrate( 		rs.getString( "Bitrate" ) 	);
				format.setPrice( 		rs.getString( "Price" ) 	);
				format.setFormatCode(	rs.getString( "PartnerFormatCode" ) );
				
				formats.addFormat( format );
    		}
    		rs.close();
    	}
    	catch ( ChoicetraxException cte )
    	{
    		throw cte;
    	}
    	catch ( Throwable t )
    	{
    		throw new ChoicetraxException( "Error executing formats and prices load " 
    										+ "with partnerID [" + partnerID + "] "
    										+ "and partner track ID [" + partnerTrackID + "]: "
    										+ "and trackID [" + trackID + "]: "
    										+ t,
    										this.getClass().getName() + "." + methodName );
    	}
    	finally
    	{
    		if ( dbHandle != null )
    			dbHandle.close();
    	}

		return formats;
	}
	
}

