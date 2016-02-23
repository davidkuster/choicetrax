package com.choicetrax.server.db.loaders.sphinx;

import org.sphx.api.SphinxClient;
import org.sphx.api.SphinxException;
import org.sphx.api.SphinxResult;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.client.util.exception.ChoicetraxSphinxException;

import com.choicetrax.server.util.jdbc.ResourceManager;


public abstract class SphinxLoader
{
	
	private SphinxClient cl = null;
	private String index = null;
	

	public SphinxLoader() 
		throws ChoicetraxException
	{
		super();
		
		try
		{
			cl = new SphinxClient( ResourceManager.getSphinxUrl(), 
									ResourceManager.getSphinxPort() );
			cl.SetMatchMode( SphinxClient.SPH_MATCH_EXTENDED2 );
	    	cl.SetLimits( 0, Constants.MAX_TRACKIDS_PER_QUERY, Constants.MAX_TRACKIDS_PER_QUERY );
		}
		catch ( SphinxException se ) {
			throw new ChoicetraxSphinxException( "SphinxLoader initialization exception: " + se );
		}
	}
	
	
	protected void setIndex( String index ) {
		this.index = index;
	}
	
	
	protected void setSortMode( String sortMode )
		throws ChoicetraxException
	{
		try 
		{
			cl.SetSortMode( SphinxClient.SPH_SORT_EXTENDED, sortMode );
		}
		catch ( SphinxException se ) {
			throw new ChoicetraxSphinxException( "SphinxLoader sort exception: " + se );
		}
	}
	
	
	public void setLimit( int limit ) 
		throws ChoicetraxException
	{
		try
		{
			cl.SetLimits( 0, limit );
		}
		catch ( SphinxException se ) {
			throw new ChoicetraxSphinxException( "SphinxLoader limit exception: " + se );
		}
	}
	
	
	public void setFilter( String field, int[] ids )
		throws ChoicetraxException
	{
		try 
		{
			cl.SetFilter( field, ids, false );
		}
		catch ( SphinxException se ) {
			throw new ChoicetraxSphinxException( "SphinxLoader filter exception: " + se );
		}
	}
	
	
	public void setFilterRange( String field, int begin, int end )
		throws ChoicetraxException
	{
		try 
		{
			cl.SetFilterRange( field, begin, end, false );
		}
		catch ( SphinxException se ) {
			throw new ChoicetraxSphinxException( "SphinxLoader filter range exception: " + se );
		}
	}
	
	
	public int[] executeQuery( String query ) 
		throws ChoicetraxException
	{
		try 
		{
			SphinxResult result = cl.Query( query, index );
			
			if ( result != null ) 
			{
				int numMatches = result.matches.length;
				int[] docIDs = new int[ numMatches ];
				
				for ( int i=0; i < numMatches; i++ ) {
					docIDs[ i ] = (int) result.matches[ i ].docId;
				}
				
				return docIDs;
			}
	    	else
	    		throw new ChoicetraxSphinxException( "Search error w/query [" + query + "]: "
	    											+ cl.GetLastError() );
		}
		catch ( SphinxException se ) {
			throw new ChoicetraxSphinxException( "SphinxLoader query exception:" + se );
		}
	}
	
	
	public SphinxResult getResult( String query )
		throws ChoicetraxException
	{
		try
		{
			SphinxResult result = cl.Query( query );
			
			if ( result != null )
				return result;
			else
	    		throw new ChoicetraxSphinxException( 
	    					"Search result error w/query [" + query + "]: " + cl.GetLastError() );
		}
		catch ( SphinxException se ) {
			throw new ChoicetraxSphinxException( "SphinxLoader query result exception: " + se );
		}
	}
	

}
