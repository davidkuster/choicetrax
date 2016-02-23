package com.choicetrax.client.actions.responses;

import java.util.LinkedList;

import com.choicetrax.client.data.Format;


public class FormatsAndPrices implements LoaderResponse 
{
	
	private LinkedList<Format> formats = null;

    
    public FormatsAndPrices() 
    {
    	formats = new LinkedList<Format>();
    }
    
    public FormatsAndPrices( LinkedList<Format> formatsList ) {
    	this.formats = formatsList;
    }
    
    public void addFormat( Format format )
    {
    	this.formats.add( format );
    }
    
    public LinkedList<Format> getFormats()
    {
    	return formats;
    }

}
