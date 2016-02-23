package com.choicetrax.server.util.jaxb;

import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

import com.choicetrax.server.data.ObjectFactory;
import com.choicetrax.client.util.exception.ChoicetraxException;


public class JAXBHelper 
{
	
	private static JAXBContext jaxbContext 		= null;
	private static Marshaller marshaller		= null;
	private static Unmarshaller unmarshaller	= null;
	
	public static ObjectFactory jaxbFactory		= null;
	
	private static Logger logger = Logger.getLogger( JAXBHelper.class );
	
	
	static
	{
		try
		{
			initObjFactory();
		}
		catch ( Exception e ) {
			logger.error( "JAXBHelper initialization error", e );
		}
	}
	
	
	
	public static void shutdown()
	{
		try 
		{
			unmarshaller = null;
			marshaller = null;
			jaxbFactory = null;
			jaxbContext = null;
			
			System.gc();
		}
		catch ( Exception e ) {
			logger.error( "Error shutting down JAXBHelper", e );
		}
	}
	
	
	private static void initObjFactory() 
		throws ChoicetraxException
	{
		try
		{
			if ( jaxbContext == null ) 	jaxbContext = JAXBContext.newInstance( "com.choicetrax.server.data" );
			if ( jaxbFactory == null ) 	jaxbFactory = new com.choicetrax.server.data.ObjectFactory();
			
			if ( marshaller == null )
			{
				marshaller = jaxbContext.createMarshaller();
				//marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );
			}
			
			if ( unmarshaller == null )
			{
				unmarshaller = jaxbContext.createUnmarshaller();
			}
		}
		catch ( JAXBException j )
		{
			throw new ChoicetraxException( "JAXBException: " + j,
											"com.choicetrax.server.util.jaxb.JAXBHelper" );
		}
	}
	
	
	public static Object convertFromXML( String xml ) 
		throws ChoicetraxException
	{
		try
		{
			initObjFactory();
			
			StreamSource source = new StreamSource( new StringReader( xml ) );
			Object o = null;
			
			synchronized( unmarshaller ) {
				o = unmarshaller.unmarshal( source );
			}
			
			return o;
		}
		catch ( JAXBException j )
		{
			throw new ChoicetraxException( "JAXBException: " + j,
											"com.choicetrax.server.util.jaxb.JAXBHelper" );
		}
	}
	
	
	public static String convertToXML( Object xmlObj ) 
		throws ChoicetraxException
	{
		try
		{
			initObjFactory();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			synchronized ( marshaller ) {
				marshaller.marshal( xmlObj, baos );
			}
			
			return baos.toString().trim();
		}
		catch ( JAXBException j )
		{
			throw new ChoicetraxException( "JAXBException: " + j,
											"com.choicetrax.server.util.jaxb.JAXBHelper" );
		}
	}
	
	
	public static Date parseDate( String s )
	{
		return DatatypeConverter.parseDate( s ).getTime();
	}
	
	public static String printDate( Date dt )
	{
		Calendar c = new GregorianCalendar();
		c.setTime( dt );
		return DatatypeConverter.printDate( c );
	}

}
