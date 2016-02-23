package com.choicetrax.client.input;

import com.google.gwt.user.client.ui.Composite;
//import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
//import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class ClickableImageOnOff 
	extends Composite
	//implements MouseListener
{
	
	public static final int DEFAULT_ON = 1;
	public static final int DEFAULT_OFF = 2;
	
	private Image onImage = null;
	private Image offImage = null;
	private int defaultState = DEFAULT_ON;
	private boolean on = true;
	
	//private FocusPanel fp = new FocusPanel(); 
	private SimplePanel fp = new SimplePanel();
	
	
	public ClickableImageOnOff( Image onImg, Image offImg, int state ) 
	{
		super();
		
		initWidget( fp );
		
		this.onImage = onImg;
		this.offImage = offImg;
		this.defaultState = state;
		
		offImage.setStylePrimaryName( "clickableImage" );
		
		if ( defaultState == DEFAULT_OFF ) {
			fp.setWidget( offImage );
			on = false;
		}
		else
			fp.setWidget( onImage );
		
		//fp.addMouseListener( this );
	}
	
	
	
	public void setStateOff() {
		on = false;
		fp.setWidget( offImage );
	}
	
	public void setStateOn() {
		on = true;
		fp.setWidget( onImage );
	}
	
	
	/* mouse listener methods */
	
	public void onMouseDown( Widget sender, int x, int y ) 
	{
		if ( ! on ) {
			on = true;
			fp.setWidget( onImage );
		}
	}
	
	public void onMouseEnter( Widget sender ) {
		//if ( ! on ) fp.setWidget( onImage );
	}
	
	public void onMouseLeave( Widget sender ) {
		//if ( ! on ) fp.setWidget( offImage );
	}
	
	public void onMouseMove( Widget sender, int x, int y ) {
		
	}
	
	public void onMouseUp( Widget sender, int x, int y ) {
		
	}
	
	/* ---------------------- */

}
