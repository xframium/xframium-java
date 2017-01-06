package org.xframium.console.http.handler.spi;

import org.xframium.console.http.handler.ECHandler;
import com.sun.net.httpserver.HttpExchange;

@SuppressWarnings("restriction")
public class KillSwitch extends ECHandler {

	@Override
	protected byte[] _handle(HttpExchange httpExchange) 
	{
	    log.warn( "Received Kill" );
	    
	    System.exit( 0 );
	    return null;
	}
}
