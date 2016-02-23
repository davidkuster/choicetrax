package com.choicetrax.client.input;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.choicetrax.client.data.TrackComponent;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class SearchHyperlink extends TrackHyperlink 
{
	

	public SearchHyperlink( TrackComponent tc, ChoicetraxViewManager manager )
	{
		super();
		
		if ( ( tc instanceof Genre )
			&& ( ((Genre) tc).getShortName() != null ) )
		{
			Genre genre = (Genre) tc;
			this.setText( genre.getGenreShortName() );
			this.setTitle( genre.getGenreName() );
		}
		else
			this.setText( tc.getName() );

		this.setWordWrap( true );
		
		this.trackComponent = tc;
		this.viewManager = manager;
		this.addClickHandler( new SearchHyperlinkListener() );
		this.setStyleName( "labelLink" );
	}
	
	
	
	private class SearchHyperlinkListener
		implements ClickHandler
	{
		public void onClick( ClickEvent event )
		{
			// TODO: passing null to onClick() might be bad.  check this.
			(new SearchListener()).onClick( null );
		}
	}

}
