package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;

import com.choicetrax.client.logic.ChoicetraxViewManager;


public class VirtualCartsComposite 
	extends AbstractMultiCartsComposite
{
	
	private PopupPanel transferPopUp = null;
		
	
	public VirtualCartsComposite( ChoicetraxViewManager manager ) {
		super( manager );
	}
	
	
	protected Label createHeaderLabel() {
		return new Label( "Virtual Carts" );
	}
	
	
	protected void emptyCartsAlert() {
		Window.alert( "Your virtual carts are empty.  Get shopping!" );
	}
	
	
	protected AbstractReleasesComposite 
		createMultiCartReleasesComposite( ChoicetraxViewManager viewManager, int partnerID ) 
	{
		return new VirtualCartReleasesComposite( viewManager, partnerID );
	}
	
	
	public void setWaitingState( boolean waiting )
	{
		if ( waiting )
			showTransferPopUp();
		else
			hideTransferPopUp();
	}
	
	
	private void showTransferPopUp()
	{
		if ( transferPopUp == null )
			initTransferPopUp();
		
		transferPopUp.show();
	}
	
	private void hideTransferPopUp()
	{
		transferPopUp.hide();
	}
	
	private void initTransferPopUp()
	{
		transferPopUp = new PopupPanel( true );
		transferPopUp.setStyleName( "loadingPanel" );
		transferPopUp.setAnimationEnabled( true );
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		vp.setSpacing( 5 );
		vp.add( new Label( "Transferring..." ) );
		vp.add( new Image( "img/loading4.gif" ) );
		transferPopUp.setWidget( vp );
		
		transferPopUp.center();
	}

}
