package com.choicetrax.client.actions.loaderactions;

import com.choicetrax.client.actions.LoaderAction;

/**
 * @author David Kuster
 * dave@djtalldave.com
 * 
 * Design has changed since this was created - my thinking now
 * is that this class will be used to retrieve and view the
 * user's Purchase History, Rated tracks, Charts, etc...
 * 
 *
 * Created December 12, 2007
 */
public class ViewAction 
	implements LoaderAction
{
	
    private String viewRequestType;
    private int userID;
    private int partnerID;
    private java.util.Date dateRangeBegin;
    private java.util.Date dateRangeEnd;
    private int chartID;
    private int genreID;
    private int beginIndex;
    
    
    public ViewAction() {
    	super();
    }
    
    
    public String getLogString() {
		return "ViewAction";
	}
    
    
	public int getBeginIndex() {
		return beginIndex;
	}
	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}
	public int getGenreID() {
		return genreID;
	}
	public void setGenreID(int genreID) {
		this.genreID = genreID;
	}
	public String getViewRequestType() {
		return viewRequestType;
	}
	public void setViewRequestType(String viewRequestType) {
		this.viewRequestType = viewRequestType;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getPartnerID() {
		return partnerID;
	}
	public void setPartnerID(int partnerID) {
		this.partnerID = partnerID;
	}
	public java.util.Date getDateRangeBegin() {
		return dateRangeBegin;
	}
	public void setDateRangeBegin(java.util.Date dateRangeBegin) {
		this.dateRangeBegin = dateRangeBegin;
	}
	public java.util.Date getDateRangeEnd() {
		return dateRangeEnd;
	}
	public void setDateRangeEnd(java.util.Date dateRangeEnd) {
		this.dateRangeEnd = dateRangeEnd;
	}
	public int getChartID() {
		return chartID;
	}
	public void setChartID(int chartID) {
		this.chartID = chartID;
	}

}