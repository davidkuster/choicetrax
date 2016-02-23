package com.choicetrax.client.display;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Partner;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.display.panels.FeedbackPanel;
import com.choicetrax.client.display.panels.decorators.BoxTabPanel;
import com.choicetrax.client.input.ClickableImageOnOff;
import com.choicetrax.client.input.HyperlinkLabel;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class AboutUsComposite extends Composite 
{
	private ChoicetraxViewManager viewManager = null;
	
	private BoxTabPanel tp = new BoxTabPanel();
	private VerticalPanel partnerPanel = new VerticalPanel();
	private FeedbackPanel feedbackPanel = null;
	
	
	
	public AboutUsComposite( ChoicetraxViewManager manager )
	{
		viewManager = manager;
		
		feedbackPanel = new FeedbackPanel( viewManager );
		
		initWidget( tp );
		tp.getDeckPanel().setAnimationEnabled( true );
		
		tp.add( createPartnerPanel(), 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.partner_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.partner_off() ),
						ClickableImageOnOff.DEFAULT_ON ) );
		tp.add( createInstructionsPanel(), 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.help_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.help_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		tp.add( createFeedbackPanel(), 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.feedback_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.feedback_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		tp.add( createAboutUsPanel(), 
				new ClickableImageOnOff( 
						new Image( Constants.IMAGE_BUNDLE_TABS.about_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.about_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		
		tp.add( createAdvertisePanel(),
				new ClickableImageOnOff(
						new Image( Constants.IMAGE_BUNDLE_TABS.advertise_on() ),
						new Image( Constants.IMAGE_BUNDLE_TABS.advertise_off() ),
						ClickableImageOnOff.DEFAULT_OFF ) );
		
		tp.selectTab( 0 );
		tp.setStylePrimaryName( "aboutUsPanel" );
	}
	
		
	private Panel createFeedbackPanel()
	{
		HTML html = new HTML( "Choicetrax is a new site, and as "
							+ "such is constantly undergoing "
							+ "renovation and development.  The "
							+ "most valuable feedback we can get "
							+ "is the stuff that comes from you - "
							+ "the user.  Please let us know your thoughts, "
							+ "questions, suggestions, problems, or ideas.  "
							+ "We would love to hear from you!"
							+ "<p>" );
				
		VerticalPanel vp = new VerticalPanel();
		vp.add( html );
		vp.add( feedbackPanel );
		
		ScrollPanel sp = new ScrollPanel();
		sp.add( vp );
		sp.setStyleName( "aboutUsPanel" );
		
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add( sp );
		
		return vp1;
	}
	
	private Panel createPartnerPanel()
	{
		HTML html = new HTML( 
			/*"This is where we will list all our partners "
			+ "with external links to their webpages. "
			+ "We may also have other links here to the "
			+ "News/Intro page, legal/privacy/terms of "
			+ "service details, a feedback link, perhaps "
			+ "something about how Choicetrax was started "
			+ "and by whom...etc etc."
			+ "<p>"*/
			
			"We are partnering with these sites currently, and plan to add more "
			+ "as time permits.  If there are certain download stores that you'd "
			+ "like to see added to Choicetrax, please let us know! "
			+ "<p>" );
		
		partnerPanel.add( html );

		ScrollPanel sp = new ScrollPanel();
		sp.add( partnerPanel );
		sp.setStyleName( "aboutUsPanel" );
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( sp );
		
		return vp;
	}
	
	
	private Panel createInstructionsPanel()
	{
		String text =
			"Having trouble figuring something out?  You're in the right place.  "
			+ "Here are some tips, tricks, and general instructions that should get "
			+ "you on your feet. "
			+ "<p><br>"
			+ "<center><h3>Concept</h3></center> "
			+ "Think of Choicetrax as a search engine on steroids.  When you enter "
			+ "a search term, we are simultaneously searching dozens of dance music "
			+ "download sites to bring you results.  You can listen to audio clips "
			+ "and add tracks into a shopping cart on your choice of store, all while "
			+ "comparing prices, quality, exclusivity, and a host of other features. "
			+ "<p><br>"
			+ "<center><h3>Join</h3></center> "
			+ "In order to enjoy all of Choicetrax's features and personalize your "
			+ "experience, we recommend you create a user account.  Just click the \"create "
			+ "account\" link on the top left of your screen and you will be guided "
			+ "through the simple process.  Membership to Choicetrax is free. "
			+ "<p><br>"
			+ "<center><h3>Search</h3></center>"
			+ "The search area toward the top of your screen is equipped to search on a "
			+ "number of different parameters - artist, title, label, genre, release date "
			+ "(start and end), and partner site.  You could search for all the house music "
			+ "that came out between April and May of 2004, or you could do a search to find "
			+ "out which download stores are carrying the newest Ministry Of Sound release.  "
			+ "The possibilities are endless.  If you know exactly what you want but aren't "
			+ "sure which store has it yet, enter in all the release details in the appropriate "
			+ "boxes and we'll lead you straight to it!"
			+ "<p><br>"
			+ "<center><h3>Listen / Buy</h3></center>"
			+ "When your search results appear you have a number of new options, including "
			+ "buying and listening to tracks.  Depending on the availability of the tracks, "
			+ "you may have as many as 20+ stores to choose from. All of your options are under "
			+ "the \"Listen / Buy\" heading, represented by icons for the stores that stock "
			+ "each track.  To the right of that you might see a + sign and a number - this "
			+ "means there are more stores carrying the track, in addition to the ones whose "
			+ "icons you already see.  Click the + sign to see all of your options.  Keep in "
			+ "mind that in order to listen to an audio clip you must choose which store's clip "
			+ "you'd like to listen to, which can be a very useful shopping tool as each store "
			+ "has different quality settings and uses different sections of the track. "
			+ "<p><br>"
			+ "<center><h3>Genres</h3></center>"
			+ "Each download store categorizes music differently.  This means that a track on "
			+ "Choicetrax could potentially show up in multiple genres during a search.  "
			+ "When looking at your search results, you'll notice that the \"genres\" tab can "
			+ "be clicked - this will display all of the other genres (if any) that this "
			+ "particular song is listed in on other partner sites. "
			+ "<p><br>"
			+ "<center><h3>Favorites</h3></center>"
			+ "One of the most useful features of Choicetrax is the ability to add things to "
			+ "your \"favorites.\"  You can add an artist, a label, or even a favorite search. "
			+ "Simply click on the artist name, label name, or the \"save search\" button in "
			+ "the search area.  With a favorite search, you can come back at a later date and "
			+ "easily perform the search again to see if there are any new results";
		
		HTML html = new HTML( text );
		
		Label test = new HyperlinkLabel( ".", new ClickHandler() {
			public void onClick( ClickEvent event ) {
				viewManager.handleError( new Exception( "test error message" ),
								this.getClass().getName() + ".createInstructionsPanel()" );
			}
		});
		
		FlowPanel fp = new FlowPanel();
		fp.add( html );
		fp.add( test );
	
		ScrollPanel sp = new ScrollPanel();
		sp.add( fp );
		sp.setStyleName( "aboutUsPanel" );
		
		// workaround necessary to get scrollpanel to scroll
		VerticalPanel vp = new VerticalPanel();
		vp.add( sp );

		return vp;
	}
	
	private Panel createAboutUsPanel()
	{
		HTML html = new HTML( 
					"Choicetrax was formed as a response to "
					+ "the growing number of download sites for DJ's.  The idea was simple "
					+ "- to make shopping for new music easier.  With each site offering "
					+ "unique and exclusive content the shopping experience can be overwhelming, "
					+ "and the shopper misses out on good music.  Choicetrax eliminates "
					+ "this problem.  With one simple search for your favorite artist or "
					+ "label, all of their music is available at your fingertips. "
					+ "<p>" );
		
		HTML html2 = new HTML( "Website hosting by <br>"
					+ "<a href=http://www.networkmedicsmn.com target=_blank>"
					+	"<img src=img/layout/networkmedics.png></a>"
					+ "<p>" );
		
		HTML html3 = new HTML( "BPM calculation by <br>"
					+ "<a href=http://www.echonest.com target=_blank>" 
					+	"<img src=img/layout/echonest2.jpg></a>" 
					+ "<p>" );
		
		VerticalPanel vp = new VerticalPanel();
		vp.add( html );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add( html2 );
		hp.add( new HTML( "&nbsp; &nbsp;" ) );
		//hp.add( html3 );
		
		vp.add( hp );
					
		
		ScrollPanel sp = new ScrollPanel();
		sp.add( vp );
		sp.setStyleName( "aboutUsPanel" );
		
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add( sp );
		
		return vp1;
	}
	
	
	private Panel createAdvertisePanel()
	{
		VerticalPanel vp = new VerticalPanel();
		
		vp.add( new HTML( 
				"We have automated the advertising on our site through our own "
				+ "easy to use Self-Service Ad Management system. "
				+ "<p>" ) );
		
		vp.add( new HTML(
				"Simply sign up for an advertiser account, select one of two ad types "
				+ "to create (top banner or left-side square ad), upload your ad image, "
				+ "select your target genres (square ads only), set your own price, "
				+ "and pay via PayPal. "
				+ "<p>" ) );
		
		vp.add( new HTML( 
				"Also, did we mention - record labels get free ads! "
				+ "<p>" ) );
		
		vp.add( new HTML( 
				"Access the Self-Service Ad Management system at: "
				+ "<a href=http://www.choicetrax.com/ads target=_new>"
				+ "http://www.choicetrax.com/ads</a>"
				+ "<p>" ) );

		ScrollPanel sp = new ScrollPanel();
		sp.add( vp );
		sp.setStyleName( "aboutUsPanel" );
		
		VerticalPanel vp1 = new VerticalPanel();
		vp1.add( sp );
		
		return vp1;
	}
	
	
	public void initialize() {
		initializePartnerPanel();
	}
	
	
	private void initializePartnerPanel()
	{
		ChoicetraxConfigData configData = viewManager.getConfigData();
		List<Partner> partnerList = configData.getChoicetraxPartners();
		
		FlowPanel fp = new FlowPanel();
		fp.setWidth( "100%" );
		
		Iterator<Partner> i = partnerList.iterator();
		while ( i.hasNext() )
		{
			Partner partner = i.next();
			String partnerName 	= partner.getPartnerName();
			String partnerUrl	= partner.getPartnerWebURL();
			String iconUrl		= partner.getPartnerIconFilename();
			
			// think about putting partner window name on the database & data obj !!!
			String windowName	= Constants.getPartnerWindowName( partnerName );
			
			HTML link = new HTML( "<a href=" + partnerUrl 
									+ " target=" + windowName + ">" 
									+ partnerName + "</a>" );
			Image icon = new Image( "img/buyicons/" + iconUrl );
			
			HorizontalPanel hp = new HorizontalPanel();
			hp.setStylePrimaryName( "aboutUsPartnerPanel" );
			hp.setVerticalAlignment( HasVerticalAlignment.ALIGN_MIDDLE );
			hp.add( icon );
			hp.add( link );
			hp.setCellWidth( icon, "30px" );
			hp.setCellWidth( link, "90px" );			
			
			fp.add( hp );
		}
		
		partnerPanel.add( fp );
		
		partnerPanel.add( new HTML ( 
			"<p><br>"
			+ "<i>Note: Click on the partner icon in the search results above to "
			+ "see formats and prices for a track and to add "
			+ "that track to your Choicetrax virtual cart.</i>" ) );
	}
	
}
