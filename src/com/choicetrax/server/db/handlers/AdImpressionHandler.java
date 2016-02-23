package com.choicetrax.server.db.handlers;

import java.util.Iterator;
import java.util.List;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.AdImpressionAction;

import com.choicetrax.server.constants.ServerConstants;


public class AdImpressionHandler 
	extends BaseActionHandler 
{
	
	private AdImpressionAction action = null;
	
	
	public AdImpressionHandler( HandlerAction handlerAction ) 
	{ 
		super( handlerAction );
		this.action = (AdImpressionAction) handlerAction;
	}
	
	
	
	protected String createAddSQL()
	{
		StringBuffer values = new StringBuffer();
		
		// if userID is not set, use IP address instead
		String userID = ServerConstants.readUserIdOrIpForMySQL( action );
		
		List<Integer> idList = action.getAdIdList();
		if ( idList != null )
		{		
			Iterator<Integer> i = idList.iterator();
			while ( i.hasNext() ) 
			{
				if ( values.length() > 0 ) values.append( ", " );
				
				int adID = i.next();
				
				values.append( "( " 
								+ adID + ", "
								+ userID + ", "
								+ "( current_date() + 0), "
								+ "1 )" );
			}
		}
		
		//String sql = "update AdImpressions "
		//			+ "set ImpressionsCount = ImpressionsCount + 1 "
		//			+ "where AdID in ( " + sb.toString() + " ) ";
		
		String sql = "insert into AdImpressions "
					+ "( AdID, UserID, ImpressionDate, NumImpressions ) "
					+ "values "
					+ values.toString()
					+ " on duplicate key update "
					+	"NumImpressions = NumImpressions + 1";
		
		return sql;
	}
	
	
	protected String createRemoveSQL()
	{
		return null;
	}

}
