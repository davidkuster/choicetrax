package com.choicetrax.client.logic.handlers.buy;

import java.util.Iterator;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.VirtualCart;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.DoNothingController;
import com.choicetrax.client.actions.handleractions.PartnerTransferAction;
import com.choicetrax.client.constants.Constants;


public abstract class AbstractBuyHandler 
	implements BuyHandler 
{

	protected ChoicetraxViewManager viewManager = null;
	
	protected int partnerID 			= -1;
	protected String partnerName 		= null;
	protected String partnerWindow		= null;
	
	protected String loginURL			= null;
	protected String cartURL			= null;
	
	protected boolean loggedIn = false;
	protected VirtualCart cart = null;
	protected Iterator<ReleaseDetail> cartIterator = null;

	
	
	public AbstractBuyHandler( ChoicetraxViewManager manager,
								String name,
								String window,
								String login,
								String cart )
	{
		this.viewManager = manager;
		this.partnerName = name;
		this.partnerWindow = window;
		this.loginURL = login;
		this.cartURL = cart;
		
		partnerID = viewManager.getConfigData().lookupPartnerID( partnerName );
	}
	
	
	
	public String getCartURL() {
		return cartURL;
	}
	
	
	public String getPartnerWindowName() {
		return partnerWindow;
	}
	
	
	
	public void login() 
	{ 
		Window.open( loginURL, partnerWindow, "" );
		loggedIn = true;
	}
	
	
	public void transfer() 
	{ 
		if ( ! loggedIn )
			Window.alert( "Did you remember to log in to " + partnerName + "?\n\n"
						+ "Please click the Login link and make the code happy. :)" );
		else
		{
			viewManager.setWaitingState( Constants.PANEL_VIRTUAL_CARTS, true );
			
			cart = viewManager.getCurrentUser().getVirtualCarts().getCart( partnerName );
			cartIterator = cart.getReleaseDetailIterator();
			
			// log transfer in TransferTracking database table
			logCartTransfer();
			
			// initiate transfer
			loadNextTrack();
		}
	}
	
	
	private void logCartTransfer()
	{
		int[] trackIDs = cart.getReleasesCache().getTrackIDsArray();
		
		PartnerTransferAction action = new PartnerTransferAction();
		action.setUserID( viewManager.getCurrentUser().getUserID() );
		action.setPartnerID( partnerID );
		action.setTrackIDs( trackIDs );
		
		viewManager.deferAction( action, new DoNothingController() );
	}
		
	
	public void transferComplete() 
	{ 
		viewManager.getUserManager().virtualCartCheckoutComplete( partnerName );
	}
	
	
	public void loadNextTrack()
	{
		if ( cartIterator.hasNext() ) 
		{
			ReleaseDetail rd = cartIterator.next();
			loadTrack( rd );
		}
		else
		{
			promptUserIfTransferCompletedCorrectly();
		}
	}
	
	
	
	protected void promptUserIfTransferCompletedCorrectly()
	{
		viewManager.setWaitingState( Constants.PANEL_VIRTUAL_CARTS, false );
		
		Window.open( cartURL, partnerWindow, "" );
		
		// do this in a deferred command so transferring popup
		// has a chance to be set to false
		
		DeferredCommand.addCommand( new Command() { 
			public void execute() 
			{
				final PopupPanel confirmPopup = new PopupPanel( false );
				//confirmPopup.setStylePrimaryName( "popup" );
				confirmPopup.setStylePrimaryName( "loadingPanel" );
				
				VerticalPanel vp = new VerticalPanel();
				vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
				vp.setSpacing( 5 );
				
				vp.add( new Label( "Transfer Complete." ) );
				vp.add( new Label( "Please check your cart on " + partnerName + "." ) );
				vp.add( new Label( "Did all the trax transfer correctly?" ) );
				
				Button yes = new Button( "Yes" );
				yes.addClickHandler( new ClickHandler() {
					public void onClick( ClickEvent event ) {
						confirmPopup.hide();
						transferComplete();
					}
				});
				
				Button no = new Button( "No" );
				no.addClickHandler( new ClickHandler() {
					public void onClick( ClickEvent event ) {
						confirmPopup.hide();
					}
				});
				
				HorizontalPanel hp = new HorizontalPanel();
				hp.add( yes );
				hp.add( new HTML( "&nbsp;" ) );
				hp.add( no );					
				
				vp.add( hp );
				
				vp.add( new HTML( "<i>(If you click Yes these trax will be moved to your "
						+ "Choicetrax<br>"
						+ "Purchase History.  If you click No they will stay "
						+ "here in your Virtual Cart.)</i>" ) );
				
				confirmPopup.add( vp );
				
				confirmPopup.center();
				confirmPopup.show();
				
				// ask user to verify that trax transferred correctly
				//if ( Window.confirm( "Transfer complete"
				//					+ "\nDid trax transfer correctly?" ) )
				//	transferComplete();
			}
		});
	}

	
	protected abstract void loadTrack( ReleaseDetail rd );
	
	
	
	
	/**
	 * this class is used by those subclasses that require
	 * forms to load their tracks.  
	 * 
	 * when the form submit completes it calls the loadNextTrack() 
	 * method.
	 */
	protected class LoadNextTrackFormHandler 
		implements FormPanel.SubmitCompleteHandler, FormPanel.SubmitHandler 
	{
		boolean called = false;
		
		public void onSubmit( FormPanel.SubmitEvent event ) { }
		
		public void onSubmitComplete( FormPanel.SubmitCompleteEvent event ) 
		{
			if ( ! called ) 
			{
				loadNextTrack();
				called = true;
			}
		}
	}

}
