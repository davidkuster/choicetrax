package com.choicetrax.client.data;

import java.util.LinkedList;

import com.choicetrax.client.data.cache.CacheObjectList;


public class DJCharts implements CacheObjectList
{

	private LinkedList<DJChartDetail> list = null;
	
	
    public DJCharts() {
    	super();
    	list = new LinkedList<DJChartDetail>();
    }
    
    public DJCharts( LinkedList<DJChartDetail> list ) {
    	super();
    	this.list = list;
    }
 
    
    public void addChart( DJChartDetail chart ) {
    	list.add( chart );
    }
    
    public LinkedList<DJChartDetail> getChartsList() {
    	return list;
    }
    
    
    public int size() {
    	return list.size();
    }
    
}
