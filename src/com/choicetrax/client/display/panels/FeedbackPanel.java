package com.choicetrax.client.display.panels;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.choicetrax.client.actions.handleractions.FeedbackAction;
import com.choicetrax.client.constants.Constants;
import com.choicetrax.client.logic.ChoicetraxViewManager;
import com.choicetrax.client.logic.callbacks.DoNothingController;


public class FeedbackPanel extends Composite
{
	
	private ChoicetraxViewManager viewManager = null;
	
	private TextBox name = new TextBox();
	private TextBox email = new TextBox();
	private TextBox subject = new TextBox();
	private TextArea text = new TextArea();
	

	public FeedbackPanel( ChoicetraxViewManager manager ) 
	{
		super();
		this.viewManager = manager;
		
		text.setCharacterWidth( 50 );
		text.setVisibleLines( 5 );
		
		Widget display = createDisplay();
		initWidget( display );
	}
	
	
	private Widget createDisplay()
	{
		name.setStyleName( "accountTextField" );
		email.setStyleName( "accountTextField" );
		subject.setStyleName( "accountTextField" );
		
		FlexTable table = new FlexTable();
				
		int row = 0;
		
		Label nameOptional = new Label( "(optional)" );
		nameOptional.setStylePrimaryName( "smallerItalicText" );
		
		table.setWidget( row, 0, new Label( "Name: " ) );
		table.setWidget( row, 1, name );
		table.setWidget( row, 2, nameOptional );
		table.getFlexCellFormatter().setHorizontalAlignment( row, 2, 
										HasHorizontalAlignment.ALIGN_LEFT );
		row++;
		
		Label emailOptional = new Label( "(optional)" );
		emailOptional.setStylePrimaryName( "smallerItalicText" );
		
		table.setWidget( row, 0, new Label( "Email: " ) );
		table.setWidget( row, 1, email );
		table.setWidget( row, 2, emailOptional );
		table.getFlexCellFormatter().setWidth( row, 2, "100%" );
		table.getFlexCellFormatter().setHorizontalAlignment( row, 2, 
										HasHorizontalAlignment.ALIGN_LEFT );
		row++;
		
		table.setWidget( row, 0, new Label( "Subject: " ) );
		table.setWidget( row, 1, subject );
		row++;
		
		table.setWidget( row, 0, new Label( "Feedback/ Question/ Problem: " ) );
		table.setWidget( row, 1, text );
		table.getFlexCellFormatter().setColSpan( row, 1, 2 );
		row++;
		
		Button submit = new Button( "Send Feedback" );
		submit.addClickHandler( new SubmitListener() );
		
		table.setWidget( row, 0, submit );
		table.getFlexCellFormatter().setColSpan( row, 0, 3 );
		table.getCellFormatter().setAlignment( row, 0, 
										HasHorizontalAlignment.ALIGN_CENTER, 
										HasVerticalAlignment.ALIGN_MIDDLE );
				
		return table;
	}
	
	
	private boolean validateInput()
	{
		String subjectText = subject.getText().trim();
		String feedbackText = text.getText().trim();
		
		if ( "".equals( subjectText ) ) {
			Window.alert( "Please enter a subject." );
			subject.setFocus( true );
			return false;
		}
		else if ( "".equals( feedbackText ) ) {
			Window.alert( "Please enter feedback." );
			text.setFocus( true );
			return false;
		}
		
		return true;
	}
	
	
	private void submitFeedback()
	{
		if ( validateInput() )
		{
			FeedbackAction action = new FeedbackAction();
			action.setActionType( Constants.ACTION_ADD );
			action.setName( name.getText() );
			action.setEmailAddress( email.getText() );
			action.setFeedbackSubject( subject.getText() );
			action.setFeedbackText( text.getText() );
			
			if ( viewManager.getCurrentUser() != null )
				action.setUserID( viewManager.getCurrentUser().getUserID() );
			
			viewManager.deferAction( action, new DoNothingController() );
			
			Window.alert( "Feedback sent!"
						+ "\n\n"
						+ "If you have a problem or asked a question \n"
						+ "(and included your email address) \n"
						+ "we'll try to get back to you ASAP." );
			
			clearTextBoxes();
		}
	}
	
	
	
	private void clearTextBoxes()
	{
		name.setText( "" );
		email.setText( "" );
		subject.setText( "" );
		text.setText( "" );
	}
	
	
	private class SubmitListener implements ClickHandler
	{
		public void onClick( ClickEvent event ) {
			submitFeedback();
		}
	}

}
