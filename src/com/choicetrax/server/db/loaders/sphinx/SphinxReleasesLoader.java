package com.choicetrax.server.db.loaders.sphinx;

import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.util.exception.ChoicetraxException;

import com.choicetrax.server.util.jdbc.ResourceManager;


public class SphinxReleasesLoader extends SphinxLoader
{

	
	public SphinxReleasesLoader( String sortType, String sortOrder ) 
		throws ChoicetraxException
	{
		super();
		
		this.setIndex( ResourceManager.getSphinxIndexTracks() );
		
		this.setSortMode( createSortMode( sortType, sortOrder ) );
	}
	
	

	/*
	 	public static final int SORT_BY_DEFAULT					= 0;
		public static final int RELEASES_SORT_BY_ARTIST			= 1;
		public static final int RELEASES_SORT_BY_TRACK			= 2;
		public static final int RELEASES_SORT_BY_MIX			= 3;
		public static final int RELEASES_SORT_BY_LABEL			= 4;
		public static final int RELEASES_SORT_BY_DATE			= 5;
		public static final int RELEASES_SORT_BY_RATING			= 6;
	 */
	private String createSortMode( String sortType, String sortOrder )
		throws ChoicetraxException
	{
		String defaultSort = "LoadDate DESC, ReleaseDate DESC, AlbumID DESC ";
							//+ "ArtistSort, TrackSort"; 
		
		if ( Constants.SORT_BY_DEFAULT.equals( sortType ) )
		{
			return defaultSort;
		}
		else if ( Constants.RELEASES_SORT_BY_ARTIST.equalsIgnoreCase( sortType ) )
		{
			return "ArtistSort " + getSortOrder( sortOrder ) + ", " + defaultSort;
		}
		else if ( Constants.RELEASES_SORT_BY_DATE.equalsIgnoreCase( sortType ) )
		{
			return "ReleaseDate " + getSortOrder( sortOrder ) + ", LoadDate DESC, AlbumID DESC";
		}
		else if ( Constants.RELEASES_SORT_BY_LABEL.equalsIgnoreCase( sortType ) )
		{
			return "LabelSort " + getSortOrder( sortOrder ) + ", " + defaultSort;
		}
		else if ( Constants.RELEASES_SORT_BY_MIX.equalsIgnoreCase( sortType ) )
		{
			return "MixSort " + getSortOrder( sortOrder ) + ", " + defaultSort;
		}
		else if ( Constants.RELEASES_SORT_BY_RATING.equalsIgnoreCase( sortType ) )
		{
			return "TrackRatingOverall " + getSortOrder( sortOrder ) + ", " + defaultSort;
		}
		else if ( Constants.RELEASES_SORT_BY_TRACK.equalsIgnoreCase( sortType ) )
		{
			return "TrackSort " + getSortOrder( sortOrder ) + ", " + defaultSort;
		}
		else
			throw new ChoicetraxException( "SphinxReleasesLoader init - unknown sort type: " 
											+ sortType );
	}
	
	
	private String getSortOrder( String sortOrder )
		throws ChoicetraxException
	{
		if ( Constants.SORT_ORDER_DESCENDING.equalsIgnoreCase( sortOrder ) )
			return "DESC";
		else if ( Constants.SORT_ORDER_ASCENDING.equalsIgnoreCase( sortOrder ) )
			return "ASC";
		else 
			throw new ChoicetraxException( "Unknown sort order type: " + sortOrder );
	}
	
	
}
