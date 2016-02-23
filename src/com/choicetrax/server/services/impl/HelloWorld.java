package com.choicetrax.server.services.impl;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.*;

public class HelloWorld 
	extends HttpServlet 
{
	
	private static final long	serialVersionUID	= 1L;

	public void doGet( HttpServletRequest req, HttpServletResponse res )
	throws ServletException, IOException
	{
		PrintWriter out = res.getWriter();
		
		out.println( "Hello, world!" );
		out.close();
	}

}
