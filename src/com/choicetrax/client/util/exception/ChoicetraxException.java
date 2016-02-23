package com.choicetrax.client.util.exception;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ChoicetraxException 
	extends Exception 
	implements IsSerializable
{
	
	private static final long	serialVersionUID	= 1L;
	
	private String errorMsg = null;
	private int errorCode = -1;
	private String callerMethod = null;
	
	private static final String SEPARATOR = "\r\n";
	
	
	
	public ChoicetraxException() {}
	
	
	public ChoicetraxException( String errorMsg, int errorCode )
	{
		super(errorMsg + " Error Code=" + errorCode);
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}
	
	
	public ChoicetraxException( String errorMsg )
	{
		super(errorMsg);
		this.errorMsg = errorMsg;
	}
	
	
	public ChoicetraxException( String errorMsg, String callerMethod )
	{
		super(errorMsg);
		this.errorMsg = errorMsg;
		this.callerMethod = callerMethod;
	}
	
	
	
	public String getErrorMsg()
	{
		return errorMsg;
	}
	
	public String getCallerMethod()
	{
		return callerMethod;
	}
	
	public int getErrorCode()
	{
		return errorCode;
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append( "ChoicetraxException: " + super.getMessage() + SEPARATOR );
		
		if ( this.getErrorMsg() != null )
			sb.append( "Error Msg: " + this.getErrorMsg() + SEPARATOR );
		
		if ( this.getErrorCode() != -1 )
			sb.append( "Error Code: " + this.getErrorCode() + SEPARATOR );
		
		if ( this.getCallerMethod() != null )
			sb.append( "Caller Method: " + this.getCallerMethod() + SEPARATOR );
		
		return sb.toString();
	}

}