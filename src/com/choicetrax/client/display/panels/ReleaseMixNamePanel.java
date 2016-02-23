package com.choicetrax.client.display.panels;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

import com.choicetrax.client.data.Artist;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.TrackHyperlink;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class ReleaseMixNamePanel extends Composite
{
	
	private ChoicetraxViewManager viewManager = null;
	
	
	public ReleaseMixNamePanel( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		this.viewManager = manager;
		
		SimplePanel sp = new SimplePanel();
		initWidget( sp );
		
		sp.add( createMixNamePanel( rd ) );
	}
		
	
	
	private FlowPanel createMixNamePanel( ReleaseDetail rd )
	{
		FlowPanel mixPanel = new FlowPanel();
		
		String mixName = rd.getMixName();
		if ( ( mixName != null ) && ( ! mixName.trim().equals( "" ) ) )
		{
			Artist remixer = rd.getRemixer();
			if ( ( remixer != null ) && ( remixer.getArtistID() > 0 ) )
			{
				String remixerName = remixer.getArtistName();
				
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
					mixPanel.add( new Label( "(" ) );
					mixPanel.add( new Label( mixName.substring( 0, index ) ) );
					mixPanel.add( remixerLabel );
					mixPanel.add( new Label( mixName.substring( index + remixerName.length(), 
												mixName.length() ) ) );
					mixPanel.add( new Label( ")" ) );
				}
				else
				{
					mixPanel.add( new Label( "(" + mixName + ")" ) );
					mixPanel.add( new Label( " [" ) );
					mixPanel.add( remixerLabel );
					mixPanel.add( new Label( "]" ) );
				}
			}
			else
				// no remixer artist
				mixPanel.add( new Label( "(" + mixName + ")" ) );
		}
		else
			// no remix
			mixPanel.add( new HTML( "&nbsp;" ) );
		
		return mixPanel;
	}

}
