package org.xframium.debugger.handler;

import java.io.IOException;
import java.io.OutputStream;
import org.xframium.debugger.DebugManager;
import org.xframium.debugger.TestContainer;
import org.xframium.device.factory.DeviceWebDriver;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings ( "restriction")
public class SessionHandler implements HttpHandler
{
    @Override
    public void handle( HttpExchange t ) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        
        stringBuilder.append( "<html><body><h2>Active Tests</h2><ol>" );
        
        for ( TestContainer tC : DebugManager.instance().getActiveTests().values() )
        {
            stringBuilder.append( "<li><a href=\"/testCase?executionId=" ).append( ( (DeviceWebDriver) tC.getWebDriver() ).getExecutionId() ).append( "\">" ).append( tC.getKeyWordTest().getName() ).append(  "<i>(" ).append( ( (DeviceWebDriver )tC.getWebDriver() ).getDevice().getEnvironment() ).append( ")</i></li>" );
        }
        
        stringBuilder.append( "</ol><h2>Completed Tests</h2><ol>" );
        for ( TestContainer tC : DebugManager.instance().getCompletedTests().values() )
        {
            stringBuilder.append( "<li><a href=\"/testCase?executionId=" ).append( ( (DeviceWebDriver) tC.getWebDriver() ).getExecutionId() ).append( "\">" ).append( tC.getKeyWordTest().getName() ).append(  "<i>(" ).append( ( (DeviceWebDriver )tC.getWebDriver() ).getDevice().getEnvironment() ).append( ")</i></li>" );
        }
        
        
        
        stringBuilder.append( "</body></html>" );

        t.sendResponseHeaders( 200, stringBuilder.length() );
        OutputStream os = t.getResponseBody();
        os.write( stringBuilder.toString().getBytes() );
        os.close();
    }
}
