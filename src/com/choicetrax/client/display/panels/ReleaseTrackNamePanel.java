package com.choicetrax.client.display.panels;

import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.input.TrackHyperlink;
import com.choicetrax.client.data.Artist;


public class ReleaseTrackNamePanel extends Composite 
{
	private ChoicetraxViewManager viewManager = null;
	
	private FlowPanel fp = null;
	
	
	public ReleaseTrackNamePanel( ReleaseDetail rd, ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
		
		fp = new FlowPanel();
		initWidget( fp );
		
		populateFlowPanel( rd );
	}
	
	
	public void add( Widget w ) {
		fp.add( w );
	}
		

	private void populateFlowPanel( ReleaseDetail rd )
	{
		fp.add( new TrackHyperlink( rd.getArtist(), viewManager ) );
		
		Artist vocalist = rd.getVocalist();
		if ( ( vocalist != null ) && ( vocalist.getArtistID() > 0 ) )
		{
			fp.add( new Label( " feat. " ) );
			fp.add( new TrackHyperlink( vocalist, viewManager ) );
		}
			
		fp.add( new Label( " - " + rd.getTrackName() ) );
		
		//fp.add( new ReleaseMixNamePanel( viewManager, rd ) );
		addMixNameDetails( fp, rd );
		
		// maybe make time italicized or another color?
		String time = rd.getTimeLength();
		if ( ( time != null ) 
			&& ( ! time.trim().equals("") )
			&& ( ! time.equals( "null" ) ) )
		{
			Label timeLabel = new Label( " (" + time + ")" );
			timeLabel.setStylePrimaryName( "smallItalicText" );
			fp.add( timeLabel );
		}
	}
	
	
	private void addMixNameDetails( FlowPanel mixPanel, ReleaseDetail rd )
	{
		String mixName = rd.getMixName();
		if ( ( mixName != null ) && ( ! mixName.trim().equals( "" ) ) )
		{
			Artist remixer = rd.getRemixer();
			if ( ( remixer != null ) && ( remixer.getArtistID() > 0 ) )
			{
				String remixerName = remixer.getArtistName();
				
				//Label remixerLabel = new TrackHyperlink( remixer, viewManager );
				Label remixerLabel = null;
				// don't enable hyperlinks for non-legit artist names
				if ( "Y".equals( remixer.getLegitName() ) )
					remixerLabel = new TrackHyperlink( remixer, viewManager );
				else
					remixerLabel = new Label( remixerName );
				
				// remixer name is in the mix name, so turn 
				// remixer name into a TrackHyperlink
				int index = mixName.toLowerCase().indexOf( remixerName.toLowerCase() );
				if ( index != -1 )
				{
					mixPanel.add( new Label( " (" ) );
					mixPanel.add( new Label( mixName.substring( 0, index ) ) );
					mixPanel.add( remixerLabel );
					mixPanel.add( new Label( mixName.substring( index + remixerName.length(), 
												mixName.length() ) ) );
					mixPanel.add( new Label( ") " ) );
				}
				else
				{
					mixPanel.add( new Label( " (" + mixName + ")" ) );
					mixPanel.add( new Label( " [" ) );
					mixPanel.add( remixerLabel );
					mixPanel.add( new Label( "] " ) );
				}
			}
			else
				// no remixer artist
				mixPanel.add( new Label( " (" + mixName + ") " ) );
		}
	}
	
}
