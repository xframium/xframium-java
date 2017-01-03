package org.xframium.console.http.handler.spi;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import org.xframium.console.http.ExecutionConsole;
import org.xframium.console.http.handler.ECHandler;
import org.xframium.container.DriverContainer;
import org.xframium.container.SuiteContainer;
import org.xframium.driver.ConfigurationReader;
import org.xframium.driver.TXTConfigurationReader;
import org.xframium.driver.TestDriver;
import org.xframium.driver.XMLConfigurationReader;
import org.xframium.page.data.provider.PageDataProvider;
import org.xframium.page.element.provider.ElementProvider;
import com.sun.net.httpserver.HttpExchange;
import com.xframium.serialization.SerializationManager;

@SuppressWarnings ( "restriction")
public class ExecuteTest extends ECHandler
{
    private TestDriver testDriver;

    @Override
    protected byte[] _handle( HttpExchange httpExchange )
    {
        try
        {
            if ( ExecutionConsole.instance().isTestExecuting() )
                throw new IllegalStateException( "Test already runnung" );
            
            final Map<String, String> queryMap = queryToMap( httpExchange.getRequestURI().getQuery() );

            queryMap.put( "driver.displayResults", "false" );
            final File configFile = new File( queryMap.get( "fileName" ) );
            
            new Thread( new Runnable()
            {

                @Override
                public void run()
                {
                    testDriver = new TestDriver();
                    testDriver.addSuiteListener( ExecutionConsole.instance() );
                    testDriver.execute( configFile, queryMap );
                }
            } ).start();

            

            return "ok".getBytes();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return e.getMessage().getBytes();
        }
    }
}
