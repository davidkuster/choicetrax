package com.choicetrax.client.display.panels;

import java.util.Iterator;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.PartnerInventory;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class SharingPanel extends Composite
{
	
	private ChoicetraxViewManager viewManager = null;
	private ReleaseDetail rd = null;
	
	
	public SharingPanel( ChoicetraxViewManager manager, ReleaseDetail rd ) 
	{
		super();
		
		this.viewManager = manager;
		this.rd = rd;
		
		SimplePanel sp = new SimplePanel();
		initWidget( sp );
		
		sp.add( createSharingDisplaySmall() );
	}
	
	
	
	/*private Widget createSharingDisplay()
	{
		String longURL = "http://www.choicetrax.com/#id=" + rd.getTrackID();
		String shortURL = "http://choicetrax.com/#id=" + rd.getTrackID();
		String title = createTitleText();
				
		// twitter
		String twitterURL = createTwitterURL( title, shortURL );
		String twitter = 
			"<a href=" +  twitterURL + " target=_new >" 
			+ 	"<img src=img/layout/sharing/twitter16.gif />"
			+ "</a> "
			+ "<a href=" + twitterURL + " target=_new >"
			+	"Twitter"
			+ "</a>";
						
		// email
		/*String email = 
			"<a href=" + createEmailURL( title, longURL ) 
			+ " target=_new >"
			+	"<img src=img/layout/sharing/iconEmail.gif />Email"
			+ "</a>"; * /
			
		// facebook
		String facebookURL = createFacebookURL( title, longURL );
		String facebook =
			"<a href=" +  facebookURL + " target=_new >"
			+	"<img src=img/layout/sharing/facebook16.gif />"
			+ "</a> "
			+ "<a href=" + facebookURL + " target=_new >"
			+	"Facebook"
			+ "</a>";
			
		// myspace
		String myspaceURL = createMyspaceURL( title, longURL );
		String myspace = 
			"<a href=" + myspaceURL + " target=_new >"
			+	"<img src=img/layout/sharing/myspace4.png />"
			+ "</a> "
			+ "<a href=" + myspaceURL + " target=_new >"
			+ 	"Myspace"
			+ "</a>";
			
		// favorites
		/*String favorites =
			"<a href=" + createSharingURL( url, title, "favorites" ) 
			+ " target=_new >"
			+	"<img src=img/layout/sharing/iconFavorites.gif />Favorites"
			+ "</a>";* /
			
		// purehousemusic
		String phmURL = createPhmURL( longURL );
		String phm =
			"<a href=" + phmURL + " target=_new >"
			+	"<img src=img/layout/sharing/iconPHM.gif />"
			+ "</a> "
			+ "<a href=" + phmURL + " target=_new >"
			+	"PHM"
			+ "</a>";
					
		// share
		String share =
			"<a href=http://www.addthis.com/bookmark.php?v=250 " 
			+ 	"onmouseover=\"return addthis_open(this, '', '" + shortURL + "', '" + escape( title ) + "');\" " 
			+ 	"onmouseout=\"addthis_close();\" "
			+ 	"onclick=\"return addthis_sendto();\""
			+ " target=_new >"
			+ "<img src=http://s7.addthis.com/static/btn/sm-share-en.gif border=0 alt=Share />"
			+ "</a>";
		
		// button order: twitter, email, facebook, myspace, favorites, PHM, separator, more
		FlexTable table = new FlexTable();
		table.setCellPadding( 4 );
		
		table.setWidget( 0, 0, new HTML( twitter ) );
		table.setWidget( 0, 1, new HTML( facebook ) );
		table.setWidget( 0, 2, new HTML( myspace ) );
		//table.setWidget( 0, 3, new HTML( phm ) );
		//table.setWidget( 0, 4, new HTML( email ) );
		//table.setWidget( 1, 2, new HTML( favorites ) );
		table.setWidget( 1, 0, new HTML( share ) );
		table.getFlexCellFormatter().setColSpan( 1, 0, 4 );
		table.getFlexCellFormatter().setHorizontalAlignment( 1, 0, 
										HasHorizontalAlignment.ALIGN_CENTER );
		
		table.getFlexCellFormatter().setVerticalAlignment( 0, 0, 
				HasVerticalAlignment.ALIGN_MIDDLE );
		table.getFlexCellFormatter().setVerticalAlignment( 0, 1, 
				HasVerticalAlignment.ALIGN_MIDDLE );
		table.getFlexCellFormatter().setVerticalAlignment( 0, 2, 
				HasVerticalAlignment.ALIGN_MIDDLE );
		table.getFlexCellFormatter().setVerticalAlignment( 0, 3, 
				HasVerticalAlignment.ALIGN_MIDDLE );
		table.getFlexCellFormatter().setVerticalAlignment( 1, 0, 
				HasVerticalAlignment.ALIGN_MIDDLE );
		
		return table;
	}*/
	
	
	private Widget createSharingDisplaySmall()
	{
		String longURL = "http://www.choicetrax.com/#id=" + rd.getTrackID();
		String shortURL = "http://choicetrax.com/#id=" + rd.getTrackID();
		String title = createTitleText();
				
		// twitter
		String twitterURL = createTwitterURL( title, shortURL );
		String twitter = 
			"<a href=" +  twitterURL + " target=_new >" 
			+ 	"<img src=img/layout/sharing/twitter16.gif alt=\"Share via Twitter\" />"
			+ "</a> ";
						
		// email
		String email = 
			"<a href=" + createEmailURL( title, longURL ) + " >" // target=_new >"
			+	"<img src=img/layout/sharing/iconEmail.gif alt=\"Share via Email\" />"
			+ "</a>";
			
		// facebook
		String facebookURL = createFacebookURL( title, longURL );
		String facebook =
			"<a href=" +  facebookURL + " target=_new >"
			+	"<img src=img/layout/sharing/facebook16.gif alt=\"Share via Facebook\" />"
			+ "</a> ";
			
		// myspace
		String myspaceURL = createMyspaceURL( title, longURL );
		String myspace = 
			"<a href=" + myspaceURL + " target=_new >"
			+	"<img src=img/layout/sharing/myspace4.png alt=\"Share via Myspace\" />"
			+ "</a> ";
			
		// favorites
		/*String favorites =
			"<a href=" + createSharingURL( url, title, "favorites" ) 
			+ " target=_new >"
			+	"<img src=img/layout/sharing/iconFavorites.gif />Favorites"
			+ "</a>";*/
			
		// purehousemusic
		String phmURL = createPhmURL( longURL );
		String phm =
			"<a href=" + phmURL + " target=_new >"
			+	"<img src=img/layout/sharing/iconPHM.gif alt=\"Share via PureHouseMusic.net\" />"
			+ "</a> ";
		
		// funkyhousebeats
		String fhbURL = createFhbURL( longURL );
		String fhb =
			"<a href= " + fhbURL + " target=_new >"
			+	"<img src=img/layout/sharing/iconFHB.gif alt=\"Share via FunkyHouseBeats.com\" />"
			+ "</a> ";
					
		// share
		/*String share =
			"<script language=javascript type=text/javascript> "
			+ "SHARETHIS.addEntry({ "
			+ "title:'Share me', "
			+ "summary:'Sharing is good for the soul.', "
			+ "icon: 'http://sharethis.com/images/share-icon-16x16.png' "
			+ "}, {button:true} );"
			+ "</script>";
			/*"<a href=http://www.addthis.com/bookmark.php?v=250&pub=choicetrax " 
			+ 	"onmouseover=\"return addthis_open(this, '', '" + shortURL + "', '" + escape( title ) + "');\" " 
			+ 	"onmouseout=\"addthis_close();\" "
			+ 	"onclick=\"return addthis_sendto();\" "
			+ "target=_new >"
			//+ "<img src=http://s7.addthis.com/static/btn/sm-share-en.gif border=0 alt=Share />"
			+ "<img src=img/layout/sharing/addThis.gif alt=\"Share via AddThis.com\" />"
			+ "</a>";*/
		// http://s7.addthis.com/static/t00/logo100100.png
		
		// button order: twitter, email, facebook, myspace, favorites, PHM, separator, more
		FlexTable table = new FlexTable();
		table.setCellPadding( 4 );
		
		int column = 0;
		table.setWidget( 0, column++, new HTML( twitter ) );
		table.setWidget( 0, column++, new HTML( facebook ) );
		table.setWidget( 0, column++, new HTML( myspace ) );
		table.setWidget( 0, column++, new HTML( phm ) );
		table.setWidget( 0, column++, new HTML( fhb ) );
		table.setWidget( 0, column++, new HTML( email ) );
		//table.setWidget( 1, column++, new HTML( favorites ) );
		//table.setWidget( 0, column++, new HTML( share ) );
		
		for ( int col=0; col <= 5; col++ ) {
			table.getFlexCellFormatter().setVerticalAlignment( 0, col, 
					HasVerticalAlignment.ALIGN_MIDDLE );
		}
		
		return table;
	}
	
	
	
	/*private String createSharingURL( String url, String title, String service )
	{
		return "http://www.addthis.com/bookmark.php?v=250&pub=choicetrax"
				+ "&s=" + service 
				+ "&title=" + escape( title )
				+ "&url=" + url;
	}*/
	
	
	
	private String escape( String s )
	{
		if ( ( s == null ) || ( s.trim().equals( "" ) ) )
			return "";
		else
			return URL.encodeComponent( s ); 
			//.replace( " ", spaces ) ).replace( "&", "%26" ).replace( "\"", "%22" );
	}
	
	private String escape( String s, String spaces ) {
		return escape( s.replace( " ", spaces ) );
	}
	
	
	
	private String createPhmURL( String longURL ) 
	{
		String artistName = escape( rd.getArtist().getArtistName(), "_" );		
		String epTitle = escape( rd.getAlbumName(), "_" );
		int numTracks = 1;
		String trackName = escape( rd.getTrackName(), "_" );
		String mixName = escape( rd.getMixName(), "_" );
		String audioURL = getPhmAudioURL();
		String labelName = escape( rd.getLabel().getLabelName(), "_" );
		String labelImage = rd.getCoverArtURL();
		String returnURL = escape( longURL );
		
		String url = "http://www.purehousemusic.net/index.php"
					+ "?action=addtrack"
					+ "&page=addcttrack"
					+ "&source=CT" 
					+ "&artist=" + artistName
			        + "&eptitle=" + epTitle
			        + "&numberoftracks=" + numTracks
			        + "&trackname=" + trackName	// trackname1, trackname2 if sending > 1
			        + "&sample=" + audioURL 	// sample1, sample2 if sending > 1
			        + "&label=" + labelName
			        + "&labelimage=" + labelImage
			        + "&ctURL=" + returnURL;
		
		if ( ! mixName.equals( "" ) ) 
			url += "&mixname=" + mixName;
		
		return url;
	}
	
	
	private String createFhbURL( String longURL ) 
	{
		String artistName = escape( rd.getArtist().getArtistName() ); //, "_" );		
		//String epTitle = escape( rd.getAlbumName(), "_" );
		//int numTracks = 1;
		String trackName = escape( rd.getTrackName() ); //, "_" );
		String mixName = escape( rd.getMixName() ); //, "_" );
		
		// reuse PHM audio url logic
		String audioURL = getPhmAudioURL();
		
		String labelName = escape( rd.getLabel().getLabelName() ); //, "_" );
		String labelImage = rd.getCoverArtURL();
		String returnURL = escape( longURL );
		
		String url = "http://www.funkyhousebeats.com/index.php"
					+ "?option=com_chronocontact"
					+ "&Itemid=154"
					+ "&chronoformname=add_choicetrax_track"
					//+ "&page=addcttrack"
					//+ "&source=CT" 
					+ "&artist=" + artistName
			        //+ "&eptitle=" + epTitle
			        //+ "&numberoftracks=" + numTracks
			        + "&track_name=" + trackName
			        + "&audio_clip=" + audioURL
			        + "&label=" + labelName
			        + "&labelimage=" + labelImage
			        + "&buy_here=" + returnURL;
		
		if ( ! mixName.equals( "" ) ) 
			url += "&mix_name=" + mixName;
		
		return url;
	}
	
	
	
	/*
	 * stompy is first choice, traxsource is last choice
	 */
	private String getPhmAudioURL()
	{
		int preferredPartnerID = 0;
		int partnerID = 0;
		
		Iterator<PartnerInventory> i = rd.getPartnerAvailability().iterator();
		while ( i.hasNext() ) 
		{
			PartnerInventory pi = i.next();
			int pID = pi.getPartnerID();
			String partnerName = viewManager.getConfigData().lookupPartnerName( pID );
			
			if ( Constants.PARTNER_NAME_STOMPY.equals( partnerName ) ) {
				preferredPartnerID = pID;
				break;
			}
			else if ( ! Constants.PARTNER_NAME_TRAXSOURCE.equals( partnerName ) )
				partnerID = pID;
			else
				preferredPartnerID = pID;
		}
		
		if ( preferredPartnerID != 0 )
			partnerID = preferredPartnerID;
		
		return rd.getPartnerInventory( partnerID ).getAudioPreviewURL();
	}
	
		
	
	private String createTitleText()
	{
		StringBuffer text = new StringBuffer();
		
		text.append( rd.getArtist().getArtistName() );
		text.append( " - " );
		text.append( rd.getTrackName() );
		text.append( " " );
						
		// add in mix name if it's there
		String mixName = rd.getMixName();
		if ( ( mixName != null ) && ( ! mixName.trim().equals( "" ) ) ) 
			text.append( "(" + mixName + ") " );

		text.append( "[" + rd.getLabel().getLabelName() + "]" );
		
		return text.toString();
	}
	
	
	
	private String createFacebookURL( String title, String url )
	{
		/* URL=http://www.facebook.com/sharer.php
	?u=http%3A%2F%2Fchoicetrax.com%2F
	&t=Andrew+Phelan+%26+Origami+-+Beyond+The+Line+%28Christian+Malloni+Remix%29+%5BEIGHT-TRACKS%5D
	
	?u=http://choicetrax.com/
	&t=Andrew Phelan & Origami - Beyond The Line (Christian Malloni Remix) [EIGHT-TRACKS]
	(decoded)
		 */
		return "http://www.facebook.com/sharer.php"
				+ "?u=" + escape( url )
				+ "&t=" + escape( title );
	}
	
	
	private String createMyspaceURL( String title, String url )
	{
		/*URL=http://www.myspace.com/Modules/PostTo/Pages/
			?u=http%3A%2F%2Fchoicetrax.com%2F
			&t=Andrew+Phelan+%26+Origami+-+Beyond+The+Line+%28Christian+Malloni+Remix%29+%5BEIGHT-TRACKS%5D
			&c=%3Ca+href%3D%22http%3A%2F%2Fchoicetrax.com%2F%22%3E
				Andrew+Phelan+%26+Origami+-+Beyond+The+Line+%28Christian+Malloni+Remix%29+%5BEIGHT-TRACKS%5D
				%3C%2Fa%3E%0A%3Cp%3E+%3C
				%2Fp%3E%0AShared+via+%3Ca+href%3D%22http%3A%2F%2Faddthis.com%22%3EAddThis%3C%2Fa%3E%0A
				
			?u=http://choicetrax.com/
			&t=Andrew Phelan & Origami - Beyond The Line (Christian Malloni Remix) [EIGHT-TRACKS]
			&c=<a href="http://choicetrax.com/">
				Andrew Phelan & Origami - Beyond The Line (Christian Malloni Remix) [EIGHT-TRACKS]</a>
			<p> </p>
			Shared via <a href="http://addthis.com">AddThis</a>
			(decoded)*/

		String formattedTitle = formatTextLength( title, 90 );
		String content = "<a href=" + url + ">" + title + "</a>"
						+ "<p><br>";
		
		String escapedURL = escape( url );
		String escapedTitle = escape( formattedTitle );
		String escapedContent = escape( content );
		
		return "http://www.myspace.com/index.cfm?fuseaction=postto"
				+ "&u=" + escapedURL
				+ "&t=" + escapedTitle
				+ "&c=" + escapedContent
				+ "&l=" + 2; // bulletin
	}
	
	
	private String createEmailURL( String title, String url )
	{
		String subject = "Track suggestion from Choicetrax.com".replace( " ", "%20" );
		String body = "%0A%0A" + escape( title, "_" ) + "%0A" + escape( url );
		
		return "mailto:recipient?subject=" + subject + "&body=" + body;
	}
	
	
	private String createTwitterURL( String title, String url )
	{
		String end = " " + url + " @choicetrax";
		String text = title + end; 
		
		if ( text.length() > 140 )
			text = formatTextLength( title, 140 - end.length() ) + end;
		
		return "http://twitter.com/home"
				//?status=http%3A%2F%2Fbit.ly%2FHIkrH+via+%40addthis
				+ "?status=" + escape( text ); 
	}
	
	
	// make sure text is <= length characters
	private String formatTextLength( String s, int length )
	{
		String text = s;
		
		while ( text.length() > length ) {
			// keep shortening title text until overall length is <= 140
			s = s.substring( 0, s.length() - 1 );
			text = s + "...";
		}
		
		return text;
	}

}
