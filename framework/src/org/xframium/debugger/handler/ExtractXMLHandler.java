package org.xframium.debugger.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.xframium.debugger.DebugManager;
import org.xframium.debugger.TestContainer;
import org.xframium.utility.XMLEscape;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings ( "restriction")
public class ExtractXMLHandler implements HttpHandler
{
    @Override
    public void handle( HttpExchange t ) throws IOException
    {
        Map<String, String> parameterMap = queryToMap( t.getRequestURI().getQuery() );
        TestContainer tC = DebugManager.instance().getActiveTests().get( parameterMap.get( "executionId" ) );
        
        StringBuilder stringBuilder = new StringBuilder();
        if ( tC != null )
        {
            stringBuilder.append( XMLEscape.toXML( tC.getWebDriver().getPageSource() ) );
        }
        
        
        String returnValue = stringBuilder.toString();
        
        t.sendResponseHeaders( 200, returnValue.length() );
        OutputStream os = t.getResponseBody();
        os.write( returnValue.getBytes() );
        os.close();
    }
    
    public Map<String, String> queryToMap( String query )
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
}
