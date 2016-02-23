package com.choicetrax.client.util.exception;


public class ChoicetraxEmailException extends ChoicetraxException
{

	private static final long	serialVersionUID	= 1L;
	
	

	public ChoicetraxEmailException() {
	}

	public ChoicetraxEmailException( String errorMsg, int errorCode ) {
		super( errorMsg, errorCode );
	}

	public ChoicetraxEmailException( String errorMsg ) {
		super( errorMsg );
	}

	public ChoicetraxEmailException( String errorMsg, String callerMethod ) {
		super( errorMsg, callerMethod );
	}

}
