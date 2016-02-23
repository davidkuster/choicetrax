package com.choicetrax.client.display;

import java.util.List;
import java.util.Iterator;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Random;

import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.config.AdFeaturedRelease;
import com.choicetrax.client.data.config.AdFeaturedReleases;
import com.choicetrax.client.data.config.ChoicetraxConfigData;
import com.choicetrax.client.display.panels.decorators.BoxDecoratorPanel;
import com.choicetrax.client.Choicetrax;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.input.HyperlinkIconCoverArt;
import com.choicetrax.client.input.TrackHyperlink;


public class FeaturedReleasesComposite 
	extends Composite 
{
	
	private ChoicetraxViewManager viewManager = null;
	private AdFeaturedReleases featuredReleases = null;
	
	private HorizontalPanel tab = null;
	private HorizontalPanel hp = null;
	private ListBox listBox = null;

	
	public FeaturedReleasesComposite( ChoicetraxViewManager manager )
	{
		this.viewManager = manager;
				
		tab = new HorizontalPanel();
		tab.setVerticalAlignment( HasVerticalAlignment.ALIGN_BOTTOM );
		tab.add( new Image( Constants.IMAGE_BUNDLE_TABS.featuredreleases_on() ) );
		tab.add( new HTML( "&nbsp;" ) );
				
		VerticalPanel vp = new VerticalPanel();
		initWidget( vp );
		
		TabBar tb = new TabBar();
		tb.addTab( tab );
		tb.selectTab( 0 );
		
		vp.add( tb );
		vp.add( new BoxDecoratorPanel( createFeaturedDisplay() ) );
		
		vp.setStyleName( "featuredReleasesTabPanel" );
	}
	
	
	private Panel createFeaturedDisplay()
	{
		hp = new HorizontalPanel();
		hp.add( new LoadingComposite() );
		
		ScrollPanel sp = new ScrollPanel( hp );
		sp.setAlwaysShowScrollBars( false );
		sp.setStyleName( "featuredReleasesScrollPanel" );
		
		return sp;
	}
	
	
	public void initialize()
	{	
		ChoicetraxConfigData configData = viewManager.getConfigData();
		featuredReleases = viewManager.getFeaturedReleases();
		
		listBox = new ListBox( false );
		listBox.setVisibleItemCount( 1 );
		
		List<Genre> genreList = configData.getChoicetraxGenres();
		if ( genreList != null ) 
		{			
			Iterator<Genre> i = genreList.iterator();
			while ( i.hasNext() ) {
				Genre genre = i.next();
				if ( featuredReleases.getReleasesForGenre( genre.getGenreID() ) != null )
					listBox.addItem( genre.getGenreShortName(), genre.getGenreID() + "" );
			}
		}
		
		tab.add( listBox );
		

		// check for existence of last selected genre cookie
		String cookieID = Choicetrax.getCookie( Constants.COOKIE_FEATURED_GENRE_ID );
		
		String genreID = null;
		int index = 0;
		
		if ( cookieID != null ) 
		{
			genreID = cookieID;
			
			for ( int i=0; i < listBox.getItemCount(); i++ ) {
				String value = listBox.getValue( i );
				if ( value.equals( genreID ) ) {
					index = i;
					break;
				}
			}
		}
		else 
		{ 
			// if it doesn't exist, randomly select one from the list		
			int size = listBox.getItemCount();
			index = Random.nextInt( size );
			genreID = listBox.getValue( index );
		}
		
		updateDisplay( Integer.parseInt( genreID ) );
		listBox.setSelectedIndex( index );
		
		listBox.addChangeHandler( new GenreSelectListener() );
	}
		
	
	
	private void updateDisplay( int genreID )
	{
		hp.clear();
		
		hp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
		
		List<AdFeaturedRelease> featuredList = featuredReleases.getReleasesForGenre( genreID );
		if ( featuredList == null ) return;
		
		Iterator<AdFeaturedRelease> i = featuredList.iterator();
		while ( i.hasNext() )
		{
			AdFeaturedRelease featuredRelease = i.next();
			ReleaseDetail rd = featuredRelease.getReleaseDetail();
			
			HyperlinkIconCoverArt img = new HyperlinkIconCoverArt( viewManager, rd, 30 );
			
			TrackHyperlink artistName = new TrackHyperlink( rd.getArtist(), viewManager );			
			Label albumName = new Label( rd.getAlbumName() );
			TrackHyperlink labelName = new TrackHyperlink( rd.getLabel(), viewManager );
			
			artistName.setWordWrap( false );
			//albumName.setWordWrap( false );
			labelName.setWordWrap( false );
			
			VerticalPanel vp = new VerticalPanel();
			vp.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_CENTER );
			vp.setStyleName( "featuredReleasePanel" );
			vp.add( artistName );
			vp.add( albumName );
			vp.add( labelName );
			
			HorizontalPanel featuredPanel = new HorizontalPanel();
			featuredPanel.add( img );
			featuredPanel.add( vp );
			
			hp.add( featuredPanel );
		}
	}
	
	
	
	private class GenreSelectListener implements ChangeHandler
	{
		public void onChange( ChangeEvent sender )
		{
			String selectedID = listBox.getValue( listBox.getSelectedIndex() );
			if ( selectedID != null ) 
			{
				updateDisplay( Integer.parseInt( selectedID ) );
				Choicetrax.setCookie( Constants.COOKIE_FEATURED_GENRE_ID, 
										selectedID,
										Constants.COOKIE_DURATION_2WEEKS );
			}
			
			listBox.setFocus( false );
		}
	}

}
