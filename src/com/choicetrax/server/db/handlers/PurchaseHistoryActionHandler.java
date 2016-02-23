package com.choicetrax.server.db.handlers;

import java.util.Iterator;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.PurchaseHistoryAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.VirtualCart;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.db.handlers.BaseActionHandler;


public class PurchaseHistoryActionHandler 
	extends BaseActionHandler
{
	
	private PurchaseHistoryAction action = null;
	

	public PurchaseHistoryActionHandler( HandlerAction handlerAction ) 
	{
		super( handlerAction );
		this.action = (PurchaseHistoryAction) handlerAction;
	}
	
	
	
	public ActionResponse handleAction()
		throws ChoicetraxException
	{
		VirtualCart cart = action.getVirtualCart();
		if ( cart.size() == 0 )
		{
			response.setResponseCode( Constants.ACTION_RESPONSE_ERROR );
			response.setResponseText( "PurchaseHistoryActionHandler error, VirtualCart.size() = 0"
									+ "\ntrax not transferred to or from Purchase History" );
			
			return response;
		}
		else
			return super.handleAction();
	}
	

	
	protected String createAddSQL()
	{
		VirtualCart cart = action.getVirtualCart();
		int partnerID = action.getPartnerID();
		int userID = action.getUserID();
		
		String sql = "insert into UserPurchaseHistory " 
					+ "( "
					+	"UserID, "
					+	"TrackID, "
					+	"PartnerID, "
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
						+ trackID + ", "
						+ partnerID + ", "
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
		VirtualCart cart = action.getVirtualCart();
		int partnerID = action.getPartnerID();
		int userID = action.getUserID();
		
		String sql = "update UserPurchaseHistory " 
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
				
		return sql;
	}

}
