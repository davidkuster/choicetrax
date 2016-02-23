package com.choicetrax.server.db.handlers;

import java.util.Iterator;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.VirtualCartAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.VirtualCart;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.db.handlers.BaseActionHandler;


public class VirtualCartActionHandler 
	extends BaseActionHandler
{
	
	private int userID = -1;
	private int partnerID = -1;
	private VirtualCart cart = null;
	
	
	public VirtualCartActionHandler( HandlerAction handlerAction )
	{
		super( handlerAction );
		VirtualCartAction action = (VirtualCartAction) handlerAction;
		
		this.userID 	= action.getUserID();
		this.partnerID 	= action.getPartnerID();
		this.cart	 	= action.getVirtualCart();
	}
	
	
	public ActionResponse handleAction()
		throws ChoicetraxException
	{
		if ( cart.size() == 0 )
		{
			response.setResponseCode( Constants.ACTION_RESPONSE_ERROR );
			response.setResponseText( "VirtualCartActionHandler error, VirtualCart.size() = 0"
									+ "\ntrax not transferred to or from Virtual Cart" );
			
			return response;
		}
		else
			return super.handleAction();
	}
	
	
	protected String createAddSQL()
	{
		String sql = "insert into UserVirtualCarts "
					+ "( "
					+	"UserID, "
					+	"PartnerID, "
					+	"TrackID, "
					+	"PartnerFormatCode, "
					+	"DateAdded, "
					+	"DateRemoved "
					+ ") "
					+ "values "; 

		StringBuffer sb = new StringBuffer();
		
		Iterator<ReleaseDetail> i = cart.getReleaseDetailIterator();
		while ( i.hasNext() ) 
		{
			ReleaseDetail rd = i.next();
			int trackID = rd.getTrackID();
			Format format = cart.getSelectedFormat( trackID );
			
			if ( sb.length() > 0 ) sb.append( ", " );
			
			sb.append( "( " 
						+ userID + ", " 
						+ partnerID + ", "
						+ trackID + ", "
						+ "'" + format.getFormatCode() + "', "
						+ "sysdate(), "
						+ "null "
						+ " )" );
		}	
		
		sql += sb.toString()
				+ "on duplicate key update "
				+ 	"DateAdded = sysdate(), "
				+	"DateRemoved = null ";
		
		return sql;
	}
	
	protected String createRemoveSQL()
	{
		String sql = "update UserVirtualCarts "
					+ "set DateRemoved = sysdate() "
					+ "where ";
		
		StringBuffer sb = new StringBuffer();
		
		Iterator<ReleaseDetail> i = cart.getReleaseDetailIterator();
		while ( i.hasNext() ) 
		{
			ReleaseDetail rd = i.next();
			int trackID = rd.getTrackID();
			
			if ( sb.length() > 0 ) sb.append( " or " );
			
			sb.append( "( "
						+ "UserID = " + userID + " and " 
						+ "TrackID = " + trackID + " and "
						+ "PartnerID = " + partnerID
						+ " )" );
		}	
		
		sql += sb.toString();
		
		/*
		UserID = " + this.userID + " "
				+	"and PartnerID = " + this.partnerID + " "
				+	"and TrackID = " + this.trackID + " "
				+	"and PartnerFormatCode = '" + this.formatCode + "'";
		*/
		
		return sql;
	}

}
