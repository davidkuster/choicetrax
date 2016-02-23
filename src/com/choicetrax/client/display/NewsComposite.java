package com.choicetrax.client.display;

import com.google.gwt.user.client.ui.*;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class NewsComposite extends Composite
{
	private ChoicetraxViewManager viewManager = null;
	
	
	public NewsComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		VerticalPanel vp = new VerticalPanel();
		initWidget( vp );
		
		String title = "<center><h3>Welcome to Choicetrax.com!</h3></center>";
		
		vp.add( new HTML( title ) );
		
		String news = "<h3>News</h3>"
					+ "We are now out of beta!  Check out our recent updates: "
					+ "<ul>"
					+ "<li><b>DJDownload</b> has been added to our list of partner stores! "
					+ "<li><b>New audio player</b> - featuring waveform display. " // and BPM."
					+ "<li><b>Recommendations</b> - recommended tracks and recommended charts. "
					+	"These are based on what tracks you've rated, added to your wishlist, "
					+	"virtual carts, or purchase history.  If you want more recommendations "
					+	"rate some of your favorite tracks and see what recommendations pop up. "
					+	"(The recommendations links are in the right panel under the playlist.) "
					+ "<li><b>Direct Track Links</b> - direct links to every track at our "
					+	"partner stores.  Especially useful if you're only looking for a "
					+	"specific track or two, this will let you go directly to that release "
					+ 	"at the partner store and skip our checkout/transfer process. "
					+ 	"Or use it to only buy a couple tracks out of your mammoth virtual cart "
					+	"instead of transferring all the tracks to the partner store. "
					+	"(The direct links are in the prices and formats popups, after you click "
					+	"on a store icon.) "
					+ "<li><b>Duplicate elimination</b> - if the same track is available at "
					+	"multiple stores it is now more likely to be combined into one "
					+ 	"search result row, leading to fewer pages of tracks to sort through."
					+ "<li><b>Advertising</b> - free for record labels!  See the Advertising "
					+	"tab at the bottom of the page for details."
					+ "<li><b>Bug Fixes</b> and lots of other behind the scenes work you will "
					+	"hopefully never be bothered by.  :) "
					+ "</ul>"
					+ "<b>Update!</b> We just got word from our good friends at Primal Records "
					+ "that to help us celebrate our one year anniversary and site relaunch "
					+ "they're offering our users a 20% discount for one month!  Just enter "
					+ "code \"choicetrax20\" when checking out from Primal. "
					+ "<p>";
		
		vp.add( new HTML( news ) );
					
		String intro = "<h3>Intro</h3>"
					+ "Shopping for new music just got a whole lot easier. "
					+ "Choicetrax is a new breed of website in the growing arena of "
					+ "downloadable dance music. " //Unlike any other service in existence, "
					+ "Choicetrax allows the user to search the databases of multiple "
					+ "download sites simultaneously.  Users have the ability to search, "
					+ "browse, listen, and add tracks to shopping carts on many different "
					+ "sites, all from one location. Check out the \"Help\" section below "
					+ "for more details."
					+ "<p>"
					+ "We welcome your comments, questions, and feedback, "
					+ "and will make every effort possible to send you a speedy reply.  "
					+ "See the \"Feedback/Contact\" tab at the bottom of the page to "
					+ "send us an email. "
					+ "<p>";
		
					//It consolidates the entire digital "
					//+ "dance music world into one easy-to-use interface. "
					//+ "<p>";
		
		vp.add( new HTML( intro ) );
		
		String videoText = "<h3>Video Tutorial</h3>"
					+ "We've made a short video to detail some of the cool features offered "
					+ "by Choicetrax.  Please click on the image below to watch and find "
					+ "out how we can help you find great music!  (View in full screen "
					+ "HD for the best experience.)"
					+ "<p>";
		
		vp.add( new HTML( videoText ) );
		
		vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		
		HTML link = new HTML( "<a href=\"http://www.youtube.com/watch?v=rVc8BkVmHzE\" "
				+	"target=_new>"
				+ 	"http://www.youtube.com/watch?v=rVc8BkVmHzE"
				+ "</a>" );
		
		HTML embed = new HTML( "<object width=425 height=344>"
					+ "<param name=movie value=http://www.youtube.com/v/rVc8BkVmHzE&hl=en&fs=1></param>"
					+ "<param name=allowFullScreen value=true></param>"
					+ "<param name=allowscriptaccess value=always></param>"
					+ "<embed src=http://www.youtube.com/v/rVc8BkVmHzE&hl=en&fs=1 " 
					+ "type=application/x-shockwave-flash allowscriptaccess=always allowfullscreen=true " 
					+ "width=425 height=344></embed>"
					+ "</object>" );

		vp.add( embed );
		vp.add( link );
		vp.add( new HTML( "<p><br>" ) );
		
		/*HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		hp.add( new HTML( "<br> Top Charts <br> <i>Coming soon!</i><p>" ) );
		hp.add( new HTML( "<br> Top Rated Tracks <br> <i>Coming soon!</i><p>" ) );
		hp.setWidth( "100%" );
		
		vp.add( hp );*/
	}

}
