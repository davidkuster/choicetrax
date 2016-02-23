package com.choicetrax.client.util.exception;


public class ChoicetraxDatabaseException extends ChoicetraxException
{

	private static final long	serialVersionUID	= 1L;
	
	

	public ChoicetraxDatabaseException() {
		super();
	}

	public ChoicetraxDatabaseException( String errorMsg, int errorCode ) {
		super( errorMsg, errorCode );
	}

	public ChoicetraxDatabaseException( String errorMsg ) {
		super( errorMsg );
	}

	public ChoicetraxDatabaseException( String errorMsg, String callerMethod ) {
		super( errorMsg, callerMethod );
	}

}
