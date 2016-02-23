package com.choicetrax.client.util.exception;


public class FailedAuthenticationException extends ChoicetraxException 
{

	private static final long	serialVersionUID	= 1L;
	
	
	public FailedAuthenticationException() {
		super();
	}
	
	
	public FailedAuthenticationException( String errorMsg, int errorCode ) {
		super( errorMsg, errorCode );
	}
	
	
	public FailedAuthenticationException( String errorMsg ) {
		super( errorMsg );
	}
	
	
	public FailedAuthenticationException( String errorMsg, String callerMethod ) {
		super( errorMsg, callerMethod );
	}

}
