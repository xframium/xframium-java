package org.xframium.debugger.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.xframium.debugger.DebugManager;
import org.xframium.debugger.StepContainer;
import org.xframium.debugger.TestContainer;
import org.xframium.device.factory.DeviceWebDriver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings ( "restriction")
public class TestHandler implements HttpHandler
{
    @Override
    public void handle( HttpExchange t ) throws IOException
    {
        Map<String, String> parameterMap = queryToMap( t.getRequestURI().getQuery() );
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append( "<html><body>" );

        TestContainer tC = DebugManager.instance().getActiveTests().get( parameterMap.get( "executionId" ) );
        
        if ( tC == null )
        {
            tC = DebugManager.instance().getCompletedTests().get( parameterMap.get( "executionId" ) );
        }
        
        if ( tC != null )
        {
            for ( StepContainer sC : tC.getSteps() )
            {
                stringBuilder.append( "<li>" ).append( sC.getStep().getName() ).append( "</li>" );
            }
        }
        
        t.sendResponseHeaders( 200, stringBuilder.length() );
        OutputStream os = t.getResponseBody();
        os.write( stringBuilder.toString().getBytes() );
        os.close();
        
        stringBuilder.append( "</body></html>" );
        
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
