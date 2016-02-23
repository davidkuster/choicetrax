package com.choicetrax.server.db.handlers;

import com.choicetrax.client.actions.HandlerAction;
import com.choicetrax.client.actions.handleractions.FavoritesAction;
import com.choicetrax.client.data.*;
import com.choicetrax.server.db.handlers.BaseActionHandler;


public class FavoritesActionHandler 
	extends BaseActionHandler
{
	private TrackComponent favorite = null;
	private int userID = -1;
	
	public FavoritesActionHandler( HandlerAction action )
	{
		super( action );
		
		FavoritesAction favAction = (FavoritesAction) action;
		this.favorite = favAction.getFavorite();
		this.userID = favAction.getUserID();
	}

	protected String createRemoveSQL()
	{
		String sql = null;
		
		if ( favorite instanceof Artist )
			sql = "update UserFavoriteArtists "
				+ "set DateRemoved = sysdate() "
				+ "where UserID = " + this.userID + " "
				+	"and ArtistID = " + this.favorite.getID();
		else if ( favorite instanceof RecordLabel )
			sql = "update UserFavoriteLabels "
				+ "set DateRemoved = sysdate() "
				+ "where UserID = " + this.userID + " "
				+	"and LabelID = " + this.favorite.getID();
		else if ( favorite instanceof Genre )
			sql = "update UserFavoriteGenres "
				+ "set DateRemoved = sysdate() "
				+ "where UserID = " + this.userID + " "
				+	"and GenreID = " + this.favorite.getID();
		
		return sql;
	}
	
	
	protected String createAddSQL()
	{
		String sql = null;
		
		if ( favorite instanceof Artist )
			sql = "insert into UserFavoriteArtists ( UserID, ArtistID, DateAdded, DateRemoved ) "
				+ "values ( " + this.userID + ", " + this.favorite.getID() + ", sysdate(), null ) "
				+ "on duplicate key update DateAdded = sysdate(), DateRemoved = null ";
		else if ( favorite instanceof RecordLabel )
			sql = "insert into UserFavoriteLabels ( UserID, LabelID, DateAdded, DateRemoved ) "
				+ "values ( " + this.userID + ", " + this.favorite.getID() + ", sysdate(), null ) "
				+ "on duplicate key update DateAdded = sysdate(), DateRemoved = null ";
		else if ( favorite instanceof Genre )
			sql = "insert into UserFavoriteGenres ( UserID, GenreID, DateAdded, DateRemoved ) "
				+ "values ( " + this.userID + ", " + this.favorite.getID() + ", sysdate(), null ) "
				+ "on duplicate key update DateAdded = sysdate(), DateRemoved = null ";
		
		return sql;
	}

}