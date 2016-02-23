package com.choicetrax.client.data.cache;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.choicetrax.client.data.DJChartDetail;
import com.choicetrax.client.data.DJCharts;


public class DJChartsCachePage 
	implements CachePage 
{

	private LinkedHashMap<Integer,DJChartDetail> cache 
		= new LinkedHashMap<Integer,DJChartDetail>();

	
	public DJChartsCachePage() {
		super();
	}
	
	
	public void addChart( DJChartDetail chart )
	{
		if ( chart != null )
			cache.put( new Integer( chart.getChartID() ), chart );
	}
	
	
	public void addCharts( DJCharts charts )
	{
		if ( charts != null )
		{
			Iterator<DJChartDetail> i = charts.getChartsList().iterator();
			while ( i.hasNext() )
			{
				this.addChart( i.next() );
			}
		}
	}
	
	
	public void addChartID( int chartID )
	{
		cache.put( new Integer( chartID ), null );
	}
	
	
	public DJChartDetail getChart( int chartID )
	{
		return cache.get( new Integer( chartID ) );
	}
	
	
	public int[] getChartIDs() 
	{
		if ( this.size() > 0 )
		{
			LinkedList<Integer> idList = new LinkedList<Integer>();
			Iterator<Integer> i = cache.keySet().iterator();
			while ( i.hasNext() ) {
				idList.add( i.next() );
			}
			
			int size = idList.size();
			int[] chartIDs = new int[ size ];
			for ( int x=0; x < size; x++ ) {
				chartIDs[ x ] = idList.get( x );
			}
			
			return chartIDs;
		}
		else return new int[ 0 ];
	}
	
	
	public DJCharts getCharts() 
	{
		DJCharts charts = new DJCharts();
		
		Iterator<DJChartDetail> i = cache.values().iterator();
		while ( i.hasNext() ) {
			DJChartDetail chart = i.next();
			if ( chart != null )
				charts.addChart( chart );
		}
		
		return charts;
	}
	
	
	public int size() {
		return cache.size();
	}
	
	
	public void clearPage() 
	{
		Iterator<Integer> i = cache.keySet().iterator();
		while ( i.hasNext() ) {
			cache.put( i.next(), null );
		}
	}

}
