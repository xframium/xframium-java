package org.xframium.debugger.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.xframium.debugger.DebugManager;
import org.xframium.debugger.TestContainer;
import org.xframium.device.factory.DeviceWebDriver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings ( "restriction")
public class ResumeHandler implements HttpHandler
{
    private String xFID;
    public ResumeHandler( String xFID )
    {
        this.xFID = xFID;
    }
    
    @Override
    public void handle( HttpExchange t ) throws IOException
    {
        Map<String, String> parameterMap = queryToMap( t.getRequestURI().getQuery() );
        TestContainer tC = DebugManager.instance(xFID).getActiveTests().get( parameterMap.get( "executionId" ) );
        
        if ( tC != null )
        {
            tC.resume();
        }
        
        
        String returnValue = "OK";
        
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
