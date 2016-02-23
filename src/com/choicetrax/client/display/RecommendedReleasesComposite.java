package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.display.panels.ListenBuyPanel;
import com.choicetrax.client.display.panels.ListenBuyPanelRecommended;
import com.choicetrax.client.input.ClickableImage;
import com.choicetrax.client.input.listeners.PlayOptionsListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class RecommendedReleasesComposite 
	extends AbstractReleasesComposite 
{


	public RecommendedReleasesComposite( ChoicetraxViewManager manager ) 
	{
		super( manager, Constants.PANEL_RECOMMENDED_TRACKS );
		this.viewManager = manager;
	}

	
	
	protected Widget createHeaderWidget( int cacheType )
	{
		VerticalPanel vp = new VerticalPanel();
		
		Label headerLabel = new Label( "Recommended Releases" );
		headerLabel.setStylePrimaryName( "mainPanelHeaderLabel" );
		
		String text = "These recommendations are based on the trax you have rated "
						+ "and added to your wishlist, virtual carts, and purchase "
						+ "history. Once these trax are rated or added to your wishlist "
						+ "or carts, they will no longer appear in your recommendations. "
						+ "To get more recommendations, rate and/or add more trax "
						+ "to your wishlist, virtual carts, purchase history, etc!"; 
			
		vp.add( headerLabel );
		vp.setCellHorizontalAlignment( headerLabel, HasHorizontalAlignment.ALIGN_CENTER );
		
		vp.add( new Label( text ) );
		vp.add( new HTML( "&nbsp;" ) );
		
		return vp;
	}

	
	protected Widget createActionHeader() 
	{
		return new Label( "Wish / Rem" );
	}
	
	
	/**
	 * Override createTableHeaders to only create sort options on Release Date.
	 */
	protected void createTableHeaders( FlexTable table, int cacheType )
	{
		int column = 0;
				
		// should only be getting recommended releases cache type
		if ( cacheType != Constants.CACHE_RELEASES_RECOMMENDED ) {
			super.createTableHeaders( table, cacheType );
			return;
		}
		
		table.setWidget( 1, column, new Label( "EP" ) );
		table.setWidget( 1, ++column, new Label( "Artist" ) );
		table.setWidget( 1, ++column, new Label( "Album / Track" ) );
		table.setWidget( 1, ++column, new Label( "Mix / Remixer" ) );
		table.setWidget( 1, ++column, new Label( "Label" ) );
		table.setWidget( 1, ++column, new Label( "Genres" ) );
		table.setWidget( 1, ++column, new Label( "Date" ) );
		
		table.setWidget( 2, column, createHeaderSorts( "Release Date", 
														Constants.RELEASES_SORT_BY_DATE,
														SORT_DATATYPE_DATE ) );
		
		Image playOptions = new ClickableImage( "img/layout/iconListen_sm.gif" );
		playOptions.addClickHandler( new PlayOptionsListener( viewManager, getCache() ) );
		playOptions.setTitle( "play audio options" );
		
		table.setWidget( 1, ++column, new Label( "Buy / Listen" ) );
		table.setWidget( 2, column, playOptions );
		
		table.setWidget( 1, ++column, createActionHeader() );
		table.getFlexCellFormatter().setRowSpan( 1, column, 2 );
		table.getFlexCellFormatter().setVerticalAlignment( 1, column, 
										HasVerticalAlignment.ALIGN_TOP );
		
		table.setWidget( 1, ++column, new Label( "Rating" ) );
		table.setWidget( 2, --column, new HTML( "&nbsp;" ) );
		
		table.getRowFormatter().setStyleName( 1, "headerRow" );
		table.getRowFormatter().setStyleName( 2, "headerRow" );		
		table.getRowFormatter().setVerticalAlign( 1, HasVerticalAlignment.ALIGN_BOTTOM );
		table.getRowFormatter().setVerticalAlign( 2, HasVerticalAlignment.ALIGN_TOP );
	}

	
	protected ListenBuyPanel createListenBuyPanel( ReleaseDetail rd ) {
		return new ListenBuyPanelRecommended( viewManager, rd );
	}
	
	
	protected Widget createHelpInfo()
	{
		String text1 = "<p><br>"
					+ "<center><b>You have no recommended releases.</b></center>"
					+ "<p>"
					+ "Recommendations are based on the trax you have rated and added "
					+ "to your wishlist, virtual carts, and purchase history.  To get "
					+ "more recommendations, rate some trax and/or add trax to your wishlist "
					+ "or virtual carts."
					+ "<p>"
					+ "The more you use Choicetrax the more we will be able to recommend "
					+ "to you!";
		
		
		/*String text2 = "<p><br><p>"
					+ "<b>Tips on searching:</b>"
					+ "<p>"
					+ "Don't use accented characters."
					+ "<p><br>"
					+ "You can use the - operator for exclusion. "
					+ "<p>"
					+ "Example: <i>search for this -\"not this\"</i>"
					+ "<p><br>"
					+ "Use quotes when the artist name includes a hyphen. "
					+ "<p>"
					+ "Example: <i>\"Hipp-E\"</i>"
					+ "<p><br>"
					+ "You can use the | operator for OR searches. "
					+ "<p>"
					+ "Example: <i>artist | track | label</i>"
					+ "<p><br>";*/
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( new HTML( text1 ) );
		
		//vp.add( new HTML( text2 ) );
		
		return vp;
	}

}
