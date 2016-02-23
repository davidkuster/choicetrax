package com.choicetrax.client.display;

import java.util.List;

import com.google.gwt.user.client.ui.*;

import com.choicetrax.client.actions.responses.LoaderResponse;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.data.Artist;
import com.choicetrax.client.data.DJChartDetail;
import com.choicetrax.client.data.DJCharts;
import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.data.cache.DJChartsCache;
import com.choicetrax.client.display.panels.GenresPanel;
import com.choicetrax.client.display.panels.ListenBuyPanel;
import com.choicetrax.client.input.TrackHyperlink;
import com.choicetrax.client.input.listeners.DJChartImageLoadListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class DJChartReleasesComposite 
	extends AbstractReleasesComposite
{
	
	private DJChartDetail chartDetail = null;
	
	
	public DJChartReleasesComposite( ChoicetraxViewManager manager )
	{
		super( manager, Constants.PANEL_DJ_CHARTS_VIEW );
	}
	
	
	public void updateDisplay( LoaderResponse response )
	{
		// read DJChart details out of response obj
		DJChartsCache cache = (DJChartsCache) response;
		DJCharts charts = cache.getChartsForPage( 1 );
		DJChartDetail chart = charts.getChartsList().get( 0 );
		
		this.chartDetail = chart;
		
		// then pass tracks data to AbstractReleasesComposite
		super.updateDisplay( chart.getChartTracks() );
	}
	
	
	protected Label createActionHeader() {
		return new Label( "Wish" );
	}
	
	protected ListenBuyPanel createListenBuyPanel( ReleaseDetail rd ) {		
		return new ListenBuyPanel( viewManager, rd );
	}
	
	
	protected Widget createHeaderWidget( int cacheType )
	{
		Artist artistObj = chartDetail.getArtist();
		String artistName = chartDetail.getArtistName();
		String chartName = chartDetail.getChartName();
		String chartDate = chartDetail.getChartDate();
		String imageURL = chartDetail.getImageURL();
		int partnerID = chartDetail.getPartnerID();
		String partnerName = viewManager.getConfigData().lookupPartnerName( partnerID );
		List<Genre> genres = chartDetail.getGenres();
		
		Label artistLabel = null;
		if ( artistObj != null ) 
			artistLabel = new TrackHyperlink( artistObj, viewManager );
		else if ( artistName != null )
			artistLabel = new Label( artistName );
		
		Image img = new Image( imageURL );
		img.setSize( "100px", "100px" );
		
		DJChartImageLoadListener handler = new DJChartImageLoadListener();
		img.addLoadHandler( handler );
		img.addErrorHandler( handler );
		
		Label partnerLabel = new Label( partnerName + " chart" );
		partnerLabel.setStylePrimaryName( "mainPanelHeaderLabel" );
		
		HTML chartLabel = new HTML( chartName + " / " + chartDate );
		chartLabel.setStylePrimaryName( "chartHeaderText" );
				
		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing( 5 );
		
		vp.add( partnerLabel );
		
		if ( artistLabel != null )
			vp.add( artistLabel );
				
		vp.add( chartLabel );
		vp.add( new GenresPanel( viewManager, genres ) );
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add( img );
		hp.add( new HTML( "&nbsp;" ) );
		hp.add( vp );
		
		return hp;
	}

	
}
