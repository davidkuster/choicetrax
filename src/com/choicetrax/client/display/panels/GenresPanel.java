package com.choicetrax.client.display.panels;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class GenresPanel extends Composite
{
	
	private ChoicetraxViewManager viewManager = null;
	
	
	public GenresPanel( ChoicetraxViewManager manager, List<Genre> genreList ) 
	{
		this.viewManager = manager;
		
		SimplePanel sp = new SimplePanel();
		initWidget( sp );
		
		sp.add( createGenrePanel( genreList ) );	
	}
	
	
	private Widget createGenrePanel( List<Genre> genres )
	{
		FlowPanel genrePanel = new FlowPanel();

		if ( genres != null )
		{
			Iterator<Genre> genreIterator = genres.iterator(); 
			for ( int x=0; genreIterator.hasNext(); x++ )
			{
				Genre genre = genreIterator.next();
				
				if ( x > 0 ) genrePanel.add( new Label( ", " ) );
				
				// populate genre names if needed
				//
				// (think about doing this when the Releases are
				// loaded instead of when they are shown and if the
				// resulting code complexity is worth any possible
				// performance enhancements...)
				//
				String shortName = genre.getGenreShortName();
				String genreName = genre.getGenreName();
				if ( shortName == null )
				{
					ChoicetraxConfigData configData = viewManager.getConfigData();
					if ( configData != null ) 
					{
						Genre g = configData.getGenre( genre.getGenreID() );
						if ( g != null ) {
							shortName = g.getGenreShortName();
							genreName = g.getGenreName();
						}
					}
				}
				
				Label genreLabel = new Label( shortName );
				genreLabel.setTitle( genreName );
				
				genrePanel.add( genreLabel );
			}
		}
		
		if ( genrePanel.getWidgetCount() == 0 )
			genrePanel.add( new HTML( "&nbsp;" ) );
		
		return genrePanel;
	}

}
