package com.choicetrax.server.db.handlers;

import java.util.Iterator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.ClientLoggerAction;
import com.choicetrax.client.actions.responses.ActionResponse;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.db.ActionHandler;


public class ClientLoggerActionHandler implements ActionHandler
{
	
	private static Logger logger = Logger.getLogger( ClientLoggerActionHandler.class );
			
	private ClientLoggerAction action = null;
	private String clientIP = null;
	
	
	public ClientLoggerActionHandler( HandlerAction handlerAction ) {
		super();
		this.action = (ClientLoggerAction) handlerAction;
		this.clientIP = action.getIpAddress();
	}

	
	@Override
	public ActionResponse handleAction() 
		throws ChoicetraxException 
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append( "Client side error: \n"
					+ "User [" + action.getUsername() + "] w/userID [" + action.getUserID() + "] \n"
					+ "Browser [" + action.getBrowserName() + "] \n"
					+ "IP Addr [" + clientIP + "] \n" );
		
		LinkedList<String> actions = action.getActions();
		if ( actions != null )
		{
			sb.append( "User actions [" );
			
			Iterator<String> i = actions.iterator();
			for ( int x=0; i.hasNext(); x++ ) 
			{
				if ( x > 0 ) sb.append( "\n\t" );
				sb.append( i.next() );
			}
			
			sb.append( "] \n" );
		}
		
		sb.append( "Client exception: " + action.getExceptionText() );
		
		logger.error( sb.toString() );
		
		return null;
	}

}
