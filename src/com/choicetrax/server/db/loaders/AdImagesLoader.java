package com.choicetrax.server.db.loaders;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;

import com.choicetrax.client.data.Genre;
import com.choicetrax.client.data.config.AdImage;
import com.choicetrax.client.data.config.AdImageQueue;
import com.choicetrax.client.data.config.AdImages;
import com.choicetrax.client.util.exception.ChoicetraxException;
import com.choicetrax.server.util.jdbc.DBResource;
import com.choicetrax.server.util.jdbc.ResourceManager;
import com.choicetrax.client.constants.Constants;


public class AdImagesLoader 
{
	
	private enum AdType {
		PAID, FREE
	}
	
	

	public AdImages loadAdImagesData( List<Genre> genreList )
		throws ChoicetraxException 
	{
		AdImages adImages = new AdImages();
		
		AdImageQueue bannerAds = loadBannerAds();
		adImages.setBannerAds( bannerAds );
		
		AdImageQueue topPaidAds = loadTopPaidTargetedAds();
		adImages.setTopPaidTargetedAds( topPaidAds );
		
		AdImage topBannerAd = loadTopPaidBannerAd();
		adImages.setTopPaidBannerAd( topBannerAd );
		
		Iterator<Genre> i = genreList.iterator();
		while ( i.hasNext() ) 
		{
			Genre genre = i.next();
			int genreID = genre.getGenreID();
			
			AdImageQueue paidGenreAds = loadTargetedAds( genreID, AdType.PAID );
			
			// if no ads for a genre, don't put it in load
			if ( paidGenreAds.size() > 0 )
				adImages.setPaidTargetedAds( genreID, paidGenreAds );
			
			AdImageQueue freeGenreAds = loadTargetedAds( genreID, AdType.FREE );
			
			if ( freeGenreAds.size() > 0 )
				adImages.setFreeTargetedAds( genreID, freeGenreAds );
		}
	
		return adImages;
	}
	
	
	
