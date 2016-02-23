package com.choicetrax.client.input;

import java.util.Date;
import com.thapar.gwt.user.ui.client.widget.simpledatepicker.SimpleDatePicker;
import com.thapar.gwt.user.ui.client.widget.simpledatepicker.DateFormatter;

import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.datepicker.client.DateBox;

import com.choicetrax.client.constants.Constants;


public class DateField 
	extends SimpleDatePicker
	//extends DateBox
	implements ClearableInputBox
{
	
	private String initialText = null;
	
	public DateField( String name )
	{
		super();
		this.setText( name );
		this.setWeekendSelectable( true );
		this.setDateFormat( DateFormatter.DATE_FORMAT_MMDDYYYY );
		
		this.initialText = name;
	}
	
	
	public String getInitialText() {
		return this.initialText;
	}
	
	
	
	public Date getDateValue()
	{
		String text = this.getText();
		
		if ( ( text != null ) 
			&& ( ! text.trim().equals("") )
			&& ( ! text.equals( initialText ) ) )
		{
			try {
				return Constants.DATE_FORMATTER_USA.parse( text );
			} catch ( Exception e ) {
				GWT.log( "DateField.getValue() exception", e );
			}
			
			// thapar's code is buggy so getting the selected date
			// really only ever returns the current date
			
			//return this.getSelectedDate();
		}
		
		return null;
	}
	
	
	public void setValue( String dateString )
	{
		Date date = Constants.DATE_FORMATTER_USA.parse( dateString );
		
		this.setSelectedDate( date );
		this.setText( dateString );
	}

}
