package com.choicetrax.client.util.exception;


public class ChoicetraxSphinxException extends ChoicetraxException
{

	private static final long	serialVersionUID	= 1L;
	
	

	public ChoicetraxSphinxException() {
	}

	public ChoicetraxSphinxException( String errorMsg, int errorCode ) {
		super( errorMsg, errorCode );
	}

	public ChoicetraxSphinxException( String errorMsg ) {
		super( errorMsg );
	}

	public ChoicetraxSphinxException( String errorMsg, String callerMethod ) {
		super( errorMsg, callerMethod );
	}

}
