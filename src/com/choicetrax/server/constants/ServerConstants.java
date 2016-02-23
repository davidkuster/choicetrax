package com.choicetrax.server.constants;

import java.text.*;
import java.util.StringTokenizer;

import com.choicetrax.client.actions.UserIpAction;


public class ServerConstants 
{
	
	// line separator
	public static final String SEPARATOR = System.getProperty( "line.separator" );
	
	// date formatters
	public static final SimpleDateFormat DATE_FORMAT 		= new SimpleDateFormat( "MM/dd/yyyy" );
	public static final SimpleDateFormat DATE_FULL_FORMAT	= new SimpleDateFormat( "MM/dd/yyyy hh:mm:ss" );
	public static final SimpleDateFormat DATE_DB_FORMAT		= new SimpleDateFormat( "yyyy-MM-dd" );
	public static final SimpleDateFormat SPHINX_FORMATTER	= new SimpleDateFormat( "yyyyMMdd" );
	public static final SimpleDateFormat EMAIL_FULL_FORMAT	= new SimpleDateFormat( "EEEE, MMMM dd, yyyy" );
	public static final SimpleDateFormat EMAIL_MONTH_DAY	= new SimpleDateFormat( "MMM dd" );
	public static final SimpleDateFormat EMAIL_YEAR			= new SimpleDateFormat( "yyyy" );
	
	// search tracking queue names
	public static final String TRACKING_QUEUE_ARTIST	= "ARTIST";
	public static final String TRACKING_QUEUE_LABEL		= "LABEL";
	public static final String TRACKING_QUEUE_GENRE		= "GENRE";
	
	
	private static final String FILTER_PATTERN 			= "[<>{}\\[\\];\\=]";
	private static final String FILTER_PATTERN_MIN 		= "[<>{}\\[\\];]";
	
	private static final String SPHINX_FILTER_PATTERN 	= "[\r\n!°©<>∏§ªø@]"; 
														//"[\r\n!°©()<>∏§ªø]";
														//"[\r\n!°‚√©()<>ƒ∏]";
	
	
	
	/**
	 * escapes accented characters from international users
	 * 
	 * @param s
	 * @return
	 */
	private static String escapeAccentedChars( String s )
	{
		s = s.replaceAll( "[ËÈÍÎ»… À]", "e" );
	    s = s.replaceAll( "[˚˘€Ÿ]", "u" );
	    s = s.replaceAll( "[ÔÓœŒÌ]", "i" );
	    s = s.replaceAll( "[‡‚¿¬ƒ√]", "a" );
	    s = s.replaceAll( "[‘¯ˆ]", "o" );
	    
	    return s;
	}
	
	
	/**
	 * removes/escapes invalid data to protect from SQL injection
	 * 
	 * @param s
	 * @return
	 */
	public static String escapeChars( String s )
	{
		if ( s == null ) return "";
		
		s = s.replaceAll( FILTER_PATTERN, "" );
		s = s.replace( "--", "" );
		s = s.replace( "\\", "" );
		s = s.replace( "'" , "\\'" );
		//s = s.replace( "select", "" );
		s = s.replace( "insert", "" );
		s = s.replace( "update", "" );
		//s = s.replace( "delete", "" );
		//s = s.replace( "drop", "" );
		
		return s;
	}
	
	
	/**
	 * used by BatchDatabaseLoader when inserting/updating 
	 * audio clip and cover art URLs.
	 * 
	 * @param s
	 * @return
	 */
	public static String escapeCharsMin( String s )
	{
		if ( s == null ) return "";
		
		s = s.replaceAll( FILTER_PATTERN_MIN, "" );
		s = s.replace( "'" , "\\'" );
		
		return s;
	}
	
	
	/**
	 * used to compare what's in the database against what was loaded
	 * from a partner site as part of the batch staging process.
	 * kinda ghetto, but the single quote thing is screwing it up 
	 * when using the escapeChars() method above.
	 * 
	 * @param s
	 * @return
	 */
	public static String escapeCharsBatch( String s )
	{
		if ( s == null ) return "";
		
		s = s.replaceAll( FILTER_PATTERN, "" );
		s = s.replace( "--", "" );
		s = s.replace( "\\", "" );
		//s = s.replace( "'" , "\\'" );
		//s = s.replace( "select", "" );
		s = s.replace( "insert", "" );
		s = s.replace( "update", "" );
		//s = s.replace( "delete", "" );
		//s = s.replace( "drop", "" );
		
		return s;
	}
	
	
	/**
	 * same issue as above escapeCharsBatch() method.
	 * 
	 * @param s
	 * @return
	 */
	public static String escapeCharsMinBatch( String s )
	{
		if ( s == null ) return "";
		
		s = s.replaceAll( FILTER_PATTERN_MIN, "" );
		//s = s.replace( "'" , "\\'" );
		
		return s;
	}
	
	
	
	
	
	
	/**
	 * removes/escapes invalid characters to protect from user error
	 * and javascript attacks
	 */
	public static String escapeSphinxChars( String s )
	{
		if ( s != null )
		{
			s = s.replaceAll( SPHINX_FILTER_PATTERN, "" );
			
			s = escapeAccentedChars( s );
		    
			s = s.replace( 'í', '\'' );
			s = s.replace( '\u2013', '-' ); // longer dash (ndash)
			s = s.replace( "--", "-" );
			s = s.replace( " - ", " " );
				    
			s = s.replaceAll( "[^\\p{ASCII}]", "" );
		}
		
		return s;
	}
	
	
	
	/**
	 * converts an int[] array to a comma-delimited string
	 * @param array
	 * @return
	 */
	public static String convertIntArrayToString( int[] array )
	{
		StringBuffer sb = new StringBuffer();
		
		if ( array != null )
		{
			for ( int x=0; x < array.length; x++ )
			{
				if ( sb.length() > 0 )
					sb.append( ", " );
				
				sb.append( array[ x ] );
			}
		}
		
		return sb.toString();
	}
	
	
	/**
	 * converts a comma-delimited string to an int[] array
	 * @param ids
	 * @return
	 */
	public static int[] convertStringToIntArray( String ids )
    {
		if ( ids == null ) return new int[] {};
		
    	StringTokenizer st = new StringTokenizer( ids, ", " );
    	int[] array = new int[ st.countTokens() ];
    	
    	for ( int x=0; st.hasMoreTokens(); x++ )
    	{
    		array[ x ] = Integer.parseInt( st.nextToken() );
    	}
    	
    	return array;
    }
	
	
	
	public static String readUserIdOrIpForMySQL( UserIpAction action )
	{
		// if userID is not set, use IP address instead
		String userID = null;
		
		if ( action.getUserID() > 0 ) {
			userID = action.getUserID() + "";
		}
		else if ( action.getIpAddress() != null ) {
			userID = "inet_aton('" + action.getIpAddress() + "')";
		}
		else 
			userID = "0";
		
		return userID;
	}
	

}
