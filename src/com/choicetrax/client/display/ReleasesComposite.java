package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.data.*;
import com.choicetrax.client.display.panels.ListenBuyPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.constants.Constants;


public class ReleasesComposite 
	extends AbstractReleasesComposite 
{
	
	// loading status pop up
	private PopupPanel loadingPopUp = null;
	
		
	public ReleasesComposite( ChoicetraxViewManager manager )
	{
		super( manager, Constants.PANEL_RELEASES );
	}
	
			
	protected Label createActionHeader()
	{
		return new Label( "Wish" );
	}
	
	
	
	protected ListenBuyPanel createListenBuyPanel( ReleaseDetail rd )
	{		
		return new ListenBuyPanel( viewManager, rd );
	}
	
		
	
	public void setWaitingState( boolean waiting )
	{
		if ( waiting )
			showLoadingPopUp();
		else
			hideLoadingPopUp();
	}
	
	
	private void showLoadingPopUp()
	{
		if ( loadingPopUp == null )
			initLoadingPopUp();
		
		loadingPopUp.show();
		loadingPopUp.center();
	}
	
	private void hideLoadingPopUp()
	{
		if ( loadingPopUp == null )
			initLoadingPopUp();
		
		loadingPopUp.hide();
	}
	
	private void initLoadingPopUp()
	{
		loadingPopUp = new PopupPanel( false );
		loadingPopUp.setStyleName( "loadingPanel" );
		loadingPopUp.setAnimationEnabled( true );
		
		loadingPopUp.setWidget( new LoadingComposite() );
		
		loadingPopUp.center();
	}
		
}