	private AdImageQueue loadBannerAds()
		throws ChoicetraxException
	{
		String methodName = "loadBannerAds()";
		
		String sql = "select a.AdID, a.ImageURL, a.LinkURL, a.AdType "
					+ "from Ads a "
					+ "where a.AdStatus in ( 'Currently Active', 'Non-Expiring' ) "
					+	"and a.AdType = '" + Constants.AD_TYPE_TOP_BANNER + "' "
					+ "order by a.AdStatus, a.AdValuePrice desc, rand()";
		
		DBResource dbHandle = null;
		
		AdImageQueue bannerAds = new AdImageQueue();
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
						
			ResultSet rs = dbHandle.executeQuery( sql );
			while ( rs.next() )
			{
				AdImage adImage = new AdImage();
				
				int adID 	 	= rs.getInt( 	 "AdID" 	);
				String imageURL	= rs.getString( "ImageURL" 	);
				String linkURL	= rs.getString( "LinkURL" 	);
				String adType	= rs.getString( "AdType" 	);
				
				adImage.setAdID( adID );
				adImage.setImageURL( imageURL );
				adImage.setLinkURL( linkURL );
				adImage.setAdType( adType );
				
				bannerAds.add( adImage );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error loading banner ads: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
		
		return bannerAds;
	}
	
	
	private AdImageQueue loadTargetedAds( int genreID, AdType freeOrPaid )
		throws ChoicetraxException
	{
		String methodName = "loadTargetedAds()";
		
		String sql = "select a.AdID, a.ImageURL, a.LinkURL, a.AdType "
					+ "from Ads a "
					+ "where a.AdStatus in ( 'Currently Active', 'Non-Expiring' ) "
					+	"and a.AdType = '" + Constants.AD_TYPE_LEFT_SMALL + "' "
					+	"and a.StartDate <= current_date() "
					+	"and a.EndDate >= current_date() "
					+	"and ( a.TargetGenres = " + genreID + " "
					+		"or a.TargetGenres like '%, " + genreID + "' "
					+		"or a.TargetGenres like '" + genreID + ", %' "
					+		"or a.TargetGenres like '%, " + genreID + ", %' ) ";
		
		if ( freeOrPaid == AdType.PAID )
			sql += "and a.AdValuePrice > 0 ";
		else if ( freeOrPaid == AdType.FREE )
			sql += "and a.AdValuePrice = 0 ";
		
		sql += "order by a.AdStatus, a.AdValuePrice desc, rand()";
		// TODO: implement a M:M AdGenres table
		
		AdImageQueue targetedAds = new AdImageQueue();
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
						
			ResultSet rs = dbHandle.executeQuery( sql );
			while ( rs.next() )
			{
				AdImage adImage = new AdImage();
				
				int adID 	 	= rs.getInt( 	 "AdID" 	);
				String imageURL	= rs.getString( "ImageURL" 	);
				String linkURL	= rs.getString( "LinkURL" 	);
				String adType	= rs.getString( "AdType" 	);
				
				adImage.setAdID( adID );
				adImage.setImageURL( imageURL );
				adImage.setLinkURL( linkURL );
				adImage.setAdType( adType );
				
				targetedAds.add( adImage );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error loading targeted ads: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
		
		return targetedAds;
	}
	
	
	
	private AdImageQueue loadTopPaidTargetedAds()
		throws ChoicetraxException
	{
		String methodName = "loadTopPaidTargetedAds()";
		
		String sql = "select a.AdID, a.ImageURL, a.LinkURL, a.AdType "
					+ "from Ads a "
					+ "where a.AdStatus in ( 'Currently Active', 'Non-Expiring' ) "
					+	"and a.AdType = '" + Constants.AD_TYPE_LEFT_SMALL + "' "
					+	"and a.StartDate <= current_date() "
					+	"and a.EndDate >= current_date() "
					+ "order by a.AdStatus, a.AdValuePrice desc, rand() "
					+ "limit 4";
		
		AdImageQueue targetedAds = new AdImageQueue();
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
						
			ResultSet rs = dbHandle.executeQuery( sql );
			while ( rs.next() )
			{
				AdImage adImage = new AdImage();
				
				int adID 	 	= rs.getInt( 	 "AdID" 	);
				String imageURL	= rs.getString( "ImageURL" 	);
				String linkURL	= rs.getString( "LinkURL" 	);
				String adType	= rs.getString( "AdType" 	);
				
				adImage.setAdID( adID );
				adImage.setImageURL( imageURL );
				adImage.setLinkURL( linkURL );
				adImage.setAdType( adType );
				
				targetedAds.add( adImage );
			}
			rs.close();
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error loading top paid targeted ads: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
		
		return targetedAds;
	}
	
	
	private AdImage loadTopPaidBannerAd()
		throws ChoicetraxException
	{
		String methodName = "loadTopPaidBannerAd()";
		
		String sql = "select a.AdID, a.ImageURL, a.LinkURL, a.AdType "
					+ "from Ads a "
					+ "where a.AdStatus in ( 'Currently Active', 'Non-Expiring' ) "
					+	"and a.AdType = '" + Constants.AD_TYPE_TOP_BANNER + "' "
					+	"and a.StartDate <= current_date() "
					+	"and a.EndDate >= current_date() "
					+ "order by a.AdStatus, a.AdValuePrice desc, rand() "
					+ "limit 1";
		
		AdImage topBanner = null;
		
		DBResource dbHandle = null;
		
		try
		{
			dbHandle = ResourceManager.getDBConnection();
						
			ResultSet rs = dbHandle.executeQuery( sql );
			if ( rs.next() )
			{
				AdImage adImage = new AdImage();
				
				int adID 	 	= rs.getInt( 	 "AdID" 	);
				String imageURL	= rs.getString( "ImageURL" 	);
				String linkURL	= rs.getString( "LinkURL" 	);
				String adType	= rs.getString( "AdType" 	);
				
				adImage.setAdID( adID );
				adImage.setImageURL( imageURL );
				adImage.setLinkURL( linkURL );
				adImage.setAdType( adType );
				
				topBanner = adImage;
			}
			rs.close();
		}
		catch ( ChoicetraxException cte )
		{
			throw cte;
		}
		catch ( Throwable t )
		{
			throw new ChoicetraxException( "Error loading top paid banner ad: " + t,
											this.getClass().getName() + "." + methodName );
		}
		finally
		{
			if ( dbHandle != null )
				dbHandle.close();
		}
		
		return topBanner;
	}

}
