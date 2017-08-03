package org.xframium.debugger.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.xframium.debugger.DebugManager;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings ( "restriction")
public class SessionHandler implements HttpHandler
{
    private String xFID;
    public SessionHandler( String xFID )
    {
        this.xFID = xFID;
    }
    
    @Override
    public void handle( HttpExchange t ) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> parameterMap = queryToMap( t.getRequestURI().getQuery() );
        
        if ( parameterMap.get( "ajax" ) != null )
        {
        	stringBuilder.append( new String( SerializationManager.instance().toByteArray( SerializationManager.instance().getAdapter( SerializationManager.JSON_SERIALIZATION ), DebugManager.instance(xFID).getTests(), 0 ) ) );
        }
        else
        {
	        InputStream inputStream = null;
	        try
	        {
	            int bytesRead = 0;
	            byte[] buffer = new byte[ 512 ];
	            inputStream = DebugManager.class.getResourceAsStream( "sessions.html" );
	            
	            while ( ( bytesRead = inputStream.read( buffer ) ) > 0 )
	            {
	                stringBuilder.append( new String (buffer, 0, bytesRead ) );
	            }
	            
	        }
	        catch( Exception e )
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	            try { inputStream.close(); } catch( Exception e ){}
	        }
        }


        t.sendResponseHeaders( 200, stringBuilder.length() );
        OutputStream os = t.getResponseBody();
        os.write( stringBuilder.toString().getBytes() );
        os.close();
    }
    
    public Map<String, String> queryToMap( String query )
    {

        Map<String, String> result = new HashMap<String, String>();
        if ( query == null )
        	return result;
        
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
}
