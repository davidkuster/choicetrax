package com.choicetrax.client.input;

import com.google.gwt.user.client.ui.Composite;

import com.choicetrax.client.data.ReleaseDetail;
import com.choicetrax.client.input.listeners.OptionsIconListener;
import com.choicetrax.client.logic.ChoicetraxViewManager;


public class HyperlinkIconOptions 
	extends Composite
{
	
	
	public HyperlinkIconOptions( ChoicetraxViewManager manager, ReleaseDetail rd )
	{
		final ChoicetraxViewManager viewManager = manager;
			
		final HyperlinkImage optionsIcon = new HyperlinkImage( 
											"img/layout/iconOptions.gif",
											"Search / Share",
											new OptionsIconListener( viewManager, rd ) );
		
		initWidget( optionsIcon );
	}

}
