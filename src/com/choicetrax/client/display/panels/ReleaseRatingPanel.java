package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.HyperlinkImage;
import com.choicetrax.client.input.HyperlinkLabel;


public class ReleaseRatingPanel 
	extends Composite 
{
	
	private static String NON_HIGHLIGHT_URL 	= "img/layout/stars/iconStarGrey.gif";
	private static String HIGHLIGHT_URL 		= "img/layout/stars/iconStarWhite.gif";
	private static String USER_RATING_URL		= "img/layout/stars/iconStarBlue.gif";
	private static String OVERALL_RATING_URL	= "img/layout/stars/iconStar.gif";
	private static String OVERALL_HALF_URL		= "img/layout/stars/iconStarHalf.gif";
	
	private ChoicetraxViewManager viewManager = null;
	protected ReleaseDetail rd = null;
	
	protected SimplePanel userRatingPanel = new SimplePanel();
	private PopupPanel popup = null;
	
	private Image star1 = null;
	private Image star2 = null;
	private Image star3 = null;
	private Image star4 = null;
	private Image star5 = null;
	
	
	public ReleaseRatingPanel( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		this.viewManager = manager;
		this.rd = rd;
		
		initPanel();
	}
	
	
	protected void initPanel()
	{
		VerticalPanel ratingPanel = new VerticalPanel();
		initWidget( ratingPanel );
			
		ratingPanel.setStylePrimaryName( "ratingPanel" );
		
		Widget overallRating = createOverallRating( rd.getTrackRating() );
		
		ratingPanel.add( overallRating );
		ratingPanel.setCellHorizontalAlignment( overallRating, HasHorizontalAlignment.ALIGN_CENTER );
		
		updateRatingDisplay( rd.getUserRating() );
		
		ratingPanel.add( userRatingPanel );
		ratingPanel.setCellHorizontalAlignment( userRatingPanel, HasHorizontalAlignment.ALIGN_CENTER );
	}
	
	
	private Widget createOverallRating( float overallRating )
	{
		Widget w = null;
		
		if ( overallRating == 0 ) {
			w = new Label( rd.getTrackRating() + "" );
		}
		else {
			HorizontalPanel hp = new HorizontalPanel();
			int x = 0;
			int floor = (int) overallRating;
			while ( x < floor ) {
				hp.add( new Image( OVERALL_RATING_URL ) );
				x++;
			}
			
			if ( overallRating >= floor + 0.5 ) 
				hp.add( new Image( OVERALL_HALF_URL ) );
			
			w = hp;
		}
		
		w.setTitle( overallRating + " overall rating w/ " 
					+ rd.getNumTrackRatings() + " total ratings" );
		
		return w;
	}
	
	
	protected void updateRatingDisplay( int rating )
	{
		if ( rating == 0 ) 
		{
			userRatingPanel.setWidget( new HyperlinkLabel( "Rate", 
										"Rate this release", 
										new AddRatingListener() ) );
		}
		else 
		{
			HorizontalPanel hp = new HorizontalPanel();
			for ( int x=0; x < rating; x++ ) {
				hp.add( new Image( USER_RATING_URL ) );
			}
			hp.setTitle( "Change your rating" );
			
			FocusPanel fp = new FocusPanel( hp );
			fp.addClickHandler( new AddRatingListener() );
			fp.setStylePrimaryName( "clickableImage" );
						
			userRatingPanel.setWidget( fp );
		}
	}
	
	
	
	protected class AddRatingListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			if ( viewManager.getCurrentUser() == null )
				com.google.gwt.user.client.Window.alert( "You must be logged in to use this feature." );
			else
			{
				popup = new PopupPanel( true );
				popup.setAnimationEnabled( true );
				
				star1 = new HyperlinkImage( NON_HIGHLIGHT_URL, new SaveRatingListener( 1 ) );
				star2 = new HyperlinkImage( NON_HIGHLIGHT_URL, new SaveRatingListener( 2 ) );
				star3 = new HyperlinkImage( NON_HIGHLIGHT_URL, new SaveRatingListener( 3 ) );
				star4 = new HyperlinkImage( NON_HIGHLIGHT_URL, new SaveRatingListener( 4 ) );
				star5 = new HyperlinkImage( NON_HIGHLIGHT_URL, new SaveRatingListener( 5 ) );
				
				// TODO: i think this could be combined into one handler. check it out.
				StarMouseListener one = new StarMouseListener( 1 );
				StarMouseListener two = new StarMouseListener( 2 );
				StarMouseListener three = new StarMouseListener( 3 );
				StarMouseListener four = new StarMouseListener( 4 );
				StarMouseListener five = new StarMouseListener( 5 );
				
				star1.addMouseOverHandler( one );
				star1.addMouseOutHandler( one );
				
				star2.addMouseOverHandler( two );
				star2.addMouseOutHandler( two );
				
				star3.addMouseOverHandler( three );
				star3.addMouseOutHandler( three );
				
				star4.addMouseOverHandler( four );
				star4.addMouseOutHandler( four );
				
				star5.addMouseOverHandler( five );
				star5.addMouseOutHandler( five );
				
				HorizontalPanel hp = new HorizontalPanel();
				hp.setSpacing( 0 );
				
				hp.add( star1 );
				hp.add( star2 );
				hp.add( star3 );
				hp.add( star4 );
				hp.add( star5 );
				
				Widget sender = (Widget) event.getSource();
				final int senderLeft	= sender.getAbsoluteLeft();
				final int senderWidth 	= sender.getOffsetWidth();
				final int senderTop		= sender.getAbsoluteTop();
				final int senderHeight	= sender.getOffsetHeight();
				
				popup.add( hp );
				popup.setStyleName("popupPanel");
				popup.setPopupPositionAndShow( new PopupPanel.PositionCallback() {
					public void setPosition( int offsetWidth, int offsetHeight ) {
						int left = senderLeft + ( senderWidth / 2 ) - ( offsetWidth / 2 );
						int top = senderTop + senderHeight;
						popup.setPopupPosition( left, top );
					}
				});
				
				// center popup on rating HyperlinkLabel
			}
		}
	}
	
	
	protected class SaveRatingListener
		implements ClickHandler
	{
		private int rating;
		
		public SaveRatingListener( int rating )
		{
			this.rating = rating;
		}
		
		public void onClick( ClickEvent event )
		{
			popup.hide();
			
			if ( viewManager.getCurrentUser() != null )
				updateRatingDisplay( rating );
			
			viewManager.getUserManager().addRating( rd, rating );
		}
	}
	
	
	protected class StarMouseListener
		implements MouseOverHandler, MouseOutHandler
	{
		private int starNum = 0;
		
		public StarMouseListener( int num ) {
			this.starNum = num;
		}
		
		public void onMouseOver( MouseOverEvent event ) 
		{
			if ( starNum >= 1 )
				star1.setUrl( HIGHLIGHT_URL );
			if ( starNum >= 2 )
				star2.setUrl( HIGHLIGHT_URL );
			if ( starNum >= 3 )
				star3.setUrl( HIGHLIGHT_URL );
			if ( starNum >= 4 )
				star4.setUrl( HIGHLIGHT_URL );
			if ( starNum == 5 )
				star5.setUrl( HIGHLIGHT_URL );
		}

		public void onMouseOut( MouseOutEvent event ) 
		{
			star1.setUrl( NON_HIGHLIGHT_URL );
			star2.setUrl( NON_HIGHLIGHT_URL );
			star3.setUrl( NON_HIGHLIGHT_URL );
			star4.setUrl( NON_HIGHLIGHT_URL );
			star5.setUrl( NON_HIGHLIGHT_URL );
		}
	}
	
}
