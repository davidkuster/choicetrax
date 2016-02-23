package com.choicetrax.client.data;

import java.util.LinkedList;

import com.choicetrax.client.data.cache.CacheObjectList;


public class Releases 
	implements CacheObjectList 
{   
	
	private LinkedList<ReleaseDetail> list = null;
	
	
    public Releases() {
    	super();
    	list = new LinkedList<ReleaseDetail>();
    }
    
    public Releases( LinkedList<ReleaseDetail> releasesList ) {
    	super();
    	this.list = releasesList;
    }
 
    
    public void addRelease( ReleaseDetail rd ) {
    	list.add( rd );
    }
    
    public LinkedList<ReleaseDetail> getReleasesList() {
    	return list;
    }
    
    
    public int size() {
    	return list.size();
    }
    
}