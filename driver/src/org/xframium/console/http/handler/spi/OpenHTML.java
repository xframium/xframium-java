package org.xframium.console.http.handler.spi;

import java.awt.Desktop;
import java.net.URI;
import java.util.Map;
import org.xframium.console.http.handler.ECHandler;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class OpenHTML extends ECHandler
{

    @Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{	
	    try
	    {
	        Map<String,String> queryMap = queryToMap( httpExchange.getRequestURI().getQuery() );
	        
    		String fileName = queryMap.get( "fileName" );
    		
    		Desktop.getDesktop().browse( new URI( "file:///" + fileName.replace( " ", "%20" ) ) );
    		return "ok".getBytes();
	    }
	    catch( Exception e )
	    {
	        e.printStackTrace();
	        return e.getMessage().getBytes();
	    }
	}
}
