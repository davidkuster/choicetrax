package com.choicetrax.client.util.exception;


public class UserInputException extends ChoicetraxException
{

	private static final long	serialVersionUID	= 1L;
	
	
	
	public UserInputException() {
	}

	public UserInputException( String errorMsg, int errorCode ) {
		super( errorMsg, errorCode );
	}

	public UserInputException( String errorMsg ) {
		super( errorMsg );
	}

	public UserInputException( String errorMsg, String callerMethod ) {
		super( errorMsg, callerMethod );
	}

}
