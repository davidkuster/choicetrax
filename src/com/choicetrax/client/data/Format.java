package com.choicetrax.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.choicetrax.client.constants.Constants;

/**
 * @author David Kuster
 * dave@djtalldave.com
 *
 * Created December 12, 2007
 */
public class Format implements IsSerializable
{
    private String formatName;
    private String bitrate;
    private String formatCode;  // partner's format code
    private String price;
    
    public Format() {
    	super();
    }
    
    public Format(String input)
    {
    	super();
    	if ( input != null )
    		createFormat( input );
    }
    
    
    public Format(	String formatName, 
    				String formatCode,
    				String bitrate,
    				String price )
    {
    	setFormatName( formatName );
    	setFormatCode( formatCode );
    	setBitrate( bitrate );
    	setPrice( price );
    }
    
   
    public String toString()
    {
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append( "Format: " );
    	
    	if ( formatName != null )	sb.append( "formatName [" + formatName + "] " );
    	if ( bitrate != null )		sb.append( "bitrate [" + bitrate + "] " );
    	if ( price != null )		sb.append( "price [" + price + "] " );
    	
    	return sb.toString();
    }
    
	public String getFormatName() {
		return formatName;
	}
	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBitrate() {
		return bitrate;
	}
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}
	public String getFormatCode() {
		return formatCode;
	}
	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}
	
	private void createFormat(String input)
	{
		//StringTokenizer st = new StringTokenizer( input );
		//while ( st.hasMoreTokens() )
		//{
			//String token = st.nextToken();
	
		int index = input.indexOf( ' ' );
		if ( index != -1 )
		{
			String token = input.substring( 0, index - 1 );
		
			if ( isFormatName( token ) )
				this.setFormatName( token );
			else if ( isBitrate( token ) )
				this.setBitrate( token );
			else if ( isPrice( token ) )
				this.setPrice( token );
			
			createFormat( input.substring( index, input.length() ) );
		}
	//}
	}
	
	public static boolean isFormatName( String s )
	{
		if ( s == null )
			return false;
		else if ( Constants.FORMAT_MP3.equalsIgnoreCase( s ) )
			return true;
		else if ( Constants.FORMAT_WAV.equalsIgnoreCase( s ) )
			return true;
		else if ( Constants.FORMAT_AAC.equalsIgnoreCase( s ) )
			return true;
		else
			return false;		
	}
	
	public static boolean isBitrate( String s )
	{
		if ( s == null )
			return false;
		else if ( Constants.BITRATE_320K.equalsIgnoreCase( s ) )
			return true;
		else if ( Constants.BITRATE_192K.equalsIgnoreCase( s ) )
			return true;
		else if ( Constants.BITRATE_441KHZ.equalsIgnoreCase( s ) )
			return true;
		else
			return false;
	}
	
	public static boolean isPrice( String s )
	{
		if ( s == null )
			return false;
		else if ( s.startsWith( Constants.CURRENCY_US ) )
			return true;
		else if ( s.startsWith( Constants.CURRENCY_BRITISH ) )
			return true;
		else 
			return false;
	}

}