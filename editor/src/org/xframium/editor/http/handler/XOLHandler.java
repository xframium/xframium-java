package org.xframium.editor.http.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public abstract class XOLHandler implements HttpHandler
{
	protected abstract byte[] _handle( HttpExchange httpExchange );
	protected Log log = LogFactory.getLog(XOLHandler.class);
	
	protected Map<String, String> queryToMap( String query )
    {
        Map<String, String> result = new HashMap<String, String>();
        for ( String param : query.split( "&" ) )
        {
            String pair[] = param.split( "=" );
            if ( pair.length > 1 )
            {
                result.put( pair[0], pair[1] );
            }
            else
            {
                result.put( pair[0], "" );
            }
        }
        return result;
    }

	public void handle(HttpExchange httpExchange) throws IOException 
	{
		if ( log.isInfoEnabled() )
			log.info(  httpExchange.getRequestURI().toString() );
		byte[] byteData = _handle( httpExchange );
		if ( byteData == null || byteData.length <= 0)
			byteData = "OK".getBytes();
		
		httpExchange.sendResponseHeaders( 200, byteData.length );
        OutputStream os = httpExchange.getResponseBody();
        os.write( byteData );
        os.close();

		
	}
	
	
}
