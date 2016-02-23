package com.choicetrax.client.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.VirtualCart;
import com.choicetrax.client.data.Format;
import com.choicetrax.client.data.Partner;
import com.choicetrax.client.display.panels.ListenBuyPanelVirtualCart;
import com.choicetrax.client.display.panels.ListenBuyPanel;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.handlers.buy.BuyHandler;


public class VirtualCartReleasesComposite 
	extends AbstractReleasesComposite
{
	
	private int partnerID 			= -1;
	private String partnerName 		= null;
	private String partnerCurrency	= null;
	
	private VerticalPanel checkoutVP 	= null;
	private DisclosurePanel checkoutDP 	= null;
	private boolean checkoutInitialized = false;
	private BuyHandler buyHandler 		= null;

	
	public VirtualCartReleasesComposite( ChoicetraxViewManager manager, int partnerID )
	{
		super( manager, Constants.PANEL_VIRTUAL_CARTS );
		
		Partner partner = viewManager.getConfigData().getPartner( partnerID );
		
		this.partnerID = partnerID;
		this.partnerName = partner.getPartnerName();
		this.partnerCurrency = partner.getCurrency();
		
		buyHandler = Constants.createBuyHandler( viewManager, partnerName );
	}
	
	
	
	public void updateDisplay( LoaderResponse response )
	{
		checkoutInitialized = false;
		super.updateDisplay( response );
	}
	
	
	
	protected Label createActionHeader()
	{
		return new Label( "Wish / Rem" );
	}
		
	
	protected ListenBuyPanel createListenBuyPanel( ReleaseDetail rd )
	{		
		return new ListenBuyPanelVirtualCart( viewManager, rd, partnerID );
	}
	
	
	protected Widget createHeaderWidget( int cacheType )
	{
		checkoutVP = new VerticalPanel();
		checkoutVP.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		checkoutVP.setStylePrimaryName( "checkoutVerticalPanel" );
		
		checkoutDP = new DisclosurePanel();
		checkoutDP.setAnimationEnabled( true );	
		
		HyperlinkLabel checkoutLabel = new HyperlinkLabel( "Checkout", new CheckoutListener() );
		checkoutLabel.addStyleName( "strongText" );
		HTML checkoutInfoText = new HTML( "<i>(Transfer trax to " + partnerName + " cart)</i>" );
		
		HyperlinkLabel partnerCartLabel = new HyperlinkLabel( "View cart at " + partnerName,
																new PartnerCartListener() );
		HTML partnerCartInfoText = new HTML( "<i>(No transfer)</i>" );
		
		VirtualCart cart = viewManager.getUserManager().getCurrentUser().getVirtualCarts().getCart( partnerName );
		String totalPrice = cart.getCartTotalPrice();
		
		String currency = "";
		if ( partnerCurrency != null ) 
			currency += partnerCurrency;
		
		HyperlinkLabel clearCartLabel = new HyperlinkLabel( "empty cart", new ClearCartListener() );
		
		HorizontalPanel checkoutHP = new HorizontalPanel();
		checkoutHP.add( checkoutLabel );
		checkoutHP.add( new HTML( " &nbsp; " ) );
		checkoutHP.add( checkoutInfoText );
		
		HorizontalPanel totalEmptyHP = new HorizontalPanel();
		totalEmptyHP.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		totalEmptyHP.setWidth( "100%" );
		totalEmptyHP.add( new HTML( "cart total: " + currency + totalPrice ) );
		totalEmptyHP.add( clearCartLabel );
		
		HorizontalPanel partnerCartHP = new HorizontalPanel();
		partnerCartHP.add( partnerCartLabel );
		partnerCartHP.add( new HTML( " &nbsp; " ) );
		partnerCartHP.add( partnerCartInfoText );
		
		checkoutVP.add( checkoutHP );
		checkoutVP.add( totalEmptyHP );
		checkoutVP.add( checkoutDP );
		checkoutVP.add( partnerCartHP );
						
		return checkoutVP;
	}
	
	
	protected Widget createTrackNamePanel( ReleaseDetail rd )
	{
		Widget nameLabel = super.createTrackNamePanel( rd );
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( nameLabel );
		vp.add( createFormatLabel( rd ) );
		
		return vp;
	}
	
	
	
	private Label createFormatLabel( ReleaseDetail rd )
	{
		VirtualCart cart = viewManager.getCurrentUser().getVirtualCarts().getCart( partnerName );
		Format format = cart.getSelectedFormat( rd.getTrackID() );
		
		String formatName = format.getFormatName() + " "
							+ format.getBitrate() + " "
							+ format.getPrice();
		
		HTML formatLabel = new HTML(formatName );
		formatLabel.setStylePrimaryName( "virtualCartFormatLabel" );
		
		return formatLabel;
	}
	
	
	// note that i should display the selected formats and prices
	// for each track in the cart, as well as a total price...
	
	
	private void initializeCheckout() 
	{
		FlexTable table = new FlexTable();
		table.setCellSpacing( 10 );
		
		Label labelA = new Label( "Ok, here's how this works.  When you click the \"Login\" "
						+ "button we'll open the " + partnerName + " website in a new tab or "
						+ "window in your browser.  Log in there " 
						+ "using your " + partnerName + " login details.  After that come "
						+ "back to this window and click the \"Transfer\" button.  We'll " 
						+ "then copy all the trax in your " + partnerName + " Virtual Cart "
						+ "on Choicetrax to your actual cart on " + partnerName + ". " );

		Label labelB = new Label( "Once that successfully finishes, click the \"Complete\" "
						+ "button and we'll move these trax out of your Virtual Cart "
						+ "and into your Choicetrax Purchase History. "
						+ "All that remains is to complete the transaction with " + partnerName 
						+ " and start enjoying your new music!");
		
		Label labelC = new Label( "(Please note that due to the nature of browser security "
						+ "we are unable to tell if an error occurs in transfering your trax. "
						+ "If something doesn't show up in your " + partnerName + " cart "
						+ "it's likely due to a territory restriction.  If not, please let "
						+ "us know.)" );
		labelC.setStylePrimaryName( "italicText" );
		
		Label login 	= new HyperlinkLabel( "Login", new PartnerLoginListener() );
		Label transfer 	= new HyperlinkLabel( "Transfer", new PartnerTransferListener() );
		//Label complete 	= new HyperlinkLabel( "Complete", new PartnerTransferCompleteListener() );
		
		VerticalPanel instr = new VerticalPanel();
		
		instr.add( labelA );
		instr.add( new HTML( "&nbsp;" ) );
		instr.add( labelB );
		instr.add( new HTML( "&nbsp;" ) );
		instr.add( labelC );
		
		table.setWidget( 0, 0, instr );
		
		table.setWidget( 0, 1, login );
		table.setWidget( 1, 0, transfer ); // this doesn't seem right
		//table.setWidget( 2, 0, complete );
		
		table.getFlexCellFormatter().setRowSpan( 0, 0, 3 );
		
		checkoutDP.add( table );
	}
	
	
	
	private class CheckoutListener implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{		
			if ( ! checkoutInitialized ) {
				initializeCheckout();
				checkoutInitialized = true;
			}
					
			DeferredCommand.addCommand( new Command() {
				public void execute() 
				{
					if ( checkoutDP.isOpen() )
						checkoutDP.setOpen( false );
					else
						checkoutDP.setOpen( true );
				}
			});
		}
	}
	
	
	private class ClearCartListener implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			if ( Window.confirm( "Click OK to clear this virtual cart." ) )
			{
				viewManager.getUserManager().clearVirtualCart( partnerName );
			}
		}
	}
	
	
	private class PartnerLoginListener implements ClickHandler
	{
		public void onClick( ClickEvent event ) {
			buyHandler.login();
		}
	}

	
	private class PartnerTransferListener implements ClickHandler
	{
		public void onClick( ClickEvent event ) {
			buyHandler.transfer();
		}
	}
	
	
	private class PartnerCartListener implements ClickHandler
	{
		public void onClick( ClickEvent event ) {
			Window.open( buyHandler.getCartURL(), buyHandler.getPartnerWindowName(), "" );
		}
	}
	
	
	/*private class PartnerTransferCompleteListener implements ClickListener
	{
		public void onClick( Widget sender ) {
			buyHandler.transferComplete();
		}
	}*/
	
}
